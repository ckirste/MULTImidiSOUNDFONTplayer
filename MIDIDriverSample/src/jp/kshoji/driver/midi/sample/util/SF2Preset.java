package jp.kshoji.driver.midi.sample.util;

import java.io.Serializable;

public class SF2Preset implements Serializable {
    private String presetname;
    private int presetbank;
    private int presetprogr;




    public SF2Preset(String presetname,int presetbank,int presetprogr){

        this.presetname=presetname;
        this.presetbank=presetbank;
        this.presetprogr=presetprogr;

    }
    public int getPresetprogr() {
        return presetprogr;
    }

    public void setPresetprogr(int presetprogr) {
        this.presetprogr = presetprogr;
    }



    public String getPresetname() {
        return presetname;
    }

    public void setPresetname(String presetname) {
        this.presetname = presetname;
    }



    public int getPresetbank() {
        return presetbank;
    }

    public void setPresetbank(int presetbank) {
        this.presetbank = presetbank;
    }




}
