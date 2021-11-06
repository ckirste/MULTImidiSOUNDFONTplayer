package jp.kshoji.driver.midi.sample;

import android.annotation.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.io.*;
import java.util.*;

import jp.kshoji.driver.midi.sample.util.*;


import android.view.View.OnClickListener;

public class InstrumentChooseActivity extends Activity
{


	 static {

	 System.loadLibrary("native-lib");

	 }
	 

	//public InputStream is_temp = null;
	
	private final String SOUNDFONT_DIRECTORY="MULTImidiSOUNDFONTplayer/SOUNDFONTS";
	
	ArrayList<SF2Preset>listeSF2Presets=new ArrayList<>();

	ArrayList<String> listeInstrumenteNames=new ArrayList<String>();
	
	ArrayList<String> selectedInstrumentList = new ArrayList<String>();

	ArrayList<String> instrumentList = new ArrayList<String>();
	ArrayAdapter<String> listedInstrumentsAdapter;

	ArrayList<String> soundfontList = new ArrayList<String>();
	ArrayAdapter<String> listedSoundfontsAdapter;

	ListView lvInstruments;
	ListView lvSoundfonts;


	Button btnOk;
	Button btnCancel;

	private File myDirSoundfonts;

	private File myDirSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.chooseinstrument_layout);

		lvInstruments = (ListView) findViewById(R.id.instrumentList);
		lvInstruments.setChoiceMode(lvInstruments.CHOICE_MODE_MULTIPLE);
		lvInstruments.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{

					String ins = p1.getItemAtPosition(p3).toString();
					selectedInstrumentList.add(ins);


				}

			});



		lvSoundfonts = (ListView) findViewById(R.id.soundfontList);
		lvSoundfonts.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{

					testToast("Soundfont selected");

					String selSoundfont = p1.getItemAtPosition(p3).toString();
					File sfFile = new File(SOUNDFONT_DIRECTORY+"/"+selSoundfont);
					
					try
					{
						selSoundfont = copyExtFileToTmpFile(selSoundfont, sfFile);
						getInstrumentsFromNativeList(selSoundfont);
					}
					catch (IOException e)
					{}

					if(listeInstrumenteNames.isEmpty()){
						
						listedInstrumentsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, soundfontList);
						

					}else{
						
						listedInstrumentsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, listeInstrumenteNames);
						
						
						
					}
					
					lvInstruments.setAdapter(listedInstrumentsAdapter);


				}

				

			});
		try
		{
			createDir();
			listFiles();

		}
		finally
		{


		}

		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{

					setListToMainActivity();

					//finish();
				}

			});

		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method

					finish();
				}

			});


	}
	private void createDir()
	{



		myDirSoundfonts = new File(Environment.getExternalStorageDirectory(), SOUNDFONT_DIRECTORY);//create directory and subfolder
		myDirSettings = new File(Environment.getExternalStorageDirectory() + File.separator + "MULTImidiSOUNDFONTplayer", "Settings");



		if (!myDirSoundfonts.exists())
		{

			myDirSoundfonts.mkdirs();
			myDirSettings.mkdirs();


		}
		else
		{

			//testToast("Ordner existieren bereits!");

		}

	}
	private void listFiles()
	{



		//String path = Environment.getExternalStorageDirectory().toString()+"/Pictures";
		//Log.d("Files", "Path: " + myDirSoundfonts);
		File directory = new File(myDirSoundfonts.getAbsolutePath());
		File[] files = directory.listFiles();
		Log.d("Files", "Size: " + files.length);
		for (int i = 0; i < files.length; i++)
		{


			soundfontList.add(files[i].getName());

			Log.d("Files", "FileName:" + files[i].getName());
		}

		listedSoundfontsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, soundfontList);
		lvSoundfonts.setAdapter(listedSoundfontsAdapter);



	}
	public void testToast(String s)
	{

		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();


	}

	private void setListToMainActivity()
	{


		finish();

	}

	@Override
	public void finish()
	{



		Intent data = new Intent();
		data.putExtra("selectedInstrumentList", selectedInstrumentList);
		

		setResult(RESULT_OK, data);

		super.finish();




	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
    private String copyExtFileToTmpFile(String fileName, File fileObjekt) throws IOException
	{


        try (InputStream is = new FileInputStream(fileObjekt)){ //getAssets().open(fileName)) {

			//is_temp = is;
            String tempFileName = "tmp_" + fileName;

			File tempFilecheck = new File(getFilesDir() + "/" + tempFileName);
			if (!tempFilecheck.exists())
			{


				try (FileOutputStream fos = openFileOutput(tempFileName, Context.MODE_PRIVATE)) {

					int bytes_read;

					byte[] buffer = new byte[4096];

					while ((bytes_read = is.read(buffer)) != -1)
					{

						fos.write(buffer, 0, bytes_read);

					}

				}

				return getFilesDir() + "/" + tempFileName;
			}
			else
			{


				return getFilesDir() + "/" + tempFileName;

			}

        }

    }
	
	private void getInstrumentsFromNativeList(String selSoundfont)
	{
		// TODO: Implement this method
		fluidsynthGetPresetName_to_ChooseActivity(selSoundfont);
		
		
		
	}
	
	public void getPresetnamesFromList(){


        for(SF2Preset sf2presetName: listeSF2Presets){


            listeInstrumenteNames.add(sf2presetName.getPresetname());


        }


    }

    public void setListeInstrumenteObjects(String presetname,int ibank, int iprogr){


        listeSF2Presets.add(new SF2Preset(presetname,ibank,iprogr));


    }
	
	
	
	//#############################################################################################
	public native void fluidsynthGetPresetName_to_ChooseActivity(String soundfontPath);


}
