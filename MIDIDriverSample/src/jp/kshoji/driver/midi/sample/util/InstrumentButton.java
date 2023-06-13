package jp.kshoji.driver.midi.sample.util;



import android.annotation.SuppressLint;
import android.content.*;
import android.util.*;
import android.widget.*;

import java.util.ArrayList;

import jp.kshoji.driver.midi.sample.R;

@SuppressLint("AppCompatCustomView")
public class InstrumentButton extends Button
{

	static {
		System.loadLibrary("native-lib");
	}




	private EffectContainer effectContainerEC;
	private boolean isDrumButton = false;
	private String instrumentName;
	private int drumKey=35;
	private int internCounter =0;
	private int global_channel=0;
	private ArrayList<SF2Preset> listeSF2Presets=new ArrayList<>();
	private String soundfontpath;
	private int usbDeviceId;
	private boolean boolVolumeOn;
	public InstrumentButton(Context context){
		super(context);


	}

	public InstrumentButton(Context context, AttributeSet attrs) {
		super(context, attrs);


	}

	public ArrayList<SF2Preset> getListeSF2Presets() {
		return listeSF2Presets;
	}

	public void setListeSF2Presets(ArrayList<SF2Preset> listeSF2Presets) {
		this.listeSF2Presets = listeSF2Presets;
	}

	public String getSoundfontpath() {
		return soundfontpath;
	}

	public void setSoundfontpath(String soundfontpath) {
		this.soundfontpath = soundfontpath;
	}

	public String getInstrumentName() {
		return instrumentName;
	}
	public boolean isDrumButton() {
		return isDrumButton;
	}

	public void setDrumButton(boolean drumButton) {
		isDrumButton = drumButton;
	}
	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}
	public void setBoolVolumeOn(boolean boolVolumeOn)
	{


		this.boolVolumeOn = boolVolumeOn;
		if(!boolVolumeOn && internCounter>0){

			//deleteInstrumentFromFluidsynthList(global_channel,usbDeviceId,this.getText().toString());


			this.setBackgroundResource(R.drawable.rectmutebtn);
			boolVolumeOn=false;





		}else if(!boolVolumeOn && internCounter==0){

			this.setBackgroundResource(R.drawable.rectmutebtn);
			boolVolumeOn=false;


		}else if(boolVolumeOn){

			//this.setBackgroundResource(R.drawable.rectmutebtn);

			this.setBackgroundResource(R.drawable.rectbtnselins);
			boolVolumeOn=true;
			internCounter=1;
			//addInstrumentTo_FluidSynthList();

		}
	}

	public void setDrumKey(int drumKey)
	{
		this.drumKey = drumKey;
	}

	public int getDrumKey()
	{
		return drumKey;
	}

	public boolean isBoolVolumeOn()
	{
		return boolVolumeOn;
	}

	
	public void setUsbDeviceId(int usbDeviceId)
	{
		this.usbDeviceId = usbDeviceId;
	}

	public int getUsbDeviceId()
	{
		return usbDeviceId;
	}

	public EffectContainer getEffectContainerEC() {
		return effectContainerEC;
	}

	public void setEffectContainerEC(EffectContainer effectContainerEC) {
		this.effectContainerEC = effectContainerEC;
	}

	public void setGlobalChannel(int channel){

		this.global_channel = channel;

	}

	public int getchannel(){

		return this.global_channel;

	}


	public void addInstrumentTo_FluidSynthList(){

		int progr =0;
		int bank =0;
		String getInstrumentOfButtoName;
		String getItemInInstrList;

		for (SF2Preset sf2p:listeSF2Presets) {

			getItemInInstrList = sf2p.getPresetname();
			getInstrumentOfButtoName = this.getText().toString();
			if (getItemInInstrList.equalsIgnoreCase(getInstrumentOfButtoName)) {

				progr = sf2p.getPresetprogr();
				bank = sf2p.getPresetbank();


				addInstrumentToFluidsynthList(soundfontpath,usbDeviceId,global_channel,bank,progr);








			}
		}

	}

	public native void addInstrumentToFluidsynthList(String soundfontpath, int usbDeviceId, int channel,int bank, int programm);
	public native void deleteInstrumentFromFluidsynthList(int channel, int iUsbId, String strPresetname);


}
