package jp.kshoji.driver.midi.sample.util;

import android.annotation.SuppressLint;
import android.content.*;
import android.widget.*;
import java.util.*;
import android.util.*;
import jp.kshoji.driver.midi.sample.*;

@SuppressLint("AppCompatCustomView")
public class RegistrationButton extends Button
{
	
	private ArrayList<InstrumentButton> listeInstrBtn;
	private ArrayList<Boolean>listeIsVolumeOnInstr;
	private boolean boolOn=false;
	
	
	public RegistrationButton(Context c){
		
		super(c);
		
		
		//this.setWidth(100);
		//this.setHeight(100);
		
	}
	
	
	public RegistrationButton(Context c,AttributeSet ats){
		
		super(c, ats);
		
	}

	public void setListeIsVolumeOnInstr(ArrayList<Boolean> listeIsVolumeOnInstr)
	{
		this.listeIsVolumeOnInstr = listeIsVolumeOnInstr;
	}

	public ArrayList<Boolean> getListeIsVolumeOnInstr()
	{
		return listeIsVolumeOnInstr;
	}

	public boolean isBoolOn()
	{
		return boolOn;
	}

	public void setListeInstrBtn(ArrayList<InstrumentButton> listeInstrBtn)
	{
		this.listeInstrBtn = listeInstrBtn;
	}

	public ArrayList<InstrumentButton> getListeInstrBtn()
	{
		return listeInstrBtn;
	}
	
	
	
	public void setOnOff(){
		
		if(boolOn){
			
			this.setBackgroundResource(R.drawable.circlebutton);
			boolOn=false;
			
			
		}else{
			
			this.setBackgroundResource(R.drawable.circlebutton_on);
			boolOn=true;
			
		}
		
	}
	
	
	public void setOn(boolean boolOn){
		
		this.boolOn = boolOn;
		if(!this.boolOn && listeInstrBtn!=null){
			
			this.setBackgroundResource(R.drawable.circlebutton_on);
			
		}else{
			
			this.setBackgroundResource(R.drawable.circlebutton);
			
		}
		
	}
	
	
}
