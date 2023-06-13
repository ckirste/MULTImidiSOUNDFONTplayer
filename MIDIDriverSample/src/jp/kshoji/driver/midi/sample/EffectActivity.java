package jp.kshoji.driver.midi.sample;

import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.widget.SeekBar.*;
import android.widget.CompoundButton.*;

import jp.kshoji.driver.midi.sample.util.EffectContainer;


public class EffectActivity extends Activity
{
    static {
        System.loadLibrary("native-lib");
    }

    EffectContainer ec;

    boolean boolSplitNotes=false;
    boolean boolMuteNotesSmalerThan=false;
    boolean boolMuteNotesGreaterThan=false;

    Button btnMuteNoteSmalerThan;
    Button btnMuteNoteGreaterThan;



    Button btnOk;
    Button btnDel;
    Button btnFixedVeloc;
    Button btnSoftVeloc;

    TextView textvInstr;

    private float floatProgress = 0.0f;
    //private int intVolume=0;
    TextView textvVol;

    TextView textvChorusDepth;
    TextView textvChorusLev;
    TextView textvChorusNr;
    TextView textvChorusSpeed;

    TextView textvReverbDamp;
    TextView textvReverbLevel;
    TextView textvReverbRoomsz;
    TextView textvReverbWidth;

    SeekBar seekVol;//(Seekbar)

    SeekBar seekChorusDepth;
    SeekBar seekChorusLev;
    SeekBar seekChorusNr;
    SeekBar seekChorusSpeed;

    SeekBar seekReverbDamp;
    SeekBar seekReverbLevel;
    SeekBar seekReverbRoomsz;
    SeekBar seekReverbWidth;

    CheckBox chckbxChorusOn;
    CheckBox chckbxReverbOn;

    private CheckBox chckbxApplyAll;

    private boolean boolApplyAll = false;

    private boolean boolDelIns = false;

    private String instr;
    private int usbDeviceId;
    private int global_channel;
    private float floatProgressRbW;
    private float floatProgressRbRS;
    private float floatProgressRbL;
    private float floatProgressRbD;
    private float floatProgressChS;
    private float floatProgressChL;
    private float floatProgressChD;

    private Button btnPitchPlus12;
    Button btnPitchMin12;
    private Button btnPitch0;

    private int transpo=0;

    private final String headline = "Instrument Effects";
    private boolean boolBtnOkpressed=false;
    private boolean boolfixedVel=false;
    private int intVel;
    private int intChorusNr;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        //setTheme(R.style.Theme_AppCompat_Dialog);

        setContentView(R.layout.effectactivity_lay);


        instr = getIntent().getStringExtra("Instrument");

        //ec = new EffectContainer();
        ec = (EffectContainer) getIntent().getSerializableExtra("EffectListe");




        usbDeviceId = getIntent().getIntExtra("UsbdeviceID",0);

        global_channel = getIntent().getIntExtra("Channel",0);

        textvInstr = (TextView)findViewById(R.id.textvInstrEff);
        textvInstr.setText(textvInstr.getText()+" "+  instr);

        btnSoftVeloc = (Button)findViewById(R.id.btnFixed);
        btnSoftVeloc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                boolfixedVel=false;
                testToast("Anschlagdyn. an!");
                fluidsynthListsetFixedVel(global_channel,usbDeviceId,instr,boolfixedVel);


            }
        });


        btnFixedVeloc = (Button)findViewById(R.id.btnFixed);
        btnFixedVeloc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                boolfixedVel=true;
                testToast("Anschlagdyn. aus!");
                fluidsynthListsetFixedVel(global_channel,usbDeviceId,instr,boolfixedVel);


            }
        });


        btnDel = (Button)findViewById(R.id.btnDeleteInstr);
        btnDel.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View p1)
            {

                boolDelIns = true;
                finish();


            }





        });

        btnOk = (Button)findViewById(R.id.btnEffectOk);
        btnOk.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View p1)
            {

                boolBtnOkpressed=true;
                finish();
                // TODO: Implement this method
            }




        });
        chckbxApplyAll = (CheckBox) findViewById(R.id.chckbxApplyAll);
        chckbxApplyAll.setOnCheckedChangeListener(new OnCheckedChangeListener(){



            @Override
            public void onCheckedChanged(CompoundButton p1, boolean p2)
            {


                if(p1.isChecked()){

                    textvVol.setText("all: "+seekVol.getProgress());
                    boolApplyAll=true;


                }else{

                    boolApplyAll=false;


                }

            }

        });


        chckbxChorusOn = (CheckBox) findViewById(R.id.chckbxChorus);
        chckbxChorusOn.setChecked(true);
        chckbxChorusOn.setOnCheckedChangeListener(new OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton p1, boolean p2)
            {


                if(p1.isChecked()){

                    //testToast("Chorus is activ!");
                    setFluidsynthChorusActive(global_channel, usbDeviceId, instr, boolApplyAll);



                }
            }

        });

        chckbxReverbOn = (CheckBox) findViewById(R.id.chckbxReverb);
        chckbxReverbOn.setChecked(true);
        chckbxReverbOn.setOnCheckedChangeListener(new OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton p1, boolean p2)
            {


                if(p1.isChecked()){


                    //testToast("Reverb is activ!");
                    int isActive = setFluidsynthReverbActive(global_channel, usbDeviceId, instr,boolApplyAll);

                    testToast("reverb active:" + isActive);

                }

            }


        });

        btnPitchMin12 = (Button)findViewById(R.id.btnMin12);
        btnPitchMin12.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                transpo=transpo-12;
                testToast(""+transpo);
                textvInstr.setText(headline+" "+instr+": "+transpo);
                fluidsynthListSetTranspo(global_channel,usbDeviceId,instr,transpo);
            }
        });


        btnPitchPlus12 = (Button)findViewById(R.id.btnPlus12);
        btnPitchPlus12.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                transpo=transpo+12;
                testToast(""+transpo);
                textvInstr.setText(headline+" "+instr+": "+transpo);
                fluidsynthListSetTranspo(global_channel,usbDeviceId,instr,transpo);
            }
        });

        btnPitch0 = (Button)findViewById(R.id.btn0);
        btnPitch0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                transpo=0;
                testToast(""+transpo);
                textvInstr.setText(headline+" "+instr+": "+transpo);
                fluidsynthListSetTranspo(global_channel,usbDeviceId,instr,transpo);
            }
        });

        OnSeekBarChangeListener seekbarEffectlistener = new OnSeekBarChangeListener(){



            @Override
            public void onProgressChanged(SeekBar p1, int p2, boolean p3)
            {

                floatProgress = p2;


                switch(p1.getId()){

                    case R.id.seekVol:


                        if(boolApplyAll){

                            //fluidsynthListSetVelocity_For_All(global_channel,usbDeviceId,p2);
                            textvVol.setText("all: "+p2);
                        }else{

                            //fluidsynthListSetVelocity(global_channel,usbDeviceId, instr, p2);
                            textvVol.setText(""+p2);
                        }


                        break;

                }

            }


            @Override
            public void onStartTrackingTouch(SeekBar p1)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar p1)
            {

                floatProgress = p1.getProgress();

                intVel= p1.getProgress();
                switch(p1.getId()){

                    case R.id.seekVol:



                        if(boolApplyAll){

                            fluidsynthListSetVelocity_For_All(global_channel,usbDeviceId,intVel);
                            //textvVol.setText("all: "+p1.setProgress(););
                        }else{

                            fluidsynthListSetVelocity(global_channel,usbDeviceId, instr, intVel);
                            //textvVol.setText(""+p2);
                        }


                        break;


                }


            }

        };

        textvVol = (TextView)findViewById(R.id.textvVol);

        textvChorusDepth = (TextView)findViewById(R.id.textvChorusDepth);
        textvChorusLev = (TextView)findViewById(R.id.textvChorusLev);
        textvChorusNr = (TextView)findViewById(R.id.textvChorusNr);
        textvChorusSpeed = (TextView)findViewById(R.id.textvChorusSpeed);

        textvReverbDamp = (TextView)findViewById(R.id.textvReverbDamp);
        textvReverbLevel = (TextView)findViewById(R.id.textvReverbLevel);
        textvReverbRoomsz = (TextView)findViewById(R.id.textvReverbRoomsz);
        textvReverbWidth = (TextView)findViewById(R.id.textvReverbWidth);

        seekVol = (SeekBar)findViewById(R.id.seekVol);
        seekVol.setOnSeekBarChangeListener(seekbarEffectlistener);
        seekVol.setProgress(ec.getIntVeloc());
        seekVol.setMax(127);
        //seekVol.setMin(0);

        seekChorusDepth = (SeekBar)findViewById(R.id.seekChorusDepth);
        seekChorusDepth.setMax(256);
        //seekChorusDepth.setProgress((int) ec.getChorusDepth());
//        seekChorusDepth.setOnSeekBarChangeListener(seekbarEffectlistener);
        seekChorusDepth.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                floatProgressChD=(float)seekBar.getProgress();
                textvChorusDepth.setText(""+floatProgressChD);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {



            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                floatProgressChD=(float)seekBar.getProgress();
                //textvChorusDepth.setText(""+floatProgress);
                //Fluidsynth_Synth_List_Synthesizer_Settings(global_channel,usbDeviceId,instr,"synth.chorus.depth",floatProgress,boolApplyAll);

                Fluidsynth_Synth_List_ChorusDepth(global_channel,usbDeviceId,
                        instr,floatProgressChD,boolApplyAll);




            }
        });

        seekChorusLev = (SeekBar)findViewById(R.id.seekChorusLev);
        seekChorusLev.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                floatProgressChL=(float)seekBar.getProgress();
                textvChorusLev.setText(""+floatProgressChL);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                floatProgressChL=(float)seekBar.getProgress();
                //textvChorusLev.setText(""+floatProgress);

                //Fluidsynth_Synth_List_Synthesizer_Settings(global_channel,usbDeviceId,instr,"synth.chorus.level",floatProgress,boolApplyAll);

                Fluidsynth_Synth_List_ChorusLevel(global_channel,usbDeviceId,
                        instr,floatProgressChL,boolApplyAll);

            }
        });
        seekChorusLev.setMax(10);

        seekChorusNr = (SeekBar)findViewById(R.id.seekChorusNr);
        seekChorusNr.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textvChorusNr.setText(""+i);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                intChorusNr=seekBar.getProgress();
                Fluidsynth_Synth_List_ChorusNr(global_channel,usbDeviceId,
                        instr,intChorusNr,boolApplyAll);

            }
        });
        seekChorusNr.setMax(99);

        seekChorusSpeed = (SeekBar)findViewById(R.id.seekChorusSpeed);
        seekChorusSpeed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                floatProgressChS=(float)seekBar.getProgress();
                textvChorusSpeed.setText(""+floatProgressChS);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                floatProgressChS=(float)seekBar.getProgress();
                //textvChorusSpeed.setText(""+floatProgress);

                //Fluidsynth_Synth_List_Synthesizer_Settings(global_channel,usbDeviceId,instr,"synth.chorus.speed",floatProgress,boolApplyAll);

                Fluidsynth_Synth_List_ChorusSpeed(global_channel,usbDeviceId,
                        instr,floatProgressChS,boolApplyAll);


            }
        });
        seekChorusSpeed.setMax(5);

        seekReverbDamp = (SeekBar)findViewById(R.id.seekReverbDamp);
        seekReverbDamp.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


                floatProgressRbD=(float)i/100;
                textvReverbDamp.setText(""+floatProgressRbD);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                floatProgressRbD=(float)seekBar.getProgress()/100;
                //textvReverbDamp.setText(""+floatProgress);
                //Fluidsynth_Synth_List_Synthesizer_Settings(global_channel,usbDeviceId,instr,"synth.reverb.damp",floatProgress/100,boolApplyAll);

                Fluidsynth_Synth_List_ReverbDamp(global_channel,usbDeviceId,
                        instr,floatProgressRbD,boolApplyAll);

            }
        });
        seekReverbDamp.setMax(100);

        seekReverbLevel = (SeekBar)findViewById(R.id.seekReverbLevel);
        seekReverbLevel.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


                floatProgressRbL=(float)i/100;
                textvReverbLevel.setText(""+floatProgressRbL);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                floatProgressRbL=(float)seekBar.getProgress()/100;
                //textvReverbLevel.setText(""+floatProgress);

                //Fluidsynth_Synth_List_Synthesizer_Settings(global_channel,usbDeviceId,instr,"synth.reverb.level",floatProgress/100,boolApplyAll);

                Fluidsynth_Synth_List_ReverbLevel(global_channel,usbDeviceId,
                        instr,floatProgressRbL,boolApplyAll);

            }
        });
        seekReverbLevel.setMax(100);

        seekReverbRoomsz = (SeekBar)findViewById(R.id.seekReverbRoomsz);
        seekReverbRoomsz.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                floatProgressRbRS=(float) i/100;
                textvReverbRoomsz.setText(""+floatProgressRbRS);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                floatProgressRbRS=(float) seekBar.getProgress()/100;
                //textvReverbRoomsz.setText(""+floatProgress);

                //Fluidsynth_Synth_List_Synthesizer_Settings(global_channel,usbDeviceId,instr,"synth.reverb.room-size",floatProgress/100,boolApplyAll);

                int room = Fluidsynth_Synth_List_ReverbRoomsize(global_channel,usbDeviceId,
                        instr,floatProgressRbRS,boolApplyAll);

            }
        });
        seekReverbRoomsz.setMax(100);

        seekReverbWidth = (SeekBar)findViewById(R.id.seekReverbWidth);
        seekReverbWidth.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                floatProgressRbW=(float)i/100;
                textvReverbWidth.setText(""+floatProgressRbW);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                floatProgressRbW=(float)seekBar.getProgress()/100;
                //textvReverbWidth.setText(""+floatProgress);

                //Fluidsynth_Synth_List_Synthesizer_Settings(global_channel,usbDeviceId,instr,"synth.reverb.width",floatProgress,boolApplyAll);

                Fluidsynth_Synth_List_ReverbWidth(global_channel,usbDeviceId,
                        instr,floatProgressRbW,boolApplyAll);

            }
        });
        seekReverbWidth.setMax(100);

        btnMuteNoteGreaterThan = (Button) findViewById(R.id.btnMuteNoteGreater);
        btnMuteNoteSmalerThan = (Button) findViewById(R.id.btnMuteNoteSmaler);

        //btnMuteNoteGreaterThan.getBackground().getColorFilter();
        btnMuteNoteGreaterThan.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View p1)
            {

                Button btn = (Button)p1;
                // TODO: Implement this method
                if(boolSplitNotes){

                    boolSplitNotes=false;
                    boolMuteNotesGreaterThan=false;

                    setFluidsynthBoolSplitNotes(global_channel,usbDeviceId,instr,boolSplitNotes);
                    setFluidsynthBoolMuteNotesGreaterThan(global_channel,usbDeviceId,instr,boolMuteNotesGreaterThan);


                    btn.setText("M>");


                }else{

                    boolSplitNotes=true;

                    boolMuteNotesGreaterThan=true;

                    boolMuteNotesSmalerThan=false;


                    setFluidsynthBoolSplitNotes(global_channel,usbDeviceId,instr,boolSplitNotes);
                    setFluidsynthBoolMuteNotesGreaterThan(global_channel,usbDeviceId,instr,boolMuteNotesGreaterThan);
                    setFluidsynthBoolMuteNotesSmalerThan(global_channel,usbDeviceId,instr,boolMuteNotesSmalerThan);
                    btnMuteNoteSmalerThan.setText("M<");

                    btn.setText("M>"+"*");

                }

            }




        });

        btnMuteNoteSmalerThan.setOnClickListener(new OnClickListener(){



            @Override
            public void onClick(View p1)
            {
                // TODO: Implement this method
                Button btn = (Button)p1;


                if(boolSplitNotes){

                    boolSplitNotes=false;
                    boolMuteNotesSmalerThan=false;

                    setFluidsynthBoolSplitNotes(global_channel,usbDeviceId,instr,boolSplitNotes);
                    setFluidsynthBoolMuteNotesSmalerThan(global_channel,usbDeviceId,instr,boolMuteNotesSmalerThan);
                    btn.setText("M<");


                }else{

                    boolSplitNotes=true;

                    boolMuteNotesSmalerThan=true;
                    boolMuteNotesGreaterThan=false;

                    setFluidsynthBoolSplitNotes(global_channel,usbDeviceId,instr,boolSplitNotes);
                    setFluidsynthBoolMuteNotesSmalerThan(global_channel,usbDeviceId,instr,boolMuteNotesSmalerThan);
                    setFluidsynthBoolMuteNotesGreaterThan(global_channel,usbDeviceId,instr,boolMuteNotesGreaterThan);
                    btnMuteNoteGreaterThan.setText("M>");

                    btn.setText("M<"+"*");


                }
            }







        });

    }

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






    @Override
    public void finish()
    {



        Intent data = new Intent();


        if(chckbxApplyAll.isChecked() && boolDelIns){

            //testToast("delete all");
            data.putExtra("deleteIns","all");

        }else if(!chckbxApplyAll.isChecked() && boolDelIns){

            //testToast("delete this");
            data.putExtra("deleteIns","this");

        }else{

            data.putExtra("deleteIns","");

            ec = new EffectContainer();
            ec.setBoolfixedVel(boolfixedVel);
            ec.setIntVeloc(intVel);
            ec.setIntTranspo(transpo);
            ec.setBoolMuteNotesGreaterThan(boolMuteNotesGreaterThan);
            ec.setBoolMuteNotesSmalerThan(boolMuteNotesSmalerThan);
            ec.setBoolSplitNotes(boolSplitNotes);
            ec.setChorusDepth(floatProgressChD);
            ec.setChorusLevel(floatProgressChL);
            ec.setChorusNr(intChorusNr);
            ec.setChorusSpeed(floatProgressChS);
            ec.setReverbDamp(floatProgressRbD);
            ec.setReverbLevel(floatProgressRbL);
            ec.setReverbRoomsize(floatProgressRbRS);
            ec.setReverbWidth(floatProgressRbW);

            


        }




        setResult(RESULT_OK, data);

        super.finish();




    }



    public void testToast(String s)
    {

        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();


    }




}