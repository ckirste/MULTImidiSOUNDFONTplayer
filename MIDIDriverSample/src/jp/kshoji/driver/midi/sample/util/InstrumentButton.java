package jp.kshoji.driver.midi.sample.util;



import android.content.*;
import android.util.*;
import android.widget.*;

public class InstrumentButton extends Button
{
	
	private int usbDeviceId;
	private boolean boolVolumeOn;
	public InstrumentButton(Context context){
		super(context);
		
		
	}
	
	public InstrumentButton(Context context, AttributeSet attrs) {
	super(context, attrs);
		
		
	}

	public void setBoolVolumeOn(boolean boolVolumeOn)
	{
		this.boolVolumeOn = boolVolumeOn;
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

	
	
}
