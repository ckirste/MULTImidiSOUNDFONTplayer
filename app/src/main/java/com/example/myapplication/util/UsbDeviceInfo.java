package com.example.myapplication.util;

public class UsbDeviceInfo
{
	
	String usbdeviceName;
	int usbdeviceID;
	
	public UsbDeviceInfo(String usbdeviceName,int usbdeviceID){
		
		this.usbdeviceName=usbdeviceName;
		this.usbdeviceID=usbdeviceID;
		
	}

	public void setUsbdeviceID(int usbdeviceID)
	{
		this.usbdeviceID = usbdeviceID;
		
	}

	public int getUsbdeviceID()
	{
		return usbdeviceID;
	}

	public void setUsbdeviceName(String usbdeviceName)
	{
		this.usbdeviceName = usbdeviceName;
	}

	public String getUsbdeviceName()
	{
		return usbdeviceName;
	}
	
	
	
}
