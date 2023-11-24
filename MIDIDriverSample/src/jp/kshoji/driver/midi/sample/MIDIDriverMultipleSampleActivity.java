package jp.kshoji.driver.midi.sample;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff.Mode;
import android.hardware.usb.UsbDevice;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
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
public class MIDIDriverMultipleSampleActivity extends AbstractMultipleMidiActivity implements View.OnClickListener, View.OnTouchListener {
    //

    private static final String TAG = "MIDIDriverMultiple";

    static {
        System.loadLibrary("native-lib");
    }

    ArrayList<InstrumentContainer> instrumentContainerArrayList;
    boolean shareAll=false;
    String strSharedFileName;
    private String externeDateiPreFix = "#_";
    private ArrayList<String> arrlistSavedInstr = new ArrayList<>();
    private ArrayList<File> arrlistSavedInstrFiles = new ArrayList<>();
    private boolean instrumentsSaved = false;
    private String strDrumName;
    private AudioManager am;
    private int curVol;
    private TextView txtvVolume;
    private SeekBar seekVolume;
    private SeekBar seekSampleRate;
    private TextView txtvSampleRate;

    private EffectContainer effectContainerEC;
    private int counterRegBtn = 1;
    private int sampleRate=44100;
    private boolean boolLoadAltList=false;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        InstrumentButton ib = (InstrumentButton) view;
        //tempBtnInstr = ib;
        drumsId = ib.getUsbDeviceId();
        int drumVelocity = 127;
        int note = ib.getDrumKey();
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:

                ib.setBackgroundResource(R.drawable.draw_drums);

                fluidsynth_ListSendNoteOnMessage(global_channel, note, drumVelocity, drumsId);
			    /*if(loadedDrums) {




				}*/

                break;
            case MotionEvent.ACTION_UP:

                switch (ib.getUsbDeviceId()) {
                    case -1:
                        ib.setBackgroundResource(R.drawable.draw_drums1);
                        break;

                    case -2:
                        ib.setBackgroundResource(R.drawable.draw_drums2);
                        break;
                    case -3:
                        ib.setBackgroundResource(R.drawable.draw_drums3);
                        break;
                    case -4:
                        ib.setBackgroundResource(R.drawable.draw_drums4);
                        break;

                }
                //ib.setBackgroundResource(R.drawable.);
                fluidsynth_ListSendNoteOffMessage(global_channel, note, drumsId);
				/*if(loadedDrums) {



				}*/

                break;
            default:
                // do nothing.
                break;
        }
        return false;


    }

    private final int AMOUNT_OF_REGISTRATION_BUTTON = 8;

    private int EFFECT_INSTRUMENT_ACTIVITY_CODE = 2;

    LinearLayout.LayoutParams sizeLayParam;

    private Velocity velo = new Velocity();

    @Override
    public void onClick(View p1) {
        // TODO: Implement this method


        switch (p1.getId()) {

            case (R.id.btnTrans0):

                //testToast("transpose:0");
                intTranspose = 0;

                txtvTranspose.setText("" + intTranspose);
                break;
            case (R.id.btnTransMin1):

                intTranspose = intTranspose - 1;
                txtvTranspose.setText("" + intTranspose);

                break;
            case (R.id.btnTransPlus1):

                intTranspose = intTranspose + 1;
                txtvTranspose.setText("" + intTranspose);

                break;

            case (R.id.btnTransMin12):

                intTranspose = intTranspose - 12;
                txtvTranspose.setText("" + intTranspose);

                break;

            case (R.id.btnTransPlus12):

                intTranspose = intTranspose + 12;
                txtvTranspose.setText("" + intTranspose);
                break;

            default:

                intTranspose = 0;


        }


    }

    private static final int PERMISSION_REQUEST_CODE_EXT_STORAGE_ = 724;
    private File myDirSettings;


    private File myDirSoundfonts;

    private int CHOOSE_INSTRUMENT_ACTIVITY_CODE = 1;

    private ArrayList<String> resultInstrumentList;

    private Button btnTransposePlus12;
    private Button btnTransposeMinus12;
    private Button btnTransposePlus1;
    private Button btnTransposeMinus1;
    private Button btnTranspose0;
    private TextView txtvTranspose;
    private int intTranspose = 0;

    private Button btnFixedVelo;
    private Button btnSoftVelo;

    SeekBar seekVelocity;
    TextView txtvVelocity;
    private int intVelocity;


    private int global_channel = 0;

    private int usbDeviceId = 0;
    private String usbDeviceName = "Display Keyboard";//"kein Usbdevice";
    private int usbCount = 0;


    private boolean bool_multi_mode = true;

    Button btnKeyboardAktivity;
    private SeekBar seekhe;
    private LinearLayout llVelocity;


    Spinner spinnVelocity;
    private LinearLayout layContainerUsbDevices;
    private LinearLayout layAddUsbDev;
    private ScrollView scrvIns;
    private UsbDeviceButton btnDeviceName;
    private LinearLayout layContainerSelIns;
    private ViewGroup layout;


    //DRUMS
    private InstrumentButton btnDrum1;
    private InstrumentButton btnDrum2;
    private InstrumentButton btnDrum3;
    private InstrumentButton btnDrum4;

    private Button btnDrums;
    private int drumsId = -1;
    private boolean loadDrums = false;


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
    ArrayList<SF2Preset> listeSF2Presets_local = new ArrayList<>();

    ArrayList<String> listeUsbDeviceNames = new ArrayList<>();
    ArrayList<Integer> listeUsbDeviceIds = new ArrayList<>();

    ArrayAdapter<String> adaptListeUsbDeviceNames;

    ArrayAdapter<String> listedInstrumentsAdapter;
    ArrayList<String> listeInstrumenteNames = new ArrayList<String>();
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


    String myLastPath = "myLastPath";
    String tempSoundfontPath = "";


    int[] arrProgram = {48, 52, 40, 60, 61, 73, 46, 28, 14};
    int iProgr = 0;
    private InstrumentButton tempBtnInstr;
    private float floatRoomsize = 0.5f;


    /**
     * Choose device from spinner
     *
     * @return the MidiOutputDevice from spinner
     */
    @Nullable
    MidiOutputDevice getMidiOutputDeviceFromSpinner() {
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


    private void startIntentEffects() {

        instrumentContainerArrayList = new ArrayList<>();
        int llCount = layContainerSelIns.getChildCount();

        for (int j = 0; j < llCount; j++) {


            InstrumentButton instru = (InstrumentButton) layContainerSelIns.getChildAt(j);

            String strInstruName = instru.getText().toString();
            InstrumentContainer insc = new InstrumentContainer();
            insc.setInstrumentName(strInstruName);
            insc.setSoundfontPath(instru.getSoundfontpath());
            insc.setOn(instru.isBoolVolumeOn());

            instrumentContainerArrayList.add(insc);


        }

            //testToast(""+tempBtnInstr.getText().toString());
        Intent intent = new Intent(MIDIDriverMultipleSampleActivity.this, EffectActivity.class);

        intent.putExtra("instrumentList",instrumentContainerArrayList);
        intent.putExtra("Instrument", tempBtnInstr.getText().toString());
        intent.putExtra("UsbdeviceID", tempBtnInstr.getUsbDeviceId());
        intent.putExtra("Channel", global_channel);

        intent.putExtra("pathOfSettingsDir",myDirSettings.getAbsolutePath());



        //effectContainerEC.setIntVeloc(70);
        effectContainerEC = tempBtnInstr.getEffectContainerEC();

        if (effectContainerEC != null) {
            intent.putExtra("EffectList", tempBtnInstr.getEffectContainerEC());
        } else {

            effectContainerEC = new EffectContainer();

            intent.putExtra("EffectList", effectContainerEC);

        }

        startActivityForResult(intent, EFFECT_INSTRUMENT_ACTIVITY_CODE);
    }

    private void startIntent() {

        //getSavedInstruments();

        Intent intent = new Intent(MIDIDriverMultipleSampleActivity.this, InstrumentChooseActivity.class);

        if (loadDrums) {
            intent.putExtra("drums", 1);
        } else {

            intent.putExtra("drums", 0);
        }

        startActivityForResult(intent, CHOOSE_INSTRUMENT_ACTIVITY_CODE);
    }

    private void startIntentPianoView() {


        Intent intent = new Intent(MIDIDriverMultipleSampleActivity.this, KeyboardActivity.class);


        startActivity(intent);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //super.onActivityResult(requestCode, resultCode, data);

        ArrayList<SF2Preset> listeSF2Presets;
        Log.d("onActivityResult", "onActivityResult: test");
        if (requestCode == CHOOSE_INSTRUMENT_ACTIVITY_CODE) {
            if (resultCode == this.RESULT_OK && loadDrums == false) {

                //testToast("result ok");
                resultInstrumentList = data.getStringArrayListExtra("selectedInstrumentList");
                tempSoundfontPath = data.getStringExtra("soundfontpath");
                String soundfontPath = tempSoundfontPath;
                listeSF2Presets = (ArrayList<SF2Preset>) data.getSerializableExtra("listeSF2Presets");

                if (listeSF2Presets.isEmpty() || resultInstrumentList.isEmpty()) {


                    testToast("Liste ist leer :-(");


                } else {


                    boolean matchedDuplicate = false;

                    int llCount = layContainerSelIns.getChildCount();

                    for (String i : resultInstrumentList) {

                        if (llCount > 0) {

                            for (int j = 0; j < layContainerSelIns.getChildCount(); j++) {


                                InstrumentButton instru = (InstrumentButton) layContainerSelIns.getChildAt(j);

                                String strInstruName = instru.getText().toString();

                                if (strInstruName.compareToIgnoreCase(i) == 0) {

                                    matchedDuplicate = true;
                                    //wenn das instrument schon in der liste ist, dann darf es nicht
                                    //nochmal geladen werden
                                }


                            }

                            if (matchedDuplicate) {

                                //do nothing
                            } else {

                                addLayoutForInstruments(i, soundfontPath, listeSF2Presets);

                            }


                        } else if (llCount == 0) {


                            addLayoutForInstruments(i, soundfontPath, listeSF2Presets);


                        }
                        //addLayoutForInstruments(i, soundfontPath, listeSF2Presets);


                    }


                    try {
                        saveSelectedInstruments();//,soundfontPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //LinearLayout tempLay = (LinearLayout) findViewById(usbDeviceId);

                    //setSingleModeLayout(tempLay,soundfontPath);

                    setInstrumentMode();

                }


            } else if (resultCode == this.RESULT_OK && loadDrums == true) {


                resultInstrumentList = data.getStringArrayListExtra("selectedInstrumentList");


                String soundfontPath = data.getStringExtra("soundfontpath");


                listeSF2Presets = (ArrayList<SF2Preset>) data.getSerializableExtra("listeSF2Presets");

                if (listeSF2Presets.isEmpty() || resultInstrumentList.isEmpty()) {


                    testToast("Liste ist leer :-(");


                } else {

                    switch (drumsId) {

                        case -1:
                            tempBtnInstr = (InstrumentButton) findViewById(R.id.drum1);

                            break;

                        case -2:

                            tempBtnInstr = (InstrumentButton) findViewById(R.id.drum2);
                            break;
                        case -3:

                            tempBtnInstr = (InstrumentButton) findViewById(R.id.drum3);
                            break;
                        case -4:

                            tempBtnInstr = (InstrumentButton) findViewById(R.id.drum4);
                            break;

                        default:

                            tempBtnInstr = (InstrumentButton) findViewById(R.id.drum1);
                            break;


                    }
                    //tempBtnInstr = (InstrumentButton) findViewById(R.id.drum1);

                    strDrumName = tempBtnInstr.getText().toString();
                    if (data.getStringExtra("drumName") != "") {

                        //btnDrum1.setText(data.getStringExtra("drumName")+":"+data.getIntExtra("drumKey",35));

                        tempBtnInstr.setDrumButton(true);
                        tempBtnInstr.setDrumKey(data.getIntExtra("drumKey", 35));
                        tempBtnInstr.setText(data.getStringExtra("drumName"));
                        tempBtnInstr.setInstrumentName(resultInstrumentList.get(0).toString());

                    }


                    //tempBtnInstr.setText(resultInstrumentList.get(0).toString());
                    tempBtnInstr.setListeSF2Presets(listeSF2Presets);
                    tempBtnInstr.setSoundfontpath(soundfontPath);

                    addInstrumentTo_FluidSynthList_();

                }

            } else {


            }

        } else if (requestCode == EFFECT_INSTRUMENT_ACTIVITY_CODE) {



            //testToast("effect_instrument");
            if (resultCode == this.RESULT_OK) {
                //testToast("effect_ok");
                final LinearLayout tempLay = (LinearLayout) tempBtnInstr.getParent();
                String deleteInstr = data.getStringExtra("deleteIns");
                String shareInstr = data.getStringExtra("shareInstr");


                if(shareInstr.compareToIgnoreCase("all")==0){

                    shareAll=true;

                    //File f = new File(data.getStringExtra("shareFile"));
                    startIntentSharingFile(data.getStringExtra("shareFile"));
                    //popUpEditText();





                }else if(shareInstr.compareToIgnoreCase("this")==0){

                    shareAll=false;
                    //File f = new File(data.getStringExtra("shareFile"));

                    startIntentSharingFile(data.getStringExtra("shareFile"));
                    //popUpEditText();





                }else if(shareInstr.compareToIgnoreCase("")==0){




                }

                //testToast(deleteInstr);

                if (deleteInstr.compareToIgnoreCase("all") == 0) {
                    //testToast("effect_delete-all");

                    for (int i = 0; i < tempLay.getChildCount(); i++) {


                        tempBtnInstr = (InstrumentButton) tempLay.getChildAt(i);
                        if (tempBtnInstr.isBoolVolumeOn()) {

                            tempBtnInstr.performClick();

                        }

                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("delete instruments...");
                    builder.setMessage("delete all instrument-buttons finally?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            tempLay.removeAllViews();
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            dialogInterface.dismiss();

                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();




                } else if (deleteInstr.compareToIgnoreCase("this") == 0) {



                    int delete = fluidsynth_ListDeleteInstrumentFinal(global_channel, tempBtnInstr.getUsbDeviceId(), tempBtnInstr.getText().toString());
                    if (delete == 1) {

                        tempLay.removeView(tempBtnInstr);
                    }
                } else if (deleteInstr.compareToIgnoreCase("") == 0) {

                    effectContainerEC = (EffectContainer) data.getSerializableExtra("EffectList");
                    tempBtnInstr.setEffectContainerEC(effectContainerEC);


                    //do nothing
                }
            }


        }
    }

    private void popUpEditText() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save File As...");

        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                strSharedFileName = externeDateiPreFix + input.getText().toString();
                //File file = new File(myDirSettings, strSharedFileName);

                try {
                    //saveSharedInstruments(file,shareAll);
                    saveSharedInstruments();
                } catch (IOException e) {
                    e.printStackTrace();
                }



                dialog.dismiss();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                strSharedFileName ="";
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void startIntentSharingFile(String fileName) {
        File f = new File(myDirSettings, fileName+".txt");
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri uri = FileProvider.getUriForFile(getApplicationContext(),"jp.kshoji.driver.midi.sample.MIDIDriverMultipleSampleActivity",
                f);
        sharingIntent.setType("text/plain");
        //sharingIntent.setType(URLConnection.guessContentTypeFromName(f.getName()));
        //String strFileName = /*fileName*/"#_z" + ".txt";
        //sharingIntent.setData(Uri.parse("file://"+f.getAbsolutePath()));


        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);//myDirSettings+"/"+strFileName));//file.getAbsolutePath()));//"file://" + file.getAbsolutePath()));
        sharingIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(sharingIntent, "share file with..."+fileName));
    }


    private void getSavedInstruments() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("open saved instruments...");
        builder.setMessage("do you want to open saved instruments from storage?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try {
                    loadInstruments();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                startIntent();
                dialogInterface.dismiss();

            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }

    private void saveSelectedInstruments() throws IOException {


        //String usbDeviceIdentifier = ""+usbDeviceId;


        File file = new File(myDirSettings, ""+usbDeviceId+"_"+usbDeviceName);


        FileOutputStream stream = new FileOutputStream(file);



        LinearLayout view = (LinearLayout) findViewById(usbDeviceId);
        String instr = "";
        int countInstr = view.getChildCount();
        for (int i = 0; i < countInstr; i++) {

            InstrumentButton ib = (InstrumentButton) view.getChildAt(i);
            instr = instr + ib.getSoundfontpath() + "\n";
            instr = instr + ib.getText().toString() + "\n";

            //TODO: save effects...
            //instr += instr + "\n";

        }


        stream.write(instr.getBytes());

        stream.close();

        instrumentsSaved = true;

    }

    private void saveSharedInstruments() throws IOException {

        //TODO: trqnsfer this to effectactivity without send file intent
        File tempFile = new File(myDirSettings,strSharedFileName+".txt");


//        File tempFile = File.createTempFile(strSharedFileName,".txt",getApplicationContext().getCacheDir());
        //file.createNewFile();

        FileOutputStream stream = new FileOutputStream(tempFile);

        LinearLayout view = (LinearLayout) findViewById(usbDeviceId);
        String instr = "";

        if (shareAll) {
            int countInstr = view.getChildCount();
            for (int i = 0; i < countInstr; i++) {

                InstrumentButton ib = (InstrumentButton) view.getChildAt(i);

                if(ib.isBoolVolumeOn()) {
                    instr = instr + ib.getSoundfontpath() + "\n";
                    instr = instr + ib.getText().toString() + "\n";

                    //TODO: save effects...
                    //instr += instr + "\n";
                }

            }
        }else {


            instr = instr + tempBtnInstr.getSoundfontpath() + "\n";
            instr = instr + tempBtnInstr.getText().toString() + "\n";
            //btnKeyboardAktivity.setText(instr);
        }


        stream.write(instr.getBytes());

        stream.flush();
        stream.close();




        //if(file.exists()) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://" + tempFile.getAbsolutePath()));
            startActivity(Intent.createChooser(sharingIntent, "share file with...." + tempFile.getAbsolutePath()));
        //}


        //tempFile.delete();





    }
    private void saveSharedInstruments(File f,boolean shareAll)throws IOException{

        FileOutputStream stream = new FileOutputStream(f);

        LinearLayout view = (LinearLayout) findViewById(usbDeviceId);
        String instr = "";

        if (shareAll) {
            int countInstr = view.getChildCount();
            for (int i = 0; i < countInstr; i++) {

                InstrumentButton ib = (InstrumentButton) view.getChildAt(i);

                if(ib.isBoolVolumeOn()) {
                    instr = instr + ib.getSoundfontpath() + "\n";
                    instr = instr + ib.getText().toString() + "\n";

                    //TODO: save effects...
                    //instr += instr + "\n";
                }

            }
        }else {

            instr = instr + tempBtnInstr.getSoundfontpath() + "\n";
            instr = instr + tempBtnInstr.getText().toString() + "\n";

        }


        stream.write(instr.getBytes());

        stream.close();


        //startIntentSharingFile(f);



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
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main_layout);//neu);

        btnKeyboardAktivity = (Button) findViewById(R.id.btnKeyboardAktivity);

        btnKeyboardAktivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {

                startIntentPianoView();

                // TODO: Implement this method
            }


        });

        llVelocity = (LinearLayout) findViewById(R.id.llVelocity);
        seekhe = (SeekBar) findViewById(R.id.mainSeekBarHallEffect);


        seekhe.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar p1, int p2, boolean p3) {
                // TODO: Implement this method
                ViewGroup.LayoutParams params = llVelocity.getLayoutParams();
// Changes the height and width to the specified *pixels*
                //params.height = 100;
                params.width = p1.getProgress();
                llVelocity.setLayoutParams(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar p1) {
                // TODO: Implement this method
            }

            @Override
            public void onStopTrackingTouch(SeekBar p1) {
                // TODO: Implement this method


            }


        });

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final int maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        curVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);

        txtvVolume = (TextView) findViewById(R.id.txtvVolume);


        seekVolume = (SeekBar) findViewById(R.id.seekVolume);
        seekVolume.setMax(maxVol);
        seekVolume.setProgress(curVol);

        seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar p1, int p2, boolean p3) {

                am.setStreamVolume(AudioManager.STREAM_MUSIC, p2, 0);
                txtvVolume.setText("" + p2);

                // TODO: Implement this method
            }

            @Override
            public void onStartTrackingTouch(SeekBar p1) {
                // TODO: Implement this method
            }

            @Override
            public void onStopTrackingTouch(SeekBar p1) {
                // TODO: Implement this method
            }


        });

        //SampleRate settings
        txtvSampleRate = (TextView) findViewById(R.id.txtvSampleRate);


        seekSampleRate = (SeekBar) findViewById(R.id.seekSampleRate);
        seekSampleRate.setMax(96000);
        


        seekSampleRate.setProgress(sampleRate);
        sampleRate = seekSampleRate.getProgress();


        seekSampleRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar p1, int p2, boolean p3) {

                int stepSize = 100;
                sampleRate = (p2/stepSize)*stepSize;
                txtvSampleRate.setText("sr: " + sampleRate);
                fluidsynthListsetSampleRate(sampleRate);


                // TODO: native Code for Samplerate
            }

            @Override
            public void onStartTrackingTouch(SeekBar p1) {
                // TODO: Implement this method
            }

            @Override
            public void onStopTrackingTouch(SeekBar p1) {
                // TODO: Implement this method
            }


        });


        btnDrums = (Button) findViewById(R.id.btnDrums);
        btnDrums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                loadDrums = true;
                startIntent();

            }
        });

        btnDrum1 = (InstrumentButton) findViewById(R.id.drum1);
        btnDrum1.setOnTouchListener(this);
        btnDrum1.setUsbDeviceId(-1);
        btnDrum1.setDrumButton(true);


        btnDrum2 = (InstrumentButton) findViewById(R.id.drum2);
        btnDrum2.setOnTouchListener(this);
        btnDrum2.setUsbDeviceId(-2);
        btnDrum2.setDrumButton(true);


        btnDrum3 = (InstrumentButton) findViewById(R.id.drum3);
        btnDrum3.setOnTouchListener(this);
        btnDrum3.setUsbDeviceId(-3);
        btnDrum3.setDrumButton(true);

        btnDrum4 = (InstrumentButton) findViewById(R.id.drum4);
        btnDrum4.setOnTouchListener(this);
        btnDrum4.setUsbDeviceId(-4);
        btnDrum4.setDrumButton(true);

        btnFixedVelo = (Button) findViewById(R.id.btnFixed);
        btnSoftVelo = (Button) findViewById(R.id.btnSoft);

        btnFixedVelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fluidsynthListsetFixedVel_For_All(global_channel, usbDeviceId, true);
                testToast("Anschlagdynamik aus!");

            }
        });

        btnSoftVelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fluidsynthListsetFixedVel_For_All(global_channel, usbDeviceId, false);
                testToast("Anschlagdynamik an!");

            }
        });


        velo.setBoolFixedVelocity(true);

        txtvVelocity = (TextView) findViewById(R.id.txtvVelocity);

        seekVelocity = (SeekBar) findViewById(R.id.seekVelocity);
        seekVelocity.setMax(127);
        seekVelocity.setKeyProgressIncrement(1);
        //seekVelocity.set setMin(0);
        seekVelocity.setProgress(100);


        seekVelocity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar p1, int p2, boolean p3) {

                intVelocity = p2;
                txtvVelocity.setText("Vel: " + intVelocity);

            }

            @Override
            public void onStartTrackingTouch(SeekBar p1) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar p1) {

                txtvVelocity.setText("Vel: " + intVelocity);
                velo.setLocal_velocity(intVelocity);
                fluidsynthListSetVelocity_For_All(global_channel, usbDeviceId, intVelocity);

            }


        });
        btnTranspose0 = (Button) findViewById(R.id.btnTrans0);
        btnTranspose0.setOnClickListener(this);

        btnTransposeMinus1 = (Button) findViewById(R.id.btnTransMin1);
        btnTransposeMinus1.setOnClickListener(this);

        btnTransposePlus1 = (Button) findViewById(R.id.btnTransPlus1);
        btnTransposePlus1.setOnClickListener(this);


        btnTransposeMinus12 = (Button) findViewById(R.id.btnTransMin12);
        btnTransposeMinus12.setOnClickListener(this);

        btnTransposePlus12 = (Button) findViewById(R.id.btnTransPlus12);
        btnTransposePlus12.setOnClickListener(this);

        txtvTranspose = (TextView) findViewById(R.id.txtviewTranspose);

        layContainerUsbDevices = (LinearLayout) findViewById(R.id.layoutForUsbDevices);


        checkPermission();
        try {
            createDir();
        } catch (IOException e) {
        }


        //imgBtnCloseApp = (ImageButton) findViewById(R.id.imgBtnCloseApp);


        if (usbCount > 0) {

            //do nothing

        } else {



			/*try {
				loadInstruments();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
            addLayForUsbdevices(usbDeviceId);

        }


		/*
		try {
			tempSoundfontPath = copyAssetToTmpFile("GeneralUser.sf2");//"sndfnt.sf2");
			fluidsynthHelloWorld(tempSoundfontPath);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		 */


        //imgbtnDeleteInstrumentBtn = (ImageButton) findViewById(R.id.imageButtonDeleteInstr);
        spinnVelocity = (Spinner) findViewById(R.id.spinnerVel);

        thruToggleButton = (ToggleButton) findViewById(R.id.toggleButtonThru);
        cableIdSpinner = (Spinner) findViewById(R.id.cableIdSpinner);
        deviceSpinner = (Spinner) findViewById(R.id.deviceNameSpinner);

        connectedDevicesAdapter = new ArrayAdapter<UsbDevice>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, new ArrayList<UsbDevice>());
        deviceSpinner.setAdapter(connectedDevicesAdapter);


        final int channel = global_channel;//0; //TODO: get from spinner


        OnTouchListener onToneButtonTouchListener = new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                int note = 60 + Integer.parseInt((String) v.getTag()) + intTranspose;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //fluidsynthSendNoteOnMessage(0,note,127);
                        fluidsynth_ListSendNoteOnMessage(global_channel, note, intVelocity, 0);//TODO:channel
                        break;
                    case MotionEvent.ACTION_UP:

                        //fluidsynthSendNoteOffMessage(0,note);
                        fluidsynth_ListSendNoteOffMessage(global_channel, note, 0);//TODO: channel

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

        showRegistrationButtons();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        int deleted = deleteFluidsynth_Synth_List_Final();

        if (deleted == 0) {

            //testToast("No List, Goodbye :-)");

            exitApp();


        } else {

            testToast("Back pressed, List deleted, Goodbye :-)");

            exitApp();


        }

		/*
		finish();
		System.exit(0);


		 */

    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
        int deleted = deleteFluidsynth_Synth_List_Final();

        if (deleted == 0) {

            //testToast("No List, Goodbye :-)");

            exitApp();


        } else {

            testToast("onDestroy, List deleted, Goodbye :-)");

            exitApp();

            //String extSor = System.getenv("EXTERNAL_STORAGE");

        }

		/*
		finish();
		System.exit(0);


		 */


    }

    private void exitApp() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit App");
        builder.setMessage("do you realy want to close this app?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                finish();
                //System.exit(0);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                dialogInterface.dismiss();

            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }



    /*
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

     */


    /*
	private void hideDisplayPiano(){


		LinearLayout llDisplayPiano = (LinearLayout) findViewById(R.id.layButtTast);
		llDisplayPiano.setVisibility(LinearLayout.GONE);

		LinearLayout llDisplayPianoDeviceLay = (LinearLayout) findViewById(0);
		llDisplayPianoDeviceLay.setVisibility(LinearLayout.GONE);



	}

     */


    private void checkPermission() {

        Log.e(TAG, "checkPermission: ");
        // Check if we have WRITE_EXTERNAL_STORAGE permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE_EXT_STORAGE_);
            return;
        }


    }


    private void createDir() throws IOException {



        myDirSoundfonts = new File(Environment.getExternalStorageDirectory(), "MULTImidiSOUNDFONTplayer/SOUNDFONTS");//create directory and subfolder
        myDirSettings = new File(Environment.getExternalStorageDirectory() + File.separator + "MULTImidiSOUNDFONTplayer", "Settings");


        if (!myDirSoundfonts.exists()) {

            myDirSoundfonts.mkdirs();
            myDirSettings.mkdirs();


        } else {


            //testToast("Ordner existieren bereits!");

        }


    }

    private void showRegistrationButtons() {

        layContainerSelIns = (LinearLayout) findViewById(usbDeviceId);

        LinearLayout llRegbtn = (LinearLayout) findViewById(R.id.layOpenSf);
        for (int i = 1; i < AMOUNT_OF_REGISTRATION_BUTTON + 1; i++) {


            RegistrationButton regBtn = new RegistrationButton(this);

            regBtn.setId(i);
            regBtn.setBackgroundResource(R.drawable.circlebutton);
            regBtn.setText("" + i);

            sizeLayParam = new LinearLayout.LayoutParams(120, 120);

            sizeLayParam.setMargins(40, 0, 0, 0);

            regBtn.setLayoutParams(sizeLayParam);

            regBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View p1) {

                    loadInstrumentsFromRegistration((RegistrationButton) p1);

                }

            });


            regBtn.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View p1) {
                    // save registration
                    RegistrationButton reg = (RegistrationButton) p1;
                    reg.setOn(true);


                    if (reg.isBoolOn() && layContainerSelIns.getChildCount() > 1) {


                        ArrayList<Boolean> listeVolumeInstr = new ArrayList<>();

                        ArrayList<InstrumentButton> listeIb = new ArrayList<>();

                        for (int i = 0; i < layContainerSelIns.getChildCount(); i++) {

                            InstrumentButton ib = (InstrumentButton) layContainerSelIns.getChildAt(i);

                            listeVolumeInstr.add(ib.isBoolVolumeOn());
                            listeIb.add(ib);

                        }

                        reg.setListeIsVolumeOnInstr(listeVolumeInstr);
                        reg.setListeInstrBtn(listeIb);

                        if (!reg.getListeInstrBtn().isEmpty()) {

                            testToast("Register saved!");


                        }


                    }


                    return true;
                }


            });


            llRegbtn.addView(regBtn);

        }


    }

    private void loadInstrumentsFromRegistration(RegistrationButton p1) {

        RegistrationButton reg = p1;//(RegistrationButton)p1;

        reg.setOn(true);

        if (reg.isBoolOn() && reg.getListeInstrBtn() != null && !reg.getListeInstrBtn().isEmpty()) {


            //delete all loaded instruments from fluidsynth
            for (int i = 0; i < layContainerSelIns.getChildCount(); i++) {
                InstrumentButton ibutton = (InstrumentButton) layContainerSelIns.getChildAt(i);

                if (ibutton.isBoolVolumeOn()) {
                    String presetname = ibutton.getText().toString();
                    int deleted = fluidsynth_ListDeleteInstrumentFinal(global_channel, usbDeviceId, presetname);


                    if (deleted == 1) {


                        //testToast("gelöscht");

                    } else {

                        deleted = fluidsynth_ListDeleteInstrumentFinal(global_channel, usbDeviceId, presetname);

                    }
                }
            }

            layContainerSelIns.removeAllViews();
            int i = 0;
            for (InstrumentButton ib : reg.getListeInstrBtn()) {


                ib.setBoolVolumeOn(reg.getListeIsVolumeOnInstr().get(i));

                layContainerSelIns.addView(ib);

                if (ib.isBoolVolumeOn()) {


                    //method load instrument from fluidsynth

                    tempBtnInstr = ib;

                    addInstrumentTo_FluidSynthList_();

                }
                i++;


            }


            reg.setOn(false);
        }

    }

    private void loadInstruments() throws IOException {

        File fileMyLastPath;
        FileInputStream is;
        BufferedReader reader;

        arrlistSavedInstr.clear();
        arrlistSavedInstrFiles.clear();
        //int usb_id_ = 0;
        File directory = new File(myDirSettings.getAbsolutePath());

        if (!myDirSettings.exists()) {

            testToast("keine gespeicherte Instrumentenliste vorhanden!");

            //startIntent();
        } else {

            File[] files = directory.listFiles();
            if (files.length > 0) {
                for (int i = 0; i < files.length; i++) {

                    String fName = files[i].getName();
                    if (fName.compareToIgnoreCase(myLastPath) == 0) {

                        fileMyLastPath = files[i];

                        is = new FileInputStream(fileMyLastPath);
                        reader = new BufferedReader(new InputStreamReader(is));

                        myLastPath = reader.readLine();
                        reader.close();
                        is.close();




                        //do nothing

                    } else {
                        if(fName.compareToIgnoreCase("myLastPath")==0){


                        }else {
                            //usb_id_ = Integer.parseInt(fName);

                            arrlistSavedInstr.add(fName);
                            arrlistSavedInstrFiles.add(files[i]);


                        }

                        //boolLoadAltList=true;
                        /*if (fName.contains(String.valueOf(usbDeviceId))){


                            //testToast(String.valueOf(usb_id_));

                            readFile(String.valueOf(usbDeviceId), files[i]);

                            break;


                        } else {


                            boolLoadAltList=true;
                            //testToast("Liste auswählen...");
                            //loadAlternativList();
                            //break;
                            //startIntent();

                            //do nothing

                        }*/
                    }
                    //addLayForUsbdevices(usbDeviceId);


                }

                loadAlternativList();
                /*if(boolLoadAltList){

                    loadAlternativList();


                }*/
            } else {

                testToast("keine Liste vorhanden! Erst Instrumente laden...");
                //startIntent();
                //addLayForUsbdevices(usbDeviceId);
            }

        }
        //startIntent();

    }

    private void loadAlternativList(){

        final PopupWindow popupWindow = new PopupWindow(getApplicationContext());
        listedInstrumentsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, arrlistSavedInstr);
        final ListView listViewInstr = new ListView(getApplicationContext());
        listViewInstr.setAdapter(listedInstrumentsAdapter);
        listViewInstr.setOnItemClickListener(new AdapterView.OnItemClickListener(){



            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
            {

                try {
                    readFile(p2.toString(),arrlistSavedInstrFiles.get(p3));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                popupWindow.dismiss();
            }

        });




        /*
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {




            }

        });

         */

        popupWindow.setFocusable(true);
        popupWindow.setWidth(800);//WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setContentView(listViewInstr);
        popupWindow.showAtLocation(findViewById(R.id.layOpenSf), Gravity.CENTER,0,0);




    }

    //TODO: weiter bearbeiten
    private void readFile(String usbDeviceName, File file) throws FileNotFoundException, IOException {


        FileInputStream is;
        BufferedReader reader;


        int countLines = 0;

        if (file.exists()) {
            is = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(is));

            String line = reader.readLine();
            if (!(line.compareTo("") ==0)) {
                tempSoundfontPath = line;
                int myLastPathBackSlash = myLastPath.lastIndexOf("/");
                myLastPath = myLastPath.substring(0, myLastPathBackSlash);

                if (tempSoundfontPath.contains(myLastPath)) {

                    fluidsynthGetPresetName(tempSoundfontPath);

                    Log.d("countLines", "" + countLines);
                }

                //TODO: if counter is even than get soundfontpath otherwise get presetname
                while (line != null) {


                    countLines++;
                    if (countLines % 2 == 1) {//wenn counter gerade dann soundfontpath wählen, wenn ungerade dann presetname

                        line = reader.readLine();
                        addLayoutForInstruments(line, tempSoundfontPath, listeSF2Presets_local);
                        //listeSF2Presets_local.clear();
                        //listeInstrumenteNames.clear();
                        Log.d("countLines", "" + countLines);


                    } else {

                        line = reader.readLine();
                        if (line == null) {


                        } else {

                            tempSoundfontPath = line;
                            if (tempSoundfontPath.contains(myLastPath)) {

                                fluidsynthGetPresetName(tempSoundfontPath);

                                Log.d("countLines", "" + countLines);
                            }
                            //fluidsynthGetPresetName(tempSoundfontPath);
                            //Log.d("countLines", "" + countLines);
                        }

                    }


                }


                setInstrumentMode();
            }




            //

        } else {


        }


    }


    //#################   native funktions  ################################################################################################################


    public native int startFluidsynth_Add_Synth_to_List(String soundfontPath, int usbID, int channel, int bank, int progr);

    public native void fluidsynth_ListSendNoteOnMessage(int channel, int note, int velocity, int iUsbId);

    public native void fluidsynth_ListSendNoteOffMessage(int channel, int note, int iUsbId);

    public native void fluidsynth_ListDeleteInstrument(int channel, int iUsbId, String strPresetname);

    public native int fluidsynth_ListDeleteInstrumentFinal(int channel, int iUsbId, String strPresetname);

    public native int deleteFluidsynth_Synth_List_Final();

    public native void Fluidsynth_Synth_List_ProgramChange_drums(int channel, int bank, int progr, String strPresetname, int drumsId);


    public native void Fluidsynth_Synth_List_ProgramChange(int channel, int bank, int progr);

    public native void Fluidsynth_Synth_List_RoomsizeChange(float roomsize);

    public native void fluidsynthListSetVelocity(int global_channel, int tempUsbId, String tempPresetName, int intVeloc);

    public native void fluidsynthListSetVelocity_For_All(int global_channel, int usbDeviceId, int intVelocity);

    public native void fluidsynthListsetFixedVel_For_All(int channel, int iUsbId, boolean fixed);

    public native void fluidsynthListsetSampleRate(int sampleRate);

    public native void fluidsynthGetPresetName(String soundfontPath); //TODO: get all preset names of loaded Soundfontfile


    public native void fluidsynthHelloWorld(String soundfontPath);


    public native int startFluidsynth_Add_Synth(String soundfontPath, int usbID);//ALT nicht verwenden/don't use###############################


    public native void fluidsynth_ListSetInstrumentMuteOnOff(int channel, int iUsbId, String strPresetname);

    //jstring jSoundfontPath, jint usbId, jint channel, jint bank, jint progr
    public native void fluidsynthSendNoteOnMessage(int channel, int note, int velocity);

    public native void fluidsynthSendNoteOffMessage(int channel, int note);

    public native void fluidsynthProgrammChange(int programm);

    public native void fluidsynthDeleteSynth();

    public native int fluidsynthgetMore();


    public void fluidsynthListProgramChance(int channel, int bank, int progr) {


        Fluidsynth_Synth_List_ProgramChange(channel, bank, progr);

    }

    public void getPresetnamesFromList() {

        //ArrayList<SF2Preset>listeSF2Presets=new ArrayList<>();
        for (SF2Preset sf2presetName : listeSF2Presets_local) {

            listeInstrumenteNames.add(sf2presetName.getPresetname());

        }


    }

    public void setListeInstrumenteObjects(String presetname, int ibank, int iprogr) {


        //ArrayList<SF2Preset>listeSF2Presets=new ArrayList<>();
        listeSF2Presets_local.add(new SF2Preset(presetname, ibank, iprogr));


    }


    //###############  add Layout #####################################################################################################
    private void addLayForUsbdevices(int tempUsbDeviceId) {
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
        btnDeviceName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {


                loadDrums = false;
                String instrMode = "Multi-Mode";
                //@SuppressLint("ResourceType") int layId = p1.getId()-100;

                testToast(instrMode);
                bool_multi_mode = true;


                //tempBtn.getUsbDeviceId();

                //usbDeviceId = btnDeviceName.getUsbDeviceId();//tempBtn;


                //LinearLayout tempLay = (LinearLayout)findViewById(usbDeviceId);
                //tempLay.setTag("Multi-Mode");
                UsbDeviceButton tempBtn = (UsbDeviceButton) p1;

                usbDeviceId = tempBtn.getUsbDeviceId();
                usbDeviceName = tempBtn.getText().toString();
                layContainerSelIns = (LinearLayout) findViewById(tempBtn.getUsbDeviceId());
                layContainerSelIns.setTag(instrMode);

                getSavedInstruments();
                //startIntent();// TODO: evtl. rückgängig machen

                //setInstrumentOnPopUpWindow(tempLay);//layContainerSelIns); //p1);

            }


        });

        btnDeviceName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                String instrMode = "Single-Mode";
                testToast(instrMode);
                bool_multi_mode = false;
                //Button tempBtn = (Button)view;
				/*usbDeviceId = btnDeviceName.getUsbDeviceId();//(tempBtn.getId())-100;


				LinearLayout tempLay = (LinearLayout)findViewById(usbDeviceId);
				tempLay.setTag("Single-Mode");
				layContainerSelIns = (LinearLayout)findViewById(usbDeviceId);
				layContainerSelIns.setTag("Single-Mode");*/

                UsbDeviceButton tempBtn = (UsbDeviceButton) view;

                usbDeviceId = tempBtn.getUsbDeviceId();
                usbDeviceName = tempBtn.getText().toString();
                layContainerSelIns = (LinearLayout) findViewById(tempBtn.getUsbDeviceId());
                layContainerSelIns.setTag(instrMode);

                getSavedInstruments();
                //startIntent();

                //startIntent();// TODO: evtl. rückgängig machen

                //setInstrumentOnPopUpWindow(tempLay);
                //setSingleModeLayout(tempLay);


                return true;
            }
        });

        //btnDeviceName.setTextColor(Color.BLACK);


        layAddUsbDev.addView(btnDeviceName);
        layAddUsbDev.addView(scrvIns);


        layContainerUsbDevices.addView(layAddUsbDev);
    }

    //aktuell in Verwendung
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void addLayoutForInstruments(String strPresetName, final String soundfontpath, ArrayList<SF2Preset> listeSF2Presets)//int indexI, String deviceName, int deviceId, LinearLayout layInsTemp)
    {

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 30, 0, 0);

        InstrumentButton btnViewInstrument = new InstrumentButton(this);

        btnViewInstrument.setRotation(0.0f);
        btnViewInstrument.setLayoutParams(llp);

        btnViewInstrument.setListeSF2Presets(listeSF2Presets);
        btnViewInstrument.setUsbDeviceId(usbDeviceId);//layInsTemp.getId());
        btnViewInstrument.setSoundfontpath(soundfontpath);

        btnViewInstrument.setBoolVolumeOn(false);

        btnViewInstrument.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View p1) {

                layContainerSelIns = (LinearLayout) p1.getParent();
                tempBtnInstr = (InstrumentButton) p1;

                InstrumentButton btnIns = (InstrumentButton) p1;


                if (btnIns.isBoolVolumeOn() == false) {


                    //setInstrumentMuteOff();
                    btnIns.setBoolVolumeOn(true);

                    addInstrumentTo_FluidSynthList_(); //TODO: falls es nicht funktioniert wieder rückgängig machen

                } else if (btnIns.isBoolVolumeOn()) {

                    btnIns.setBoolVolumeOn(false);


                    //TODO: falls es nicht funktioniert wieder rückgängig machen
                    fluidsynth_ListDeleteInstrument(global_channel, tempBtnInstr.getUsbDeviceId(), tempBtnInstr.getText().toString());

                    //testToast(""+tempBtnInstr.getText() + " stumm!");


                }


            }
        });


        btnViewInstrument.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View p1) {

                layContainerSelIns = (LinearLayout) p1.getParent();
                tempBtnInstr = (InstrumentButton) p1;

                //InstrumentButton btnIns =(InstrumentButton)p1;

                if (!tempBtnInstr.isBoolVolumeOn()) {


                    LinearLayout templay = (LinearLayout) tempBtnInstr.getParent();
                    templay.removeView(tempBtnInstr);


                } else {


                    startIntentEffects();

                }

                //showOptions(p1);


                return true;
            }

        });

        btnViewInstrument.setText(strPresetName);

        //findViewById(R.id.seekbarlayout).setVisibility(LinearLayout.GONE);
        findViewById(R.id.laydropdownDevices).setVisibility(LinearLayout.GONE);
        //findViewById(R.id.layOpenSf).setVisibility(LinearLayout.GONE);

        LinearLayout layInsTemp = layContainerSelIns;
        layInsTemp.addView(btnViewInstrument);

        //listeSF2Presets_local.clear();


    }








    private void setInstrumentMode() {


        if (layContainerSelIns.getChildCount() > 1) {


        } else {
            int i = 0;
            InstrumentButton ib = (InstrumentButton) layContainerSelIns.getChildAt(i);
            if (!ib.isBoolVolumeOn()) {

                tempBtnInstr = ib;

                //setInstrumentMuteOff();

                ib.setBoolVolumeOn(true);

                //TODO: evtl. wieder Rückgängig machen
                addInstrumentTo_FluidSynthList_();


            }
        }

    }




    private void addInstrumentTo_FluidSynthList_() {

        String presetname = "";
        int progr = 0;
        int bank = 0;
        String getInstrumentOfButtoName;
        String getItemInInstrList;

        for (SF2Preset sf2p : tempBtnInstr.getListeSF2Presets()) {

            getItemInInstrList = sf2p.getPresetname();
            if (tempBtnInstr.isDrumButton()) {

                getInstrumentOfButtoName = tempBtnInstr.getInstrumentName();


            } else {
                getInstrumentOfButtoName = tempBtnInstr.getText().toString();


            }
            if (getItemInInstrList.equalsIgnoreCase(getInstrumentOfButtoName)) {

                progr = sf2p.getPresetprogr();
                bank = sf2p.getPresetbank();
                presetname = sf2p.getPresetname();
                //testToast("fromSavedInstruments: "+presetname);

            }

        }

        if (loadDrums) {


            //String str = tempBtnInstr.getText().toString();

            //testToast("loaddrums: "+ strDrumName);

            if (!Character.isDigit(strDrumName.charAt(0))) {

                //testToast("keine Zahl");
                Fluidsynth_Synth_List_ProgramChange_drums(global_channel, bank, progr, tempBtnInstr.getInstrumentName(), drumsId);

            } else {

//				testToast("zahl");
                int start = startFluidsynth_Add_Synth_to_List(tempBtnInstr.getSoundfontpath(), tempBtnInstr.getUsbDeviceId(), global_channel, bank, progr);

                if (start > 0) {

                    //testToast("drum geladen");
                    //if add succeed
                    //testToast(presetname+" geladen!");
                    Log.d("addinstr from single", "startFluidSynth " + start);
                    //Fluidsynth_Synth_List_RoomsizeChange(floatRoomsize);

                } else {

                    testToast("ERROR from native-code: startFluidsynth_Add_Synth_to_List");
                    Log.d("addinstr from single", "error from native code" + start);

                }

            }


        } else {


            int start = startFluidsynth_Add_Synth_to_List(tempBtnInstr.getSoundfontpath(), tempBtnInstr.getUsbDeviceId(), global_channel, bank, progr);

            if (start > 0) {

                //if add succeed
                //testToast(presetname+" geladen!");
                Log.d("addinstr from single", "startFluidSynth " + start);
                //Fluidsynth_Synth_List_RoomsizeChange(floatRoomsize);

            } else {

                testToast("ERROR from native-code: startFluidsynth_Add_Synth_to_List");
                Log.d("addinstr from single", "error from native code" + start);

            }
        }


    }


    private void setInstrumentMuteOn() {


        tempBtnInstr.setBoolVolumeOn(false);
        //tempBtnInstr.setBackgroundResource(R.drawable.rectmutebtn);

    }

    private void setInstrumentMuteOff() {

        tempBtnInstr.setBoolVolumeOn(true);
        //tempBtnInstr.setBackgroundResource(R.drawable.rectbtnselins);


    }


    public void testToast(String s) {

        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

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

        boolean usbDeviceConnected = false;
        int usbCount2 = layContainerUsbDevices.getChildCount();
        UsbDevice usbNew = midiOutputDevice.getUsbDevice();

        usbDeviceId = usbNew.getDeviceId();

        UsbDeviceButton usbd = (UsbDeviceButton) findViewById(usbDeviceId);

        if (usbd != null) {


        } else {


            //UsbDevice usb = midiOutputDevice.getUsbDevice();
            usbDeviceName = usbNew.getProductName();
            //usbDeviceId = usb.getDeviceId();


            addLayForUsbdevices(usbDeviceId);

        }


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
        fluidsynth_ListSendNoteOffMessage(global_channel, note + intTranspose, sender.getUsbDevice().getDeviceId());//TODO: channel

    }


    @Override
    public void onMidiNoteOn(@NonNull final MidiInputDevice sender, int cable, int channel, int note, int velocity) {


        usbDeviceId = sender.getUsbDevice().getDeviceId();
        //fluidsynthSendNoteOnMessage(global_channel,note,velocity);
        fluidsynth_ListSendNoteOnMessage(global_channel, note + intTranspose, velocity, sender.getUsbDevice().getDeviceId());//TODO:channel

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

            private RegistrationButton nextRegBtn;


            @Override
            public void run() {

                if (counterRegBtn > AMOUNT_OF_REGISTRATION_BUTTON) {

                    counterRegBtn = 1;
                }
                nextRegBtn = (RegistrationButton) findViewById(counterRegBtn);
                loadInstrumentsFromRegistration(nextRegBtn);
                //instrModeSetNextInstr(sender.getUsbDevice().getDeviceId());


                counterRegBtn++;

            }
        });


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


    /*
    popupWindow = new PopupWindow(getApplicationContext());
    listedInstrumentsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, arrListInstr...);
    final ListView listViewInstr = new ListView(getApplicationContext());
	listViewInstr.setAdapter(listedInstrumentsAdapter);
    listViewInstr.setOnItemClickListener(new AdapterView.OnItemClickListener(){



			@TargetApi(Build.VERSION_CODES.LOLLIPOP)
			@Override
			public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
			{
			}

	});




		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {




            }

        });

        popupWindow.setFocusable(true);
		popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

		popupWindow.setContentView(listViewInstr);

     */


	/*private void setInstrumentOnPopUpWindow(final LinearLayout view)
	{

		final ArrayList<SF2Preset>listeSF2Presets=new ArrayList<>();
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
				*//*if (bool_multi_mode) {
					int start = startFluidsynth_Add_Synth_to_List(tempSoundfontPath, getUsbId, global_channel, bank, progr);

					if (start > 0) {

						//if add succeed
						testToast(getUsbId + ": " + getselectedItemInstr + " erfolgreich geladen!");

					} else {

						testToast("ERROR from native-code: startFluidsynth_Add_Synth_to_List");

					}
				}*//*



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
				*//*if(!bool_multi_mode){

					setSingleModeLayout(view);

				}*//*
            }
        });

		popupWindow.setFocusable(true);
		popupWindow.setWidth(800);//WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindow.setHeight(800);//WindowManager.LayoutParams.WRAP_CONTENT);

		popupWindow.setContentView(listViewInstr);
		//popupWindow.showAsDropDown(view, 0, 0);
		popupWindow.showAtLocation((View)findViewById(R.id.layoutForUsbDevices),0,0,0);
		//popupWindow.showAtLocation((View)(findViewById(R.id.layAppContent)),0,0,0);


	}*/
	/*private void setInstrmentspinner(){

	    listedInstrumentsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, listeInstrumenteNames);

        presetSpinner.setAdapter(listedInstrumentsAdapter);
		testToast("fertig 2");

    }*/

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

	/*private void showInstrumentButtonOptions(View instrumentV){


		final PopupWindow popupWindow = new PopupWindow(getApplicationContext());
		LinearLayout llInstrumentOptions = (LinearLayout)findViewById(R.id.instrument_options);



		final InstrumentButton tempInstrButt = (InstrumentButton)instrumentV;
		final int tempUsbId = tempInstrButt.getUsbDeviceId();
		final String tempPresetName = (String) tempInstrButt.getText();
		final LinearLayout llTemp = (LinearLayout) findViewById(tempUsbId); //usbDeviceId);



		*//*
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


		 *//*
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


	}*/

	/*//NICHT verwenden
	private void addInstrumentToFluidsynthList(String getInstrOfList,int usbId, String soundfontpath) {

		ArrayList<SF2Preset>listeSF2Presets=new ArrayList<>();

        String getItemInInstrList;
        int iprogr=0;
        int ibank=0;
        String getPresetName ="";
        //testToast("getInstrOfList:"+getInstrOfList);
        for (SF2Preset sf2p:listeSF2Presets) {

            getItemInInstrList = sf2p.getPresetname();


            if (getItemInInstrList.equalsIgnoreCase(getInstrOfList)){

                iprogr = sf2p.getPresetprogr();
                ibank = sf2p.getPresetbank();
                getPresetName = sf2p.getPresetname();
                //testToast("getPresetName"+getPresetName);



            }
        }

        //native Fluidsynth-Instrument-Object anlegen mit progr und bank und usbid
        int start = startFluidsynth_Add_Synth_to_List(soundfontpath, usbId, global_channel, ibank, iprogr);

        if (start > 0) {

            //if add succeed
            testToast(getPresetName+" geladen!");

        } else {

            testToast("ERROR from native-code: startFluidsynth_Add_Synth_to_List");

        }


    }*/

	/*//NICHT verwenden
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void addLayoutForInstruments(String strPresetName, final String soundfontpath)//int indexI, String deviceName, int deviceId, LinearLayout layInsTemp)
    {

        InstrumentButton btnViewInstrument = new InstrumentButton(this);
        btnViewInstrument.setBackgroundResource(R.drawable.rectbtnselins);
        btnViewInstrument.setRotation(0.0f);
        btnViewInstrument.setUsbDeviceId(usbDeviceId);//layInsTemp.getId());
        btnViewInstrument.setSoundfontpath(soundfontpath);

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
                    addInstrumentToFluidsynthList(getInstrOfList,getSelUsbId, soundfontpath);

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


    }*/

	/*private void addInstrumentToFluidsynthList(String getInstrOfList,int usbId) {


		ArrayList<SF2Preset>listeSF2Presets=new ArrayList<>();
		String getItemInInstrList;
		int iprogr=0;
		int ibank=0;
		String getPresetName ="";
		//testToast("getInstrOfList:"+getInstrOfList);
		for (SF2Preset sf2p:listeSF2Presets) {

			getItemInInstrList = sf2p.getPresetname();


			if (getItemInInstrList.equalsIgnoreCase(getInstrOfList)){

				iprogr = sf2p.getPresetprogr();
				ibank = sf2p.getPresetbank();
				getPresetName = sf2p.getPresetname();
				//testToast("getPresetName"+getPresetName);



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


	}*/

	/*private void setSingleModeLayout(LinearLayout tempLay) {

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




	}*/

	/*//NICHT verwenden
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void addLayoutForInstruments(String strPresetName)//int indexI, String deviceName, int deviceId, LinearLayout layInsTemp)
	{

		InstrumentButton btnViewInstrument = new InstrumentButton(this);
		btnViewInstrument.setBackgroundResource(R.drawable.rectbtnselins);
		btnViewInstrument.setRotation(0.0f);
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


	}*/


}
