package jp.kshoji.driver.midi.sample.util;

import java.io.Serializable;

public class EffectContainer implements Serializable
{
	
	/*
	public native int getFluidsynthLocalSplitNote(int global_channel,int usbDeviceId,String instr);

    public native void setFluidsynthBoolSplitNotes(int global_channel,int usbDeviceId,String instr, boolean boolSplitNotes);

    public native void setFluidsynthBoolMuteNotesSmalerThan(int global_channel,int usbDeviceId,String instr, boolean boolMuteNotesSmalerThan);

    public native void setFluidsynthBoolMuteNotesGreaterThan(int global_channel,int usbDeviceId,String instr, boolean boolMuteNotesGreaterThan);

    public native void setFluidsynthChorusActive(int global_channel, int usbDeviceId, String instr, boolean all);

    public native int setFluidsynthReverbActive(int global_channel, int usbDeviceId, String instr, boolean all);

    public native void fluidsynthListSetVelocity(int global_channel, int usbDeviceId, String instr, int intVeloc);

    public native void fluidsynthListSetTranspo(int global_channel, int usbDeviceId, String instr, int intTranspo);


    public native void fluidsynthListsetFixedVel(int channel, int iUsbId, String instr,boolean fixed);

    public native void fluidsynthListSetVelocity_For_All(int global_channel, int usbDeviceId, int intVelocity);

    public native void Fluidsynth_Synth_List_ChorusDepth(int global_channel, int usbDeviceId,
                                                         String instr,float settingValue, boolean all);

    public native void Fluidsynth_Synth_List_ChorusLevel(int global_channel, int usbDeviceId,
                                                         String instr,float settingValue, boolean all);

    public native void Fluidsynth_Synth_List_ChorusNr(int global_channel, int usbDeviceId,
                                                      String instr,int settingValue, boolean all);

    public native void Fluidsynth_Synth_List_ChorusSpeed(int global_channel, int usbDeviceId,
                                                         String instr,float settingValue, boolean all);

    public native void Fluidsynth_Synth_List_ReverbDamp(int global_channel, int usbDeviceId,
                                                        String instr,float settingValue, boolean all);

    public native void Fluidsynth_Synth_List_ReverbLevel(int global_channel, int usbDeviceId,
                                                         String instr,float settingValue, boolean all);

    public native int Fluidsynth_Synth_List_ReverbRoomsize(int global_channel, int usbDeviceId,
														   String instr,float settingValue, boolean all);

    public native void Fluidsynth_Synth_List_ReverbWidth(int global_channel, int usbDeviceId,
          String instr,float settingValue, boolean all);
	*/
	
	//public native int getFluidsynthLocalSplitNote(int global_channel,int usbDeviceId,String instr);

    private boolean boolSplitNotes;

    private boolean boolMuteNotesSmalerThan;

    private boolean boolMuteNotesGreaterThan;

    private boolean fluidsynthChorusActive;

    private boolean fluidsynthReverbActive;

    private int intVeloc;

    private int  intTranspo;

    private boolean boolfixedVel;
	
    private int intVelocity;

    private float chorusDepth;
	
    private float chorusLevel;
	
    private int chorusNr;
	
    private float chorusSpeed;
	
    private float reverbDamp;
	
    private float reverbLevel;
	
    private float reverbRoomsize;
	
    private float reverbWidth;
	
	
	public EffectContainer(){




	}

	public void setBoolSplitNotes(boolean boolSplitNotes)
	{
		this.boolSplitNotes = boolSplitNotes;
	}

	public boolean isBoolSplitNotes()
	{
		return boolSplitNotes;
	}

	public void setBoolMuteNotesSmalerThan(boolean boolMuteNotesSmalerThan)
	{
		this.boolMuteNotesSmalerThan = boolMuteNotesSmalerThan;
	}

	public boolean isBoolMuteNotesSmalerThan()
	{
		return boolMuteNotesSmalerThan;
	}

	public void setBoolMuteNotesGreaterThan(boolean boolMuteNotesGreaterThan)
	{
		this.boolMuteNotesGreaterThan = boolMuteNotesGreaterThan;
	}

	public boolean isBoolMuteNotesGreaterThan()
	{
		return boolMuteNotesGreaterThan;
	}

	public void setFluidsynthChorusActive(boolean fluidsynthChorusActive)
	{
		this.fluidsynthChorusActive = fluidsynthChorusActive;
	}

	public boolean isFluidsynthChorusActive()
	{
		return fluidsynthChorusActive;
	}

	public void setFluidsynthReverbActive(boolean fluidsynthReverbActive)
	{
		this.fluidsynthReverbActive = fluidsynthReverbActive;
	}

	public boolean isFluidsynthReverbActive()
	{
		return fluidsynthReverbActive;
	}

	public void setIntVeloc(int intVeloc)
	{
		this.intVeloc = intVeloc;
	}

	public int getIntVeloc()
	{
		return intVeloc;
	}

	public void setIntTranspo(int intTranspo)
	{
		this.intTranspo = intTranspo;
	}

	public int getIntTranspo()
	{
		return intTranspo;
	}

	public void setBoolfixedVel(boolean boolfixedVel)
	{
		this.boolfixedVel = boolfixedVel;
	}

	public boolean isBoolfixedVel()
	{
		return boolfixedVel;
	}

	public void setIntVelocity(int intVelocity)
	{
		this.intVelocity = intVelocity;
	}

	public int getIntVelocity()
	{
		return intVelocity;
	}

	public void setChorusDepth(float chorusDepth)
	{
		this.chorusDepth = chorusDepth;
	}

	public float getChorusDepth()
	{
		return chorusDepth;
	}

	public void setChorusLevel(float chorusLevel)
	{
		this.chorusLevel = chorusLevel;
	}

	public float getChorusLevel()
	{
		return chorusLevel;
	}

	public void setChorusNr(int chorusNr)
	{
		this.chorusNr = chorusNr;
	}

	public int getChorusNr()
	{
		return chorusNr;
	}

	public void setChorusSpeed(float chorusSpeed)
	{
		this.chorusSpeed = chorusSpeed;
	}

	public float getChorusSpeed()
	{
		return chorusSpeed;
	}

	public void setReverbDamp(float reverbDamp)
	{
		this.reverbDamp = reverbDamp;
	}

	public float getReverbDamp()
	{
		return reverbDamp;
	}

	public void setReverbLevel(float reverbLevel)
	{
		this.reverbLevel = reverbLevel;
	}

	public float getReverbLevel()
	{
		return reverbLevel;
	}

	public void setReverbRoomsize(float reverbRoomsize)
	{
		this.reverbRoomsize = reverbRoomsize;
	}

	public float getReverbRoomsize()
	{
		return reverbRoomsize;
	}

	public void setReverbWidth(float reverbWidth)
	{
		this.reverbWidth = reverbWidth;
	}

	public float getReverbWidth()
	{
		return reverbWidth;
	}
}
