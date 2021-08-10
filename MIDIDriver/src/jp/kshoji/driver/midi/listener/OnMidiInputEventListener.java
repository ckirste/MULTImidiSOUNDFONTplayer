package jp.kshoji.driver.midi.listener;

import android.support.annotation.NonNull;

import jp.kshoji.driver.midi.device.MidiInputDevice;

/**
 * Listener for MIDI events
 * For events' details, @see <a href="http://www.usb.org/developers/devclass_docs/midi10.pdf">Universal Serial Bus Device Class Definition for MIDI Devices</a>
 * 
 * @author K.Shoji
 */
public interface OnMidiInputEventListener {
	
	/**
	 * Miscellaneous function codes. Reserved for future extensions.
	 * Code Index Number : 0x0
	 *
     * @param sender the Object which the event sent
     * @param cable the cable ID 0-15
	 * @param byte1 the first byte
	 * @param byte2 the second byte
	 * @param byte3 the third byte
	 */
	void onMidiMiscellaneousFunctionCodes(@NonNull MidiInputDevice sender, int cable, int byte1, int byte2, int byte3);
	
	/**
	 * Cable events. Reserved for future expansion.
	 * Code Index Number : 0x1
	 *
     * @param sender the Object which the event sent
     * @param cable the cable ID 0-15
     * @param byte1 the first byte
     * @param byte2 the second byte
     * @param byte3 the third byte
	 */
	void onMidiCableEvents(@NonNull MidiInputDevice sender, int cable, int byte1, int byte2, int byte3);
	
	/**
	 * System Common messages, or SysEx ends with following single byte.
	 * Code Index Number : 0x2 0x3 0x5
	 *
     * @param sender the Object which the event sent
     * @param cable the cable ID 0-15
	 * @param bytes bytes.length:1, 2, or 3
	 */
	void onMidiSystemCommonMessage(@NonNull MidiInputDevice sender, int cable, byte[] bytes);
	
	/**
	 * SysEx
	 * Code Index Number : 0x4, 0x5, 0x6, 0x7
	 *
     * @param sender the Object which the event sent
     * @param cable the cable ID 0-15
	 * @param systemExclusive the SysEx message
	 */
	void onMidiSystemExclusive(@NonNull MidiInputDevice sender, int cable, byte[] systemExclusive);
	
	/**
	 * Note-off
	 * Code Index Number : 0x8
	 *
     * @param sender the Object which the event sent
     * @param cable the cable ID 0-15
	 * @param channel 0-15
	 * @param note 0-127
	 * @param velocity 0-127
	 */
	void onMidiNoteOff(@NonNull MidiInputDevice sender, int cable, int channel, int note, int velocity);
	
	/**
	 * Note-on
	 * Code Index Number : 0x9
	 *
     * @param sender the Object which the event sent
     * @param cable the cable ID 0-15
     * @param channel the MIDI channel number 0-15
	 * @param note 0-127
	 * @param velocity 0-127
	 */
	void onMidiNoteOn(@NonNull MidiInputDevice sender, int cable, int channel, int note, int velocity);
	
	/**
	 * Poly-KeyPress
	 * Code Index Number : 0xa
	 *
     * @param sender the Object which the event sent
     * @param cable the cable ID 0-15
     * @param channel the MIDI channel number 0-15
	 * @param note 0-127
	 * @param pressure 0-127
	 */
	void onMidiPolyphonicAftertouch(@NonNull MidiInputDevice sender, int cable, int channel, int note, int pressure);
	
	/**
	 * Control Change
	 * Code Index Number : 0xb
	 *
     * @param sender the Object which the event sent
     * @param cable the cable ID 0-15
     * @param channel the MIDI channel number 0-15
	 * @param function 0-127
	 * @param value 0-127
	 */
	void onMidiControlChange(@NonNull MidiInputDevice sender, int cable, int channel, int function, int value);
	
	/**
	 * Program Change
	 * Code Index Number : 0xc
	 *
     * @param sender the Object which the event sent
     * @param cable the cable ID 0-15
     * @param channel the MIDI channel number 0-15
	 * @param program 0-127
	 */
	void onMidiProgramChange(@NonNull MidiInputDevice sender, int cable, int channel, int program);
	
	/**
	 * Channel Pressure
	 * Code Index Number : 0xd
	 *
     * @param sender the Object which the event sent
     * @param cable the cable ID 0-15
     * @param channel the MIDI channel number 0-15
	 * @param pressure 0-127
	 */
	void onMidiChannelAftertouch(@NonNull MidiInputDevice sender, int cable, int channel, int pressure);
	
	/**
	 * PitchBend Change
	 * Code Index Number : 0xe
	 *
     * @param sender the Object which the event sent
     * @param cable the cable ID 0-15
     * @param channel the MIDI channel number 0-15
	 * @param amount 0(low)-8192(center)-16383(high)
	 */
	void onMidiPitchWheel(@NonNull MidiInputDevice sender, int cable, int channel, int amount);
	
	/**
	 * Single Byte
	 * Code Index Number : 0xf
	 *
     * @param sender the Object which the event sent
     * @param cable the cable ID 0-15
	 * @param byte1 the first byte
	 */
	void onMidiSingleByte(@NonNull MidiInputDevice sender, int cable, int byte1);

	/**
	 * RPN message
	 *
     * @param sender the Object which the event sent
     * @param cable the cable ID 0-15
     * @param channel the MIDI channel number 0-15
	 * @param function 14bits
	 * @param valueMSB higher 7bits
	 * @param valueLSB lower 7bits. -1 if value has no LSB. If you know the function's parameter value have LSB, you must ignore when valueLSB < 0.
	 */
	void onMidiRPNReceived(@NonNull MidiInputDevice sender, int cable, int channel, int function, int valueMSB, int valueLSB);

	/**
	 * NRPN message
	 *
     * @param sender the Object which the event sent
     * @param cable the cable ID 0-15
     * @param channel the MIDI channel number 0-15
	 * @param function 14bits
	 * @param valueMSB higher 7bits
	 * @param valueLSB lower 7bits. -1 if value has no LSB. If you know the function's parameter value have LSB, you must ignore when valueLSB < 0.
	 */
	void onMidiNRPNReceived(@NonNull MidiInputDevice sender, int cable, int channel, int function, int valueMSB, int valueLSB);
}
