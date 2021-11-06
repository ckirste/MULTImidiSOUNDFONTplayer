package jp.kshoji.driver.midi.sample;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff.Mode;
import android.hardware.usb.UsbDevice;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jp.kshoji.driver.midi.activity.AbstractMultipleMidiActivity;
import jp.kshoji.driver.midi.device.MidiInputDevice;
import jp.kshoji.driver.midi.device.MidiOutputDevice;
import jp.kshoji.driver.midi.sample.util.*;


/**
 * Sample Activity for MIDI Driver library
 * 
 * @author C.Kirste/K.Shoji
 */
public class MIDIDriverMultipleSampleActivity extends AbstractMultipleMidiActivity implements View.OnClickListener{
	static {
		System.loadLibrary("native-lib");
	}

	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method


		switch(p1.getId()){

			case(R.id.btnTrans0):

				//testToast("transpose:0");
				intTranspose=0;

				txtvTranspose.setText(""+intTranspose);
				break;
			case(R.id.btnTransMin1):

				intTranspose=intTranspose-1;
				txtvTranspose.setText(""+intTranspose);

				break;
			case(R.id.btnTransPlus1):

				intTranspose= intTranspose+1;
				txtvTranspose.setText(""+intTranspose);

				break;

			case(R.id.btnTransMin12):

				intTranspose= intTranspose-12;
				txtvTranspose.setText(""+intTranspose);

				break;

			case(R.id.btnTransPlus12):

				intTranspose=intTranspose+12;
				txtvTranspose.setText(""+intTranspose);
				break;

			default:

				intTranspose=0;


		}


	}

	private static final int PERMISSION_REQUEST_CODE_EXT_STORAGE_ = 724;
	private File myDirSettings;

	private Button BtnOpenChooseInstrumentActivity;
	private File myDirSoundfonts;

	private int CHOOSE_INSTRUMENT_ACTIVITY_CODE =1;

	private ArrayList<String> resultInstrumentList;

	private Button btnTransposePlus12;
	private Button btnTransposeMinus12;
	private Button btnTransposePlus1;
	private Button btnTransposeMinus1;
	private Button btnTranspose0;
	private TextView txtvTranspose;
	private int intTranspose=0;

	//change Popupwindow to another activty
    private /*final */PopupWindow popupWindow;
	private int global_channel = 0;
    private int k=0;//wird für zuordnung aus der instrum.-liste benötigt
	private int usbDeviceId=0;
	private String usbDeviceName = "Display Keyboard";//"kein Usbdevice";
	private int usbCount=0;
	private boolean isClicked = false;

	private boolean bool_multi_mode = true;

	ImageButton imgbtnDeleteInstrumentBtn;
	Spinner spinnVelocity;
	private LinearLayout layContainerUsbDevices;
	private LinearLayout layAddUsbDev;
	private ScrollView scrvIns;
	private UsbDeviceButton btnDeviceName;
	private LinearLayout layContainerSelIns;
	private ViewGroup layout;

	//ImageButton imgBtnCloseApp;

	/* User interface
	final Handler midiInputEventHandler = new Handler(new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (midiInputEventAdapter != null) {
				midiInputEventAdapter.add((String)msg.obj);
			}
			// message handled successfully
			return true;
		}
	});
	
	final Handler midiOutputEventHandler = new Handler(new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (midiOutputEventAdapter != null) {
				midiOutputEventAdapter.add((String)msg.obj);
			}
			// message handled successfully
			return true;
		}
	});


	 */
	ArrayList<SF2Preset>listeSF2Presets=new ArrayList<>();

	ArrayList<String>listeUsbDeviceNames=new ArrayList<>();
	ArrayList<Integer>listeUsbDeviceIds=new ArrayList<>();

	ArrayAdapter<String>adaptListeUsbDeviceNames;

	ArrayAdapter<String>listeSF2PresetsAdapter;

	ArrayAdapter<String> listedInstrumentsAdapter;
	ArrayList<String> listeInstrumenteNames=new ArrayList<String>();
	ArrayAdapter<String> midiInputEventAdapter;
	ArrayAdapter<String> midiOutputEventAdapter;

	private ToggleButton thruToggleButton;
	Spinner cableIdSpinner;
	Spinner deviceSpinner;
	Spinner presetSpinner;
	Spinner usbDevicesSpinner;

	SeekBar seekbarRoomSize;
	TextView txtRoomsize;



	ArrayAdapter<UsbDevice> connectedDevicesAdapter;


    String tempSoundfontPath;



	int []arrProgram = {48,52,40,60,61,73,46,28,14};
	int iProgr = 0;
    private InstrumentButton tempBtnInstr;
	private float floatRoomsize;


	/**
	 * Choose device from spinner
	 * 
	 * @return the MidiOutputDevice from spinner
	 */
	@Nullable MidiOutputDevice getMidiOutputDeviceFromSpinner() {
		if (deviceSpinner != null && deviceSpinner.getSelectedItemPosition() >= 0 && connectedDevicesAdapter != null && !connectedDevicesAdapter.isEmpty()) {
			UsbDevice device = connectedDevicesAdapter.getItem(deviceSpinner.getSelectedItemPosition());
			if (device != null) {
				Set<MidiOutputDevice> midiOutputDevices = getMidiOutputDevices();
				
				if (midiOutputDevices.size() > 0) {
					// returns the first one.
					return (MidiOutputDevice) midiOutputDevices.toArray()[0];
				}
			}
		}
		return null;
	}


	private void startIntent()
	{
		Intent intent = new Intent(MIDIDriverMultipleSampleActivity.this, InstrumentChooseActivity.class);


		startActivityForResult(intent,CHOOSE_INSTRUMENT_ACTIVITY_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		//super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == CHOOSE_INSTRUMENT_ACTIVITY_CODE) {
			if(resultCode == this.RESULT_OK){
				resultInstrumentList=data.getStringArrayListExtra("selectedInstrumentList");

				for(String i:resultInstrumentList){


					addLayoutForInstruments(i);

					//testToast("sf2-file: "+i);


				}


				mergeInstrumentListForSave();
				LinearLayout tempLay = (LinearLayout)findViewById(usbDeviceId);
				setSingleModeLayout(tempLay);



			}
			if (resultCode == this.RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}

	private void mergeInstrumentListForSave(){

		LinearLayout view = (LinearLayout) findViewById(usbDeviceId);
		String instr = "";
		int countInstr = view.getChildCount();
		for(int i=0;i<countInstr;i++){

			InstrumentButton ib = (InstrumentButton) view.getChildAt(i);
			instr = instr+ib.getText().toString()+"\n";
			//instr += instr + "\n";

		}

		try
		{
			saveSelectedInstruments(""+usbDeviceId,instr);
		}
		catch (IOException e)
		{}


	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

		switch (requestCode) {
			case PERMISSION_REQUEST_CODE_EXT_STORAGE_: {
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// permission granted
					checkPermission();
				} else {
					// permission denied
					testToast("Permission denied, no storage access!");
				}
			}
		}

	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_test);//neu);

		btnTranspose0= (Button) findViewById(R.id.btnTrans0);
		btnTranspose0.setOnClickListener(this);

		btnTransposeMinus1= (Button) findViewById(R.id.btnTransMin1);
		btnTransposeMinus1.setOnClickListener(this);

		btnTransposePlus1= (Button) findViewById(R.id.btnTransPlus1);
		btnTransposePlus1.setOnClickListener(this);


		btnTransposeMinus12= (Button) findViewById(R.id.btnTransMin12);
		btnTransposeMinus12.setOnClickListener(this);

		btnTransposePlus12= (Button) findViewById(R.id.btnTransPlus12);
		btnTransposePlus12.setOnClickListener(this);

		txtvTranspose= (TextView) findViewById(R.id.txtviewTranspose);

		layContainerUsbDevices = (LinearLayout) findViewById(R.id.layoutForUsbDevices);

		checkPermission();
		try
		{
			createDir();
		}
		catch (IOException e)
		{}

		//imgBtnCloseApp = (ImageButton) findViewById(R.id.imgBtnCloseApp);


		if(usbCount>0){

			//do nothing

		}else{

			addLayForUsbdevices(usbDeviceId);

		}


		try {
			tempSoundfontPath = copyAssetToTmpFile("GeneralUser.sf2");//"sndfnt.sf2");
			fluidsynthHelloWorld(tempSoundfontPath);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}


		//imgbtnDeleteInstrumentBtn = (ImageButton) findViewById(R.id.imageButtonDeleteInstr);
		spinnVelocity = (Spinner) findViewById(R.id.spinnerVel);

		thruToggleButton = (ToggleButton) findViewById(R.id.toggleButtonThru);
		cableIdSpinner = (Spinner) findViewById(R.id.cableIdSpinner);
		deviceSpinner = (Spinner) findViewById(R.id.deviceNameSpinner);
		
		connectedDevicesAdapter = new ArrayAdapter<UsbDevice>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, new ArrayList<UsbDevice>());
		deviceSpinner.setAdapter(connectedDevicesAdapter);


		final int channel =global_channel;//0; //TODO: get from spinner


		txtRoomsize = (TextView) findViewById(R.id.txtRoomsize);

		seekbarRoomSize = (SeekBar) findViewById(R.id.mainSeekBarHallEffect);
		//seekbarHallEffect.setMin(0);
		//seekbarHallEffect.setMax(10000);
		//seekbarHallEffect.setMinimumWidth(5000);


		seekbarRoomSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){


			@Override
			public void onProgressChanged(SeekBar p1, int p2, boolean p3)
			{


				floatRoomsize = (float) ((float)p2/100.0);

				txtRoomsize.setText(""+floatRoomsize);

				//floatHallEffect = p2;
			}

			@Override
			public void onStartTrackingTouch(SeekBar p1)
			{
				// TODO: Implement this method
			}

			@Override
			public void onStopTrackingTouch(SeekBar p1)
			{
				// TODO: Implement this method

				//testToast("" + floatRoomsize);
				//usbSynth0.setEffects(floatHallEffect);

				txtRoomsize.setText(""+floatRoomsize);

				Fluidsynth_Synth_List_RoomsizeChange(floatRoomsize);


			}





		});


		OnTouchListener onToneButtonTouchListener = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {


				int note = 60 + Integer.parseInt((String) v.getTag())+intTranspose;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					//fluidsynthSendNoteOnMessage(0,note,127);
					fluidsynth_ListSendNoteOnMessage(global_channel,note,127,usbDeviceId);//TODO:channel
					break;
				case MotionEvent.ACTION_UP:

					//fluidsynthSendNoteOffMessage(0,note);
					fluidsynth_ListSendNoteOffMessage(global_channel,note,usbDeviceId);//TODO: channel

					break;
				default:
					// do nothing.
					break;
				}
				return false;
			}
		};
		findViewById(R.id.buttonC).setOnTouchListener(onToneButtonTouchListener);
		findViewById(R.id.buttonCis).setOnTouchListener(onToneButtonTouchListener);
		findViewById(R.id.buttonD).setOnTouchListener(onToneButtonTouchListener);
		findViewById(R.id.buttonDis).setOnTouchListener(onToneButtonTouchListener);
		findViewById(R.id.buttonE).setOnTouchListener(onToneButtonTouchListener);
		findViewById(R.id.buttonF).setOnTouchListener(onToneButtonTouchListener);
		findViewById(R.id.buttonFis).setOnTouchListener(onToneButtonTouchListener);
		findViewById(R.id.buttonG).setOnTouchListener(onToneButtonTouchListener);
		findViewById(R.id.buttonGis).setOnTouchListener(onToneButtonTouchListener);
		findViewById(R.id.buttonA).setOnTouchListener(onToneButtonTouchListener);
		findViewById(R.id.buttonAis).setOnTouchListener(onToneButtonTouchListener);
		findViewById(R.id.buttonB).setOnTouchListener(onToneButtonTouchListener);
		findViewById(R.id.buttonC2).setOnTouchListener(onToneButtonTouchListener);

		int whiteKeyColor = 0xFFFFFFFF;
		int blackKeyColor = 0xFF808080;
		findViewById(R.id.buttonC).getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonCis).getBackground().setColorFilter(blackKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonD).getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonDis).getBackground().setColorFilter(blackKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonE).getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonF).getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonFis).getBackground().setColorFilter(blackKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonG).getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonGis).getBackground().setColorFilter(blackKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonA).getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonAis).getBackground().setColorFilter(blackKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonB).getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);
		findViewById(R.id.buttonC2).getBackground().setColorFilter(whiteKeyColor, Mode.MULTIPLY);

		/*
		findViewById(R.id.close_app).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				fluidsynthDeleteSynth();
				deleteFluidsynth_Synth_List();
				finish();
			}
		});

		 */


		//load instruments from Soundfontfile to listeInstrumenteNames ######################################################
		fluidsynthGetPresetName();

		/*imgBtnCloseApp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				fluidsynthDeleteSynth();

				deleteFluidsynth_Synth_List();

				testToast("Close App! Bye!");

				finish();

			}
		});*/





}

	/*@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{

		//boolean getListUsblatency=false;

		super.onWindowFocusChanged(hasFocus);
		if (hasFocus)
		{
			//
			FakeKeyGenerator.getInstance().start();


		}
		else
		{
			// Stop the background tasks.
			//mLatencyHandler.removeCallbacks(mLatencyRunnable);
			FakeKeyGenerator.getInstance().stop();
		}
	}*/

	@Override
	public void onBackPressed() {

		super.onBackPressed();

		testToast("Bye backed pressed");

		fluidsynthDeleteSynth();

		deleteFluidsynth_Synth_List();

		//testToast("Bye backed pressed");

		//finish();

	}
	protected void onStop() {

        super.onStop();
        testToast("on stop finished");

	    fluidsynthDeleteSynth();

		deleteFluidsynth_Synth_List();

		testToast("on stop finished");

		finish();


	}

	@Override
	protected void onDestroy() {

        super.onDestroy();
        testToast("on destroy finished");

	    fluidsynthDeleteSynth();

		deleteFluidsynth_Synth_List();

		testToast("on destroy finished");

		finish();


	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	private String copyAssetToTmpFile(String fileName) throws IOException {
		try (InputStream is = getAssets().open(fileName)) {
			String tempFileName = "tmp_" + fileName;
			try (FileOutputStream fos = openFileOutput(tempFileName, Context.MODE_PRIVATE)) {
				int bytes_read;
				byte[] buffer = new byte[4096];
				while ((bytes_read = is.read(buffer)) != -1) {
					fos.write(buffer, 0, bytes_read);
				}
			}
			return getFilesDir() + "/" + tempFileName;
		}
	}

	private void hideDisplayPiano(){


		LinearLayout llDisplayPiano = (LinearLayout) findViewById(R.id.layButtTast);
		llDisplayPiano.setVisibility(LinearLayout.GONE);


	}




	private void checkPermission(){

		// Check if we have WRITE_EXTERNAL_STORAGE permission
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
					PERMISSION_REQUEST_CODE_EXT_STORAGE_);
			return;
		}


	}


	private void createDir() throws IOException{



		myDirSoundfonts = new File(Environment.getExternalStorageDirectory(),"MULTImidiSOUNDFONTplayer/SOUNDFONTS");//create directory and subfolder
		myDirSettings = new File(Environment.getExternalStorageDirectory() + File.separator + "MULTImidiSOUNDFONTplayer","Settings");



		if(!myDirSoundfonts.exists()){

			myDirSoundfonts.mkdirs();
			myDirSettings.mkdirs();


		}else{


			//testToast("Ordner existieren bereits!");

		}




	}

	private void saveSelectedInstruments(String usbDeviceIdentifier, String instruments) throws IOException
	{
		File file = new File(myDirSettings, usbDeviceIdentifier);


		FileOutputStream stream = new FileOutputStream(file);

		stream.write(instruments.getBytes());

		//stream.write("Instrument2".getBytes());




		stream.close();
	}

	//#################   native funktions  ################################################################################################################

    public native void fluidsynthHelloWorld(String soundfontPath);

    public native void fluidsynthGetPresetName(); //TODO: get all preset names of loaded Soundfontfile

    public native int startFluidsynth_Add_Synth_to_List(String soundfontPath, int usbID,int channel,int bank, int progr);

    public native void fluidsynth_ListSendNoteOnMessage(int channel,int note, int velocity, int iUsbId);

    public native void fluidsynth_ListSendNoteOffMessage(int channel,int note, int iUsbId);

    public native void fluidsynth_ListSetInstrumentMuteOnOff(int channel, int iUsbId, String strPresetname);

    public native void fluidsynth_ListDeleteInstrument(int channel, int iUsbId, String strPresetname);

    public native int fluidsynth_ListDeleteInstrumentFinal(int channel, int iUsbId, String strPresetname);


    public native void Fluidsynth_Synth_List_ProgramChange(int channel, int bank, int progr);

    public native void Fluidsynth_Synth_List_RoomsizeChange(float roomsize);


	public native void fluidsynthListSetVelocity(int global_channel, int tempUsbId, String tempPresetName, int intVeloc);


	public native int startFluidsynth_Add_Synth(String soundfontPath, int usbID);//ALT nicht verwenden/don't use###############################

	//jstring jSoundfontPath, jint usbId, jint channel, jint bank, jint progr

	public native int deleteFluidsynth_Synth_List();

	public native void fluidsynthSendNoteOnMessage(int channel,int note,int velocity);

	public native void fluidsynthSendNoteOffMessage(int channel,int note);

	public native void fluidsynthProgrammChange(int programm);

	public native void fluidsynthDeleteSynth();

	public native int fluidsynthgetMore();


	public void fluidsynthListProgramChance(int channel, int bank, int progr){


		Fluidsynth_Synth_List_ProgramChange(channel,bank,progr);

	}

	public void getPresetnamesFromList(){

		for(SF2Preset sf2presetName: listeSF2Presets){

			listeInstrumenteNames.add(sf2presetName.getPresetname());

		}

		//setInstrmentspinner();

	}
	public void setListeInstrumenteObjects(String presetname,int ibank, int iprogr){



		listeSF2Presets.add(new SF2Preset(presetname,ibank,iprogr));




	}


	//###############  add Layout #####################################################################################################
	private void addLayForUsbdevices(int tempUsbDeviceId)
	{
		layAddUsbDev = new LinearLayout(this);
		layAddUsbDev.setOrientation(LinearLayout.VERTICAL);
		//layAddUsbDev.setBackgroundResource(R.drawable.customborder);
		scrvIns = new ScrollView(this);
		layContainerSelIns = new LinearLayout(this);
		layContainerSelIns.setOrientation(LinearLayout.VERTICAL);
		layContainerSelIns.setId(tempUsbDeviceId);




		scrvIns.addView(layContainerSelIns);
		btnDeviceName = new UsbDeviceButton(this);
		btnDeviceName.setText(usbDeviceName);
		btnDeviceName.setUsbDeviceId(tempUsbDeviceId);
		btnDeviceName.setBackgroundResource(R.drawable.rectbtnusbdev); //getBackground().setColorFilter(Color.BLACK,Mode.MULTIPLY);
		btnDeviceName.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View p1)
			{


				//@SuppressLint("ResourceType") int layId = p1.getId()-100;

				testToast("Multi-Mode");
				bool_multi_mode=true;
				//Button tempBtn = (UsbDeviceButtonButton)p1;
				int layId = btnDeviceName.getUsbDeviceId();//tempBtn;
				usbDeviceId=layId;

				LinearLayout tempLay = (LinearLayout)findViewById(layId);
				tempLay.setTag("Multi-Mode");
				startIntent();

				//setInstrumentOnPopUpWindow(tempLay);//layContainerSelIns); //p1);
				// TODO: Implement this method
			}




		});

		btnDeviceName.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {

				testToast("Single-Mode");
				bool_multi_mode=false;
				//Button tempBtn = (Button)view;
				int layId = btnDeviceName.getUsbDeviceId();//(tempBtn.getId())-100;

				usbDeviceId=layId;
				LinearLayout tempLay = (LinearLayout)findViewById(layId);
				tempLay.setTag("Single-Mode");
				setInstrumentOnPopUpWindow(tempLay);
				//setSingleModeLayout(tempLay);

				// TODO: Implement this method

				return true;
			}
		});

		//btnDeviceName.setTextColor(Color.BLACK);


		layAddUsbDev.addView(btnDeviceName);
		layAddUsbDev.addView(scrvIns);


		layContainerUsbDevices.addView(layAddUsbDev);
	}

	private void setSingleModeLayout(LinearLayout tempLay) {

		layout = tempLay;
		for (int i = 0; i < layout.getChildCount(); i++) {
			View child = layout.getChildAt(i);
			if (child instanceof InstrumentButton) {


				tempBtnInstr =(InstrumentButton)child;
				setInstrumentMuteOn();

			}
		}
		tempBtnInstr = (InstrumentButton) layout.getChildAt(iProgr);
		setInstrumentMuteOff();

		addInstrumentToFluidsynthList(tempBtnInstr.getText().toString(),tempBtnInstr.getUsbDeviceId());

		//instrModeSetNextInstr(0);



		
	}

    private void instrModeSetNextInstr(int usbDeviceId){

        String getItemInInstrList;
        int bank;
        int progr;
        ViewGroup usbdeviceLay = (ViewGroup) findViewById(usbDeviceId);
        String instrMode = (String) usbdeviceLay.getTag();
        int countOfBtnInstr = usbdeviceLay.getChildCount();
		View child;


        	if (iProgr==countOfBtnInstr-1){
        		//TODO: verbessern
				testToast("iProgr"+iProgr+"cobi"+countOfBtnInstr);

				tempBtnInstr = (InstrumentButton) usbdeviceLay.getChildAt(countOfBtnInstr-1);
				setInstrumentMuteOn();
                iProgr=0;
                tempBtnInstr = (InstrumentButton) usbdeviceLay.getChildAt(iProgr);
                setInstrumentMuteOff();
                for (SF2Preset sf2p : listeSF2Presets) {

                    getItemInInstrList = sf2p.getPresetname();
                    if (getItemInInstrList == tempBtnInstr.getText().toString()) {

                        progr = sf2p.getPresetprogr();
                        bank = sf2p.getPresetbank();

                        Fluidsynth_Synth_List_ProgramChange(global_channel, bank, progr);


                    }
                }


			}
        	child = usbdeviceLay.getChildAt(iProgr);
            if (child instanceof InstrumentButton) {

                tempBtnInstr =(InstrumentButton)child;

                if(tempBtnInstr.isBoolVolumeOn() && instrMode == "Single-Mode"){


					//tempBtnInstr = (InstrumentButton) usbdeviceLay.getChildAt(i);
					setInstrumentMuteOn();
					iProgr++;


					tempBtnInstr = (InstrumentButton) usbdeviceLay.getChildAt(iProgr);
					setInstrumentMuteOff();
					for (SF2Preset sf2p : listeSF2Presets) {

						getItemInInstrList = sf2p.getPresetname();
						if (getItemInInstrList == tempBtnInstr.getText().toString()) {

							progr = sf2p.getPresetprogr();
							bank = sf2p.getPresetbank();

							Fluidsynth_Synth_List_ProgramChange(global_channel, bank, progr);


						}
					}


                }else if(tempBtnInstr.isBoolVolumeOn() && instrMode == "Multi-Mode"){

                    iProgr++;

                    tempBtnInstr = (InstrumentButton) usbdeviceLay.getChildAt(iProgr);
                    setInstrumentMuteOff();

					for (SF2Preset sf2p:listeSF2Presets) {

						getItemInInstrList = sf2p.getPresetname();
						if (getItemInInstrList==tempBtnInstr.getText().toString()){

							//progr = sf2p.getPresetprogr();
							//bank = sf2p.getPresetbank();

							addInstrumentToFluidsynthList(getItemInInstrList,usbDeviceId);

						}
					}

				}
            }



    }
	private void setInstrumentOnPopUpWindow(final LinearLayout view)
	{

		popupWindow = new PopupWindow(getApplicationContext());


		listedInstrumentsAdapter = new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_list_item_multiple_choice, listeInstrumenteNames);
		//listedInstrumentsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listeInstrumenteNames);

		final ListView listViewInstr = new ListView(getApplicationContext());
		listViewInstr.setAdapter(listedInstrumentsAdapter);

		listViewInstr.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		//listViewInstr.setItemChecked(2, true);

		listViewInstr.setBackgroundResource(R.drawable.customborder);

		listViewInstr.setOnItemClickListener(new AdapterView.OnItemClickListener(){



			@TargetApi(Build.VERSION_CODES.LOLLIPOP)
			@Override
			public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
			{


				int getUsbId = view.getId();

				int progr = listeSF2Presets.get(p3).getPresetprogr();
				int bank = listeSF2Presets.get(p3).getPresetbank();
				String getselectedItemInstr = listeSF2Presets.get(p3).getPresetname();


				//TODO: nur ausführen wenn wirklich alle gleichzeitig geladen werden sollen
				/*if (bool_multi_mode) {
					int start = startFluidsynth_Add_Synth_to_List(tempSoundfontPath, getUsbId, global_channel, bank, progr);

					if (start > 0) {

						//if add succeed
						testToast(getUsbId + ": " + getselectedItemInstr + " erfolgreich geladen!");

					} else {

						testToast("ERROR from native-code: startFluidsynth_Add_Synth_to_List");

					}
				}*/



				k = p3;

				UsbDevice usbD = (UsbDevice) deviceSpinner.getSelectedItem();

				String midiDeviceName = "";

				int usbDId = 0;

				if (usbD != null)
				{

					usbDId = getUsbId;

					//addLayoutForInstruments(getselectedItemInstr, k, midiDeviceName, usbDId,view);


				}

				else
				{

					midiDeviceName = "Display Keyboard";
					//addLayoutForInstruments(getselectedItemInstr, k, midiDeviceName, usbDId,view);

				}


			}
		});




		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

				setSingleModeLayout(view);
				//TODO: evtl wieder ändern
				/*if(!bool_multi_mode){

					setSingleModeLayout(view);

				}*/
            }
        });

		popupWindow.setFocusable(true);
		popupWindow.setWidth(800);//WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindow.setHeight(800);//WindowManager.LayoutParams.WRAP_CONTENT);

		popupWindow.setContentView(listViewInstr);
		//popupWindow.showAsDropDown(view, 0, 0);
		popupWindow.showAtLocation((View)findViewById(R.id.layoutForUsbDevices),0,0,0);
		//popupWindow.showAtLocation((View)(findViewById(R.id.layAppContent)),0,0,0);


	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void addLayoutForInstruments(String strPresetName)//int indexI, String deviceName, int deviceId, LinearLayout layInsTemp)
	{

		InstrumentButton btnViewInstrument = new InstrumentButton(this);
		btnViewInstrument.setBackgroundResource(R.drawable.rectbtnselins);
		btnViewInstrument.setRotation(0.0f);
		//btnViewInstrument.setId(indexI + 100);
		//btnViewInstrument.setTag(layInsTemp.getId());
		btnViewInstrument.setUsbDeviceId(usbDeviceId);//layInsTemp.getId());

		btnViewInstrument.setBoolVolumeOn(true);

		btnViewInstrument.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View p1)
			{

			    tempBtnInstr = (InstrumentButton)p1;

                InstrumentButton btnIns =(InstrumentButton)p1;
                String getInstrOfList = (String) btnIns.getText();
                int getSelUsbId = btnIns.getUsbDeviceId();// getTag();

				if (btnIns.isBoolVolumeOn()==false){

					//add new instr on fluidsynthfor mute off
					//get patch an bank from list
                    setInstrumentMuteOff();
					addInstrumentToFluidsynthList(getInstrOfList,getSelUsbId);

				}else {

					//testToast("delete:"+getSelUsbId);
					fluidsynth_ListDeleteInstrument(global_channel, getSelUsbId, getInstrOfList);

				}

                //fluidsynth_ListSetInstrumentMuteOnOff(global_channel,getSelUsbId,getInstrOfList);



			}
		});




		btnViewInstrument.setOnLongClickListener(new View.OnLongClickListener(){

			@Override
			public boolean onLongClick(View p1)
			{


				showOptions(p1);
				//showInstrumentButtonOptions(p1);

				return true;
			}

		});

		btnViewInstrument.setText(strPresetName);

		//findViewById(R.id.seekbarlayout).setVisibility(LinearLayout.GONE);
		findViewById(R.id.laydropdownDevices).setVisibility(LinearLayout.GONE);
		findViewById(R.id.layOpenSf).setVisibility(LinearLayout.GONE);

		LinearLayout layInsTemp = (LinearLayout) findViewById(usbDeviceId);
		layInsTemp.addView(btnViewInstrument);

		
	}

	private void addInstrumentToFluidsynthList(String getInstrOfList,int usbId) {

		String getItemInInstrList;
		int iprogr=0;
		int ibank=0;
		String getPresetName ="";

		for (SF2Preset sf2p:listeSF2Presets) {

			getItemInInstrList = sf2p.getPresetname();
			if (getItemInInstrList==getInstrOfList){

				iprogr = sf2p.getPresetprogr();
				ibank = sf2p.getPresetbank();
				getPresetName = sf2p.getPresetname();

			}
		}

		//native Fluidsynth-Instrument-Object anlegen mit progr und bank und usbid
		int start = startFluidsynth_Add_Synth_to_List(tempSoundfontPath, usbId, global_channel, ibank, iprogr);

		if (start > 0) {

			//if add succeed
			testToast(getPresetName+" geladen!");

		} else {

			testToast("ERROR from native-code: startFluidsynth_Add_Synth_to_List");

		}


	}


	private void showOptions(final View instrumentV){


		final InstrumentButton temp_instr_btn = (InstrumentButton)instrumentV;
		//testToast("show options!!!!!");
		final PopupWindow popupWindowOptions = new PopupWindow(getApplicationContext());

		final LinearLayout tempLL = new LinearLayout(this);
		tempLL.setOrientation(LinearLayout.VERTICAL);

		final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
				(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
						android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);

		ImageButton temp_img_btn_del = new ImageButton(this);
		temp_img_btn_del.setImageResource(R.drawable.ic_menu_delete);
		temp_img_btn_del.setLayoutParams(params);
		temp_img_btn_del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String deleteInstr = (String) temp_instr_btn.getText();
				//testToast(deleteInstr+" was deleted!");
				LinearLayout llTemp = (LinearLayout) findViewById(temp_instr_btn.getUsbDeviceId());
				popupWindowOptions.dismiss();
				llTemp.removeView(temp_instr_btn);
				int deleted = fluidsynth_ListDeleteInstrumentFinal(global_channel,temp_instr_btn.getUsbDeviceId(), (String) temp_instr_btn.getText());

				if(deleted==1) {

					testToast(deleteInstr+" was deleted!");


				}else{

					testToast("Error from nativelib!");

				}


			}
		});

		TextView temp_txtV_Vel = new TextView(this);
		temp_txtV_Vel.setText("Velocity:");
		temp_txtV_Vel.setLayoutParams(params);

		final Spinner spinnVeloc = new Spinner(this);

		List<String> list = new ArrayList<String>();

		list.add("127");
		list.add("auto");
		list.add("50");
		list.add("100");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);// simple_spinner_dropdown_item);
		spinnVeloc.setAdapter(dataAdapter);
		spinnVeloc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


				spinnVeloc.setLayoutParams(params);
				adapterView.setSelection(i);
				String instrName = (String)temp_instr_btn.getText();

				int intVeloc = 127;
				String veloc = adapterView.getItemAtPosition(i).toString();
				if(veloc=="auto"){

					//do nothing
                    fluidsynthListSetVelocity(global_channel,
                            temp_instr_btn.getUsbDeviceId(),
                            instrName, 0);
				}else{

					intVeloc = Integer.parseInt(veloc);
					fluidsynthListSetVelocity(global_channel,
							temp_instr_btn.getUsbDeviceId(),
							instrName, intVeloc);

				}
				//TODO:

			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}

		});


		tempLL.addView(temp_img_btn_del);
		tempLL.addView(temp_txtV_Vel);
		tempLL.addView(spinnVeloc);
		tempLL.setLayoutParams(params);


		//final LinearLayout llInstrumentOptions = (LinearLayout)findViewById(R.id.instrument_options);
		//final InstrumentButton temp_intr_btn = (InstrumentButton)instrumentV;

		popupWindowOptions.setFocusable(true);
		popupWindowOptions.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindowOptions.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

		int intHeigth = (-2)*instrumentV.getHeight();
		popupWindowOptions.setContentView((View)tempLL);
		popupWindowOptions.showAsDropDown(instrumentV,instrumentV.getWidth(),intHeigth);
		//popupWindow.setOutsideTouchable(true);
		//popupWindow.showAtLocation((instrumentV),0,0,0);


	}
	private void showInstrumentButtonOptions(View instrumentV){


		final PopupWindow popupWindow = new PopupWindow(getApplicationContext());
		LinearLayout llInstrumentOptions = (LinearLayout)findViewById(R.id.instrument_options);



		final InstrumentButton tempInstrButt = (InstrumentButton)instrumentV;
		final int tempUsbId = tempInstrButt.getUsbDeviceId();
		final String tempPresetName = (String) tempInstrButt.getText();
		final LinearLayout llTemp = (LinearLayout) findViewById(tempUsbId); //usbDeviceId);



		/*
		final ImageButton imgbtnDeleteInstrumentBtn;
		imgbtnDeleteInstrumentBtn = (ImageButton) findViewById(R.id.imageButtonDeleteInstr);

		imgbtnDeleteInstrumentBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				llTemp.removeView(tempInstrButt);
				if(!tempInstrButt.isBoolVolumeOn()){

					//do nothing because already deleted

				}else{

					//TODO: delete fluidsyth list object
				}

			}
		});


		 */
		//Spinner spinnVelocity = (Spinner) findViewById(R.id.spinnerVel);

		spinnVelocity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

				String strVeloc = adapterView.getItemAtPosition(i).toString();
				int intVeloc = Integer.parseInt(strVeloc);
				if(strVeloc=="auto"){
					//do nothing because default setting
				}else{

					fluidsynthListSetVelocity(global_channel,tempUsbId,tempPresetName,intVeloc);

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});



		popupWindow.setFocusable(true);
		popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

		popupWindow.setContentView(llInstrumentOptions);
		popupWindow.showAsDropDown(instrumentV, 0, 0);
		//popupWindow.showAtLocation(instrumentV, Gravity.CENTER,100,100); //(View)findViewById(R.id.layoutForUsbDevices),0,0,0);


	}


	private void setInstrumentMuteOn(){



	    tempBtnInstr.setBoolVolumeOn(false);
        tempBtnInstr.setBackgroundResource(R.drawable.rectmutebtn);

    }

    private void setInstrumentMuteOff(){

	    tempBtnInstr.setBoolVolumeOn(true);
        tempBtnInstr.setBackgroundResource(R.drawable.rectbtnselins);


    }




    /*private void setInstrumentbuttonMute(View v){

        Button btnIns =(Button)v;
        String getInstrOfList = (String) btnIns.getText();
        int getSelUsbId = (int) btnIns.getTag();

		if(isClicked==false){


			v.setBackgroundResource(R.drawable.rectmutebtn);
			isClicked=true;

            fluidsynth_ListSetInstrumentMuteOnOff(global_channel,getSelUsbId,getInstrOfList);

		}else{

			v.setBackgroundResource(R.drawable.rectbtnselins);

			isClicked=false;

            fluidsynth_ListSetInstrumentMuteOnOff(global_channel,getSelUsbId,getInstrOfList);
		}

	}*/




	public void testToast(String s)
	{

		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

	}



	private void setInstrmentspinner(){

	    listedInstrumentsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, listeInstrumenteNames);

        presetSpinner.setAdapter(listedInstrumentsAdapter);
		testToast("fertig 2");

    }


	//*******************************************************************************************************************************************************************************

    @Override
    public void onDeviceAttached(@NonNull UsbDevice usbDevice) {
        // deprecated method.
        // do nothing
    }

    @Override
    public void onMidiInputDeviceAttached(@NonNull MidiInputDevice midiInputDevice) {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
    public void onMidiOutputDeviceAttached(@NonNull final MidiOutputDevice midiOutputDevice) {
        if (connectedDevicesAdapter != null) {
            connectedDevicesAdapter.remove(midiOutputDevice.getUsbDevice());
            connectedDevicesAdapter.add(midiOutputDevice.getUsbDevice());
            connectedDevicesAdapter.notifyDataSetChanged();
            listeUsbDeviceNames.add(midiOutputDevice.getUsbDevice().getProductName());
			listeUsbDeviceIds.add(midiOutputDevice.getUsbDevice().getDeviceId());

        }
		usbCount++;


		UsbDevice usb= midiOutputDevice.getUsbDevice();
		usbDeviceName = usb.getProductName();
		usbDeviceId = usb.getDeviceId();
		addLayForUsbdevices(usbDeviceId);
		hideDisplayPiano();

        Toast.makeText(MIDIDriverMultipleSampleActivity.this, "USB MIDI Device " + midiOutputDevice.getUsbDevice().getDeviceName() + " has been attached.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeviceDetached(@NonNull UsbDevice usbDevice) {
        // deprecated method.
        // do nothing
    }

    @Override
    public void onMidiInputDeviceDetached(@NonNull MidiInputDevice midiInputDevice) {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
    public void onMidiOutputDeviceDetached(@NonNull final MidiOutputDevice midiOutputDevice) {
        if (connectedDevicesAdapter != null) {
            connectedDevicesAdapter.remove(midiOutputDevice.getUsbDevice());
            connectedDevicesAdapter.notifyDataSetChanged();
			adaptListeUsbDeviceNames.remove(midiOutputDevice.getUsbDevice().getProductName());
			adaptListeUsbDeviceNames.notifyDataSetChanged();

        }
        Toast.makeText(MIDIDriverMultipleSampleActivity.this, "USB MIDI Device " + midiOutputDevice.getUsbDevice().getDeviceName() + " has been detached.", Toast.LENGTH_LONG).show();
    }

	@Override
	public void onMidiMiscellaneousFunctionCodes(@NonNull MidiInputDevice sender, int cable, int byte1, int byte2, int byte3) {

	}

	@Override
	public void onMidiCableEvents(@NonNull MidiInputDevice sender, int cable, int byte1, int byte2, int byte3) {

	}

	@Override
	public void onMidiSystemCommonMessage(@NonNull MidiInputDevice sender, int cable, byte[] bytes) {

	}

	@Override
	public void onMidiSystemExclusive(@NonNull MidiInputDevice sender, int cable, byte[] systemExclusive) {

	}

	@Override
	public void onMidiNoteOff(@NonNull final MidiInputDevice sender, int cable, int channel, int note, int velocity) {




		//fluidsynthSendNoteOffMessage(global_channel,note);
		fluidsynth_ListSendNoteOffMessage(global_channel,note+intTranspose,sender.getUsbDevice().getDeviceId());//TODO: channel

	}


	@Override
	public void onMidiNoteOn(@NonNull final MidiInputDevice sender, int cable, int channel, int note, int velocity) {


		//fluidsynthSendNoteOnMessage(global_channel,note,velocity);
		fluidsynth_ListSendNoteOnMessage(global_channel,note+intTranspose,velocity,sender.getUsbDevice().getDeviceId());//TODO:channel

	}

	@Override
	public void onMidiPolyphonicAftertouch(@NonNull MidiInputDevice sender, int cable, int channel, int note, int pressure) {

	}


	@Override
	public void onMidiControlChange(@NonNull final MidiInputDevice sender, int cable, int channel, int function, int value) {

		/*int intProgram;




		if(iProgr==arrProgram.length){

		    iProgr=0;
        }
		intProgram = arrProgram[iProgr++];

		//Todo: change to native list
		fluidsynthProgrammChange(intProgram);*/



		//testToast("Program:"+value);
	}

	@Override
	public void onMidiProgramChange(@NonNull final MidiInputDevice sender, int cable, int channel, int program) {




        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                instrModeSetNextInstr(sender.getUsbDevice().getDeviceId());
                //testToast("programm"+iProgr);
                //iProgr++;


            }
        });



		//testToast("PROGRAMM CHANGE"/*+(sender.getUsbDevice().getDeviceId())*/);



	}

	@Override
	public void onMidiChannelAftertouch(@NonNull MidiInputDevice sender, int cable, int channel, int pressure) {

	}

	@Override
	public void onMidiPitchWheel(@NonNull MidiInputDevice sender, int cable, int channel, int amount) {

	}

	@Override
	public void onMidiSingleByte(@NonNull MidiInputDevice sender, int cable, int byte1) {

	}


}
