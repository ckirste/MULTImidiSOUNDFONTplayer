package jp.kshoji.javax.sound.midi;

import androidx.annotation.*;

/**
 * Interface for {@link MidiDevice} transmitter.
 *
 * @author K.Shoji
 */
public interface MidiDeviceTransmitter extends Transmitter {

    /**
     * Get the {@link MidiDevice} associated with this instance.
     *
     * @return the {@link MidiDevice} associated with this instance.
     */
    @NonNull
    MidiDevice getMidiDevice();
}
