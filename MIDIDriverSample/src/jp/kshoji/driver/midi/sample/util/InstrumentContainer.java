package jp.kshoji.driver.midi.sample.util;

import java.io.Serializable;

public class InstrumentContainer implements Serializable {

    public InstrumentContainer(){


    }
    public String getSoundfontPath() {
        return soundfontPath;
    }

    public void setSoundfontPath(String soundfontPath) {
        this.soundfontPath = soundfontPath;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public int getUsbDeviceID() {
        return usbDeviceID;
    }

    public void setUsbDeviceID(int usbDeviceID) {
        this.usbDeviceID = usbDeviceID;
    }

    private String soundfontPath;
    private String instrumentName;
    private boolean isOn;
    private int usbDeviceID;




}
