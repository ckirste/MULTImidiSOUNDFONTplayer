package com.example.myapplication;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.hardware.usb.UsbDevice;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

import jp.kshoji.driver.midi.activity.AbstractMultipleMidiActivity;
import jp.kshoji.driver.midi.device.MidiInputDevice;
import jp.kshoji.driver.midi.device.MidiOutputDevice;

//import android.support.annotation.*;


public class MainActivity extends AbstractMultipleMidiActivity {

    //private static EditText dtext;

    //midi from java
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    // User interface
    final Handler midiInputEventHandler = new Handler(new Callback() {
        @Override
        public boolean handleMessage(Message msg)
        {
            if (midiInputEventAdapter != null)
            {
                midiInputEventAdapter.add((String)msg.obj);
            }
            // message handled successfully
            return true;
        }
    });

    final Handler midiOutputEventHandler = new Handler(new Callback() {
        @Override
        public boolean handleMessage(Message msg)
        {
            if (midiOutputEventAdapter != null)
            {
                midiOutputEventAdapter.add((String)msg.obj);
            }
            // message handled successfully
            return true;
        }
    });

    ArrayAdapter<UsbDevice> connectedDevicesAdapter;
    ArrayAdapter<String> midiInputEventAdapter;
    ArrayAdapter<String> midiOutputEventAdapter;
    private ToggleButton thruToggleButton;
    Spinner cableIdSpinner;
    Spinner deviceSpinner;

    EditText dt;

    /**
     * Choose device from spinner
     *
     * @return the MidiOutputDevice from spinner
     */
    @Nullable
    MidiOutputDevice getMidiOutputDeviceFromSpinner()
    {
        if (deviceSpinner != null && deviceSpinner.getSelectedItemPosition() >= 0 && connectedDevicesAdapter != null && !connectedDevicesAdapter.isEmpty())
        {
            UsbDevice device = connectedDevicesAdapter.getItem(deviceSpinner.getSelectedItemPosition());
            if (device != null)
            {


                Set<MidiOutputDevice> midiOutputDevices = getMidiOutputDevices();

                if (midiOutputDevices.size() > 0)
                {
                    // returns the first one.
                    return (MidiOutputDevice) midiOutputDevices.toArray()[0];
                }
            }
        }
        return null;
    }


    protected void onDestroy() {
        super.onDestroy();
        fluidsynthDeleteSynth();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.main);
        try {
            String tempSoundfontPath = copyAssetToTmpFile("GeneralUser.sf2");//"sndfnt.sf2");
            fluidsynthHelloWorld(tempSoundfontPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ListView midiInputEventListView = (ListView) findViewById(R.id.midiInputEventListView);
        midiInputEventAdapter = new ArrayAdapter<String>(this, R.layout.midi_event, R.id.midiEventDescriptionTextView);
        midiInputEventListView.setAdapter(midiInputEventAdapter);

        ListView midiOutputEventListView = (ListView) findViewById(R.id.midiOutputEventListView);
        midiOutputEventAdapter = new ArrayAdapter<String>(this, R.layout.midi_event, R.id.midiEventDescriptionTextView);
        midiOutputEventListView.setAdapter(midiOutputEventAdapter);

        thruToggleButton = (ToggleButton) findViewById(R.id.toggleButtonThru);
        cableIdSpinner = (Spinner) findViewById(R.id.cableIdSpinner);
        deviceSpinner = (Spinner) findViewById(R.id.deviceNameSpinner);

        connectedDevicesAdapter = new ArrayAdapter<UsbDevice>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, new ArrayList<UsbDevice>());
        deviceSpinner.setAdapter(connectedDevicesAdapter);

        View.OnTouchListener onToneButtonTouchListener = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {



                int note = 60 + Integer.parseInt((String) v.getTag());
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:

                        fluidsynthSendNoteOnMessage(0,note,127);
                        //usbSynth0.sendShortmessage(ShortMessage.NOTE_ON, note, 127, 0);

                        break;
                    case MotionEvent.ACTION_UP:

                        fluidsynthSendNoteOffMessage(0,note);
                        //usbSynth0.sendShortmessage(ShortMessage.NOTE_OFF, note, 127, 0);



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

        findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fluidsynthDeleteSynth();
                finish();

            }
        });

        findViewById(R.id.btnSetProgr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dt = findViewById(R.id.editTextNumber);
                int intProgr = Integer.parseInt(dt.getText().toString());
                fluidsynthProgrammChange(intProgr);
            }
        });
    }

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

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native void fluidsynthHelloWorld(String soundfontPath);

    //public native String getJniString();


    //public native int getJNIIntEvent();

    public native void fluidsynthSendNoteOnMessage(int channel,int note,int velocity);

    public native void fluidsynthSendNoteOffMessage(int channel,int note);

    public native void fluidsynthProgrammChange(int programm);

    public native void fluidsynthDeleteSynth();




    public void testToast(String s)
    {

        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();


    }

    @Override
    public void onDeviceAttached(UsbDevice usbDevice) {

    }

    @Override
    public void onMidiInputDeviceAttached(MidiInputDevice midiInputDevice) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMidiOutputDeviceAttached(MidiOutputDevice midiOutputDevice) {

        if (connectedDevicesAdapter != null)
        {
            connectedDevicesAdapter.remove(midiOutputDevice.getUsbDevice());
            connectedDevicesAdapter.add(midiOutputDevice.getUsbDevice());
            connectedDevicesAdapter.notifyDataSetChanged();
        }


        if (deviceSpinner.getSelectedItem() != null)
        {


            //txtViewUsbName.setText("" + midiOutputDevice.getUsbDevice().getProductName());

        }
        /*
        usbCount++;
        testToast("usbCount: " + usbCount);

        setUsbSynth(midiOutputDevice.getUsbDevice().getDeviceId());
        */

		/*
		 listeUsbdevices.add(new UsbDeviceInfo(midiOutputDevice.getUsbDevice().getProductName(), midiOutputDevice.getUsbDevice().getDeviceId()));
		 testToast("listsize Usbdevice: " + listeUsbdevices.size());
		 */
        UsbDevice usb= midiOutputDevice.getUsbDevice();
        String texttrenn="/";
        String text = "id/name/manufakt/produkt/serial: " + usb.getDeviceId() + texttrenn + usb.getDeviceName() +  texttrenn + usb.getManufacturerName() +  texttrenn + usb.getProductName() +  texttrenn + usb.getSerialNumber();

        Toast.makeText(MainActivity.this, "USB MIDI Device " + text/* midiOutputDevice.getUsbDevice().getDeviceName()*/+ " has been attached.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeviceDetached(UsbDevice usbDevice) {

    }

    @Override
    public void onMidiInputDeviceDetached(MidiInputDevice midiInputDevice) {


    }

    @Override
    public void onMidiOutputDeviceDetached(@NonNull final MidiOutputDevice midiOutputDevice) {

        if (connectedDevicesAdapter != null)
        {
            connectedDevicesAdapter.remove(midiOutputDevice.getUsbDevice());
            connectedDevicesAdapter.notifyDataSetChanged();
        }
        Toast.makeText(MainActivity.this, "USB MIDI Device " + midiOutputDevice.getUsbDevice().getDeviceName() + " has been detached.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMidiMiscellaneousFunctionCodes(MidiInputDevice sender, int cable, int byte1, int byte2, int byte3) {

    }

    @Override
    public void onMidiCableEvents(MidiInputDevice sender, int cable, int byte1, int byte2, int byte3) {

    }

    @Override
    public void onMidiSystemCommonMessage(MidiInputDevice sender, int cable, byte[] bytes) {

    }

    @Override
    public void onMidiSystemExclusive(MidiInputDevice sender, int cable, byte[] systemExclusive) {

    }

    @Override
    public void onMidiNoteOff(MidiInputDevice sender, int cable, int channel, int note, int velocity) {


        fluidsynthSendNoteOffMessage(0,note);
    }

    @Override
    public void onMidiNoteOn(MidiInputDevice sender, int cable, int channel, int note, int velocity) {

        fluidsynthSendNoteOnMessage(0,note,velocity);
        //TextView dtextview = findViewById(R.id.textViewEvent);
        //dtextview.setText(getJNIIntEvent());
    }

    @Override
    public void onMidiPolyphonicAftertouch(MidiInputDevice sender, int cable, int channel, int note, int pressure) {

    }

    @Override
    public void onMidiControlChange(MidiInputDevice sender, int cable, int channel, int function, int value) {

    }

    @Override
    public void onMidiProgramChange(MidiInputDevice sender, int cable, int channel, int program) {

    }

    @Override
    public void onMidiChannelAftertouch(MidiInputDevice sender, int cable, int channel, int pressure) {

    }

    @Override
    public void onMidiPitchWheel(MidiInputDevice sender, int cable, int channel, int amount) {

    }

    @Override
    public void onMidiSingleByte(MidiInputDevice sender, int cable, int byte1) {

    }

    @Override
    public void onMidiTimeCodeQuarterFrame(MidiInputDevice sender, int cable, int timing) {

    }

    @Override
    public void onMidiSongSelect(MidiInputDevice sender, int cable, int song) {

    }

    @Override
    public void onMidiSongPositionPointer(MidiInputDevice sender, int cable, int position) {

    }

    @Override
    public void onMidiTuneRequest(MidiInputDevice sender, int cable) {

    }

    @Override
    public void onMidiTimingClock(MidiInputDevice sender, int cable) {

    }

    @Override
    public void onMidiStart(MidiInputDevice sender, int cable) {

    }

    @Override
    public void onMidiContinue(MidiInputDevice sender, int cable) {

    }

    @Override
    public void onMidiStop(MidiInputDevice sender, int cable) {

    }

    @Override
    public void onMidiActiveSensing(MidiInputDevice sender, int cable) {

    }

    @Override
    public void onMidiReset(MidiInputDevice sender, int cable) {

    }
}