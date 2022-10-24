package jp.kshoji.driver.midi.sample;

import android.content.Context;

public class Synthesizer {

    static {

        System.loadLibrary("native-lib");

    }

    Synthesizer(Context c){






    }


    public native void fluidsynth_ListSendNoteOnMessage(int channel,int note, int velocity, int iUsbId);

    public native void fluidsynth_ListSendNoteOffMessage(int channel,int note, int iUsbId);

}
