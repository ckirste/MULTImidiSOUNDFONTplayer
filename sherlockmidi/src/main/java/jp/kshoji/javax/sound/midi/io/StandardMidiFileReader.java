package jp.kshoji.javax.sound.midi.io;

import android.content.res.AssetManager.AssetInputStream;
import androidx.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import jp.kshoji.javax.sound.midi.InvalidMidiDataException;
import jp.kshoji.javax.sound.midi.MetaMessage;
import jp.kshoji.javax.sound.midi.MidiEvent;
import jp.kshoji.javax.sound.midi.MidiFileFormat;
import jp.kshoji.javax.sound.midi.MidiMessage;
import jp.kshoji.javax.sound.midi.Sequence;
import jp.kshoji.javax.sound.midi.ShortMessage;
import jp.kshoji.javax.sound.midi.SysexMessage;
import jp.kshoji.javax.sound.midi.Track;
import jp.kshoji.javax.sound.midi.Track.TrackUtils;
import jp.kshoji.javax.sound.midi.spi.MidiFileReader;

/**
 * The implementation SMF reader
 *
 * @author K.Shoji
 */
public class StandardMidiFileReader extends MidiFileReader {

    /**
     * Represents Extended MIDI File format
     *
     * @author K.Shoji
     */
	class ExtendedMidiFileFormat extends MidiFileFormat {
		private int numberOfTracks;

		/**
		 * Get the number of tracks for this MIDI file.
		 * 
		 * @return the number of tracks for this MIDI file
		 */
		public int getNumberTracks() {
			return numberOfTracks;
		}

		/**
		 * Create an {@link ExtendedMidiFileFormat} object from the given parameters.
		 * 
		 * @param type the MIDI file type (0, 1, or 2)
		 * @param divisionType the MIDI file division type
		 * @param resolution the MIDI file timing resolution
		 * @param bytes the MIDI file size in bytes
		 * @param microseconds the MIDI file length in microseconds
		 * @param numberOfTracks the number of tracks
		 */
		public ExtendedMidiFileFormat(int type, float divisionType, int resolution, int bytes, long microseconds, int numberOfTracks) {
			super(type, divisionType, resolution, bytes, microseconds);
			this.numberOfTracks = numberOfTracks;
		}
	}

    /**
     * Represents InputStream for MIDI Data
     *
     * @author K.Shoji
     */
	class MidiDataInputStream extends DataInputStream {

        /**
         * Constructor
         *
         * @param inputStream the source stream
         */
		public MidiDataInputStream(@NonNull InputStream inputStream) {
			super(inputStream);
		}

        /**
         * Read value from InputStream
         *
         * @return the variable
         * @throws IOException
         */
		public int readVariableLengthInt() throws IOException {
			int c;
			int value = readByte();

			if ((value & 0x80) != 0) {
				value &= 0x7f;
				do {
					value = (value << 7) + ((c = readByte()) & 0x7f);
				} while ((c & 0x80) != 0);
			}

			return value;
		}
	}

    @NonNull
    @Override
	public MidiFileFormat getMidiFileFormat(@NonNull InputStream inputStream) throws InvalidMidiDataException, IOException {
		DataInputStream dataInputStream;
		if (inputStream instanceof DataInputStream) {
			dataInputStream = (DataInputStream) inputStream;
		} else if (inputStream instanceof AssetInputStream) {
			// AssetInputStream can't read with DataInputStream
			dataInputStream = new MidiDataInputStream(convertToByteArrayInputStream(inputStream));
		} else {
			dataInputStream = new DataInputStream(inputStream);
		}
		
		try {
			int type, numberOfTracks, division, resolution, bytes;
			float divisionType;
	
			if (dataInputStream.readInt() != MidiFileFormat.HEADER_MThd) {
				throw new InvalidMidiDataException("Invalid header");
			}
	
			bytes = dataInputStream.readInt();
			if (bytes < 6) {
				throw new InvalidMidiDataException("Invalid header");
			}
	
			type = dataInputStream.readShort();
			if (type < 0 || type > 2) {
				throw new InvalidMidiDataException("Invalid header");
			}
	
			numberOfTracks = dataInputStream.readShort();
			if (numberOfTracks <= 0) {
				throw new InvalidMidiDataException("Invalid tracks");
			}
	
			division = dataInputStream.readShort();
			if ((division & 0x8000) != 0) {
				division = -((division >>> 8) & 0xff);
				switch (division) {
				case 24:
					divisionType = Sequence.SMPTE_24;
					break;
				case 25:
					divisionType = Sequence.SMPTE_25;
					break;
				case 29:
					divisionType = Sequence.SMPTE_30DROP;
					break;
				case 30:
					divisionType = Sequence.SMPTE_30;
					break;
	
				default:
					throw new InvalidMidiDataException("Invalid sequence information");
				}
				resolution = division & 0xff;
			} else {
				divisionType = Sequence.PPQ;
				resolution = division & 0x7fff;
			}
	
			dataInputStream.skip(bytes - 6);
	
			return new ExtendedMidiFileFormat(type, divisionType, resolution, MidiFileFormat.UNKNOWN_LENGTH, MidiFileFormat.UNKNOWN_LENGTH, numberOfTracks);
		} finally {
			dataInputStream.close();
		}
	}

	@NonNull
    @Override
	public MidiFileFormat getMidiFileFormat(@NonNull URL url) throws InvalidMidiDataException, IOException {
		InputStream inputStream = url.openStream();
		try {
			return getMidiFileFormat(inputStream);
		} finally {
			inputStream.close();
		}
	}

	@NonNull
    @Override
	public MidiFileFormat getMidiFileFormat(@NonNull File file) throws InvalidMidiDataException, IOException {
		InputStream inputStream = new FileInputStream(file);
		try {
			return getMidiFileFormat(inputStream);
		} finally {
			inputStream.close();
		}
	}

	@NonNull
    @Override
	public Sequence getSequence(@NonNull InputStream inputStream) throws InvalidMidiDataException, IOException {
		MidiDataInputStream midiDataInputStream = new MidiDataInputStream(convertToByteArrayInputStream(inputStream));
		
		try {
			ExtendedMidiFileFormat midiFileFormat = (ExtendedMidiFileFormat) getMidiFileFormat(midiDataInputStream);
			Sequence sequence = new Sequence(midiFileFormat.getDivisionType(), midiFileFormat.getResolution());
	
			int numberOfTracks = midiFileFormat.getNumberTracks();
			
			while (numberOfTracks-- > 0) {
				final Track track = sequence.createTrack();
				if (midiDataInputStream.readInt() != MidiFileFormat.HEADER_MTrk) {
					throw new InvalidMidiDataException("Invalid track header");
				}
				// track length: ignored
				midiDataInputStream.readInt();
	
				int runningStatus = -1;
				int ticks = 0;
				boolean isEndOfTrack = false;
	
				// Read all of the events.
				while (!isEndOfTrack) {
					final MidiMessage message;

					ticks += midiDataInputStream.readVariableLengthInt(); // add deltaTime
	
					int data = midiDataInputStream.readUnsignedByte();
					if (data < 0x80) {
						// data values
						if (runningStatus >= 0 && runningStatus < 0xf0) {
							message = processRunningMessage(runningStatus, data, midiDataInputStream);
						} else if (runningStatus >= 0xf0 && runningStatus <= 0xff) {
							message = processSystemMessage(runningStatus, data, midiDataInputStream);
						} else {
							throw new InvalidMidiDataException(String.format("Invalid data: %02x %02x", runningStatus, data));
						}
					} else if (data < 0xf0) {
						// Control messages
						message = processRunningMessage(data, midiDataInputStream.readUnsignedByte(), midiDataInputStream);

						runningStatus = data;
					} else if (data == ShortMessage.START_OF_EXCLUSIVE || data == ShortMessage.END_OF_EXCLUSIVE) {
						// System Exclusive event
						int sysexLength = midiDataInputStream.readVariableLengthInt();
						byte sysexData[] = new byte[sysexLength];
						midiDataInputStream.readFully(sysexData);
						
						SysexMessage sysexMessage = new SysexMessage();
						sysexMessage.setMessage(data, sysexData, sysexLength);
						message = sysexMessage;
						
						runningStatus = -1;
					} else if (data == MetaMessage.META) {
						// Meta Message
						int type = midiDataInputStream.readUnsignedByte();
						
						int metaLength = midiDataInputStream.readVariableLengthInt();
						byte metaData[] = new byte[metaLength];
						midiDataInputStream.readFully(metaData);
						
						MetaMessage metaMessage = new MetaMessage();
						metaMessage.setMessage(type, metaData, metaLength);
						message = metaMessage;
						
						runningStatus = -1;
						
						if (type == MetaMessage.TYPE_END_OF_TRACK) {
							isEndOfTrack = true;
						}
					} else {
						// f1-f6, f8-fe
						message = processSystemMessage(data, null, midiDataInputStream);
						
						runningStatus = data;
					}

					track.add(new MidiEvent(message, ticks));
				}
				
				TrackUtils.sortEvents(track);
			}
	
			return sequence;
		} finally {
			midiDataInputStream.close();
		}
	}

    /**
     * Process the {@link SysexMessage}
     *
     * @param data1 the first data
     * @param data2 the second data
     * @param midiDataInputStream the InputStream
     * @return the processed MIDI message
     * @throws InvalidMidiDataException invalid MIDI data inputted
     * @throws IOException
     */
    @NonNull
	private static ShortMessage processSystemMessage(int data1, Integer data2, @NonNull MidiDataInputStream midiDataInputStream) throws InvalidMidiDataException, IOException {
		ShortMessage shortMessage;
		switch (data1) {
		case ShortMessage.SONG_POSITION_POINTER://f2
			shortMessage = new ShortMessage();
			if (data2 == null) {
				shortMessage.setMessage(data1, midiDataInputStream.readUnsignedByte(), midiDataInputStream.readUnsignedByte());
			} else {
				shortMessage.setMessage(data1, data2, midiDataInputStream.readUnsignedByte());
			}
			break;
			
		case ShortMessage.SONG_SELECT://f3
		case ShortMessage.BUS_SELECT://f5
			shortMessage = new ShortMessage();
			if (data2 == null) {
				shortMessage.setMessage(data1, midiDataInputStream.readUnsignedByte(), 0);
			} else {
				shortMessage.setMessage(data1, data2, 0);
			}
			break;
		
		case ShortMessage.TUNE_REQUEST://f6
		case ShortMessage.TIMING_CLOCK://f8
		case ShortMessage.START://fa
		case ShortMessage.CONTINUE://fb
		case ShortMessage.STOP://fc
		case ShortMessage.ACTIVE_SENSING://fe
			if (data2 != null) {
				// XXX must be ignored??
				throw new InvalidMidiDataException(String.format("Invalid data: %02x", data2));
			}
			shortMessage = new ShortMessage();
			shortMessage.setMessage(data1, 0, 0);
			break;
		
		default://f1, f9, fd
			throw new InvalidMidiDataException(String.format("Invalid data: %02x", data1));
		}
		return shortMessage;
	}

    /**
     * Process the MIDI running message
     *
     * @param status running status
     * @param data1 the first data
     * @param midiDataInputStream the InputStream
     * @return the processed MIDI message
     * @throws InvalidMidiDataException invalid MIDI data inputted
     * @throws IOException
     */
    @NonNull
    private static ShortMessage processRunningMessage(int status, int data1, @NonNull MidiDataInputStream midiDataInputStream) throws InvalidMidiDataException, IOException {
		ShortMessage shortMessage;
		switch (status & ShortMessage.MASK_EVENT) {
		case ShortMessage.NOTE_OFF://80
		case ShortMessage.NOTE_ON://90
		case ShortMessage.POLY_PRESSURE://a0
		case ShortMessage.CONTROL_CHANGE://b0
		case ShortMessage.PITCH_BEND://e0
			shortMessage = new ShortMessage();
			shortMessage.setMessage(status, data1, midiDataInputStream.readUnsignedByte());
			break;

		case ShortMessage.PROGRAM_CHANGE://c0
		case ShortMessage.CHANNEL_PRESSURE://d0
			shortMessage = new ShortMessage();
			shortMessage.setMessage(status, data1, 0);
			break;

		default:
			throw new InvalidMidiDataException(String.format("Invalid data: %02x %02x", status, data1));
		}
		
		return shortMessage;
	}

	/**
	 * Convert inputStream into {@link ByteArrayInputStream}
	 * 
	 * @param inputStream the {@link InputStream} instance
	 * @return the {@link ByteArrayInputStream}
	 * @throws IOException
	 */
    @NonNull
    private static ByteArrayInputStream convertToByteArrayInputStream(@NonNull InputStream inputStream) throws IOException {
		if (inputStream instanceof ByteArrayInputStream) {
			// already ByteArrayInputStream
			return (ByteArrayInputStream) inputStream;
		}
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[10240];
		int readBytes = 0;
		while ((readBytes = inputStream.read(buffer)) >= 0) {
			outputStream.write(buffer, 0, readBytes);
		}

        return new ByteArrayInputStream(outputStream.toByteArray());
	}

	@NonNull
    @Override
	public Sequence getSequence(@NonNull URL url) throws InvalidMidiDataException, IOException {
		InputStream inputStream = url.openStream();
		try {
			return getSequence(inputStream);
		} finally {
			inputStream.close();
		}
	}

	@NonNull
    @Override
	public Sequence getSequence(@NonNull File file) throws InvalidMidiDataException, IOException {
		InputStream inputStream = new FileInputStream(file);
		try {
			return getSequence(inputStream);
		} finally {
			inputStream.close();
		}
	}
}
