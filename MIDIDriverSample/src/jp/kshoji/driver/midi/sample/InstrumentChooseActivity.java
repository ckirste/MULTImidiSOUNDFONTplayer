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
	String selSoundfont;


	//public InputStream is_temp = null;
	
	//private final String SOUNDFONT_DIRECTORY="MULTImidiSOUNDFONTplayer/SOUNDFONTS";
	
	ArrayList<SF2Preset>listeSF2Presets=new ArrayList<>();

	ArrayList<String> listeInstrumenteNames=new ArrayList<String>();
	
	ArrayList<String> selectedInstrumentList = new ArrayList<String>();

	ArrayList<String> instrumentList = new ArrayList<String>();
	ArrayAdapter<String> listedInstrumentsAdapter;

	ArrayList<String> soundfontList = new ArrayList<String>();
	ArrayAdapter<String> listedSoundfontsAdapter;

	ListView lvInstruments;
	ListView lvSoundfonts;

	ListView lvPercussion;
	TextView txtvInstr;

	Button btnOk;
	Button btnCancel;

	int requestCode;

	File fi;
	Button btnReturn;
	private final String APP_DATA_DIRECTORY = "MULTImidiSOUNDFONTplayer";

	private final String SOUNDFONT_DIRECTORY="SOUNDFONTS";

	private final String SETTINGS_DIRECTORY="Settings";

	String ext;

	private boolean sf2FileSelected=false;

	private File sdcard = null;


	private File myDirSoundfonts;

	private File myDirSettings;

	ArrayAdapter<String> listedPercussionAdapter;
	private String[] arrPerc={
			/*35*/"Acoustic Bass Drum",
			"Electric Bass Drum",
			"Side Stick",
			"Acoustic Snare",
			"Hand Clap",
			"Electric Snare",
			"Low Floor Tom",
			"Closed Hi-hat",
			"High Floor Tom",
			"Pedal Hi-hat",
			"Low Tom",
			"Open Hi-hat",
			"Low-Mid Tom",
			"Hi-Mid Tom",
			"Crash Cymbal 1",
			"High Tom",
			"Ride Cymbal 1",
			"Chinese Cymbal",
			"Ride Bell",
			"Tambourine",
			"Splash Cymbal",
			"Cowbell",
			"Crash Cymbal 2",
			"Vibraslap",
			"Ride Cymbal 2",
			"High Bongo",
			"Low Bongo",
			"Mute High Conga",
			"Open High Conga",
			"Low Conga",
			"High Timbale",
			"Low Timbale",
			"High Agogô",
			"Low Agogô",
			"Cabasa",
			"Maracas",
			"Short Whistle",
			"Long Whistle",
			"Short Guiro",
			"Long Guiro",
			"Claves",
			"High Woodblock",
			"Low Woodblock",
			"Mute Cuica",
			"Open Cuica",
			"Mute Triangle",
			"Open Triangle"/*81*/};

	List<String> arrListPerc;

	String selPercussionIns="";

	//addional to index in List
	int drumKey=35;

	private boolean percussionClicked=false;



	private String pathOfLastFile="";

	String myLastPath = "myLastPath";

	private File fileOfLastPath;

	private int drumKeySel;
	private int loadeddrums;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.chooseinstrument_layout);
		//testToast(""+listeInstrumenteNames.size());

		btnReturn = (Button)findViewById(R.id.btnParentfile);

		arrListPerc = Arrays.asList(arrPerc);

		txtvInstr = (TextView) findViewById(R.id.txtvInstr);

		loadeddrums = getIntent().getIntExtra("drums",0);



		/*
		if(listeInstrumenteNames.isEmpty()){

			testToast("Liste ist leer");

		}

		*/

		lvPercussion = (ListView) findViewById(R.id.percussionList);
		listedPercussionAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, arrListPerc);
		lvPercussion.setChoiceMode(lvPercussion.CHOICE_MODE_SINGLE);

		lvPercussion.setOnItemClickListener(new OnItemClickListener(){


			@Override
			public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
			{


				percussionClicked=true;

				selPercussionIns = p1.getItemAtPosition(p3).toString();
				drumKeySel= p3;
				//testToast(""+drumKeySel);


			}





		});

		lvInstruments = (ListView) findViewById(R.id.instrumentList);
		lvInstruments.setChoiceMode(lvInstruments.CHOICE_MODE_MULTIPLE);
		lvInstruments.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{

					String ins = p1.getItemAtPosition(p3).toString();

					if(ins.contains("Drumkit") || ins.contains("Drums") || ins.contains("Perc.")){

						lvPercussion.setAdapter(listedPercussionAdapter);


					}

					//selectedInstrumentList.add(ins);


				}

		});





		lvSoundfonts = (ListView) findViewById(R.id.soundfontList);
		lvSoundfonts.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{

					File sfFile = null;

					listeSF2Presets.clear();
					listeInstrumenteNames.clear();
					selSoundfont = "";
					selSoundfont = p1.getItemAtPosition(p3).toString();


					ext = getExt(selSoundfont);

					//testToast("ext: "+ext);
					if(ext != null && ext.compareToIgnoreCase("sf2")==0){
						// do anything

						sf2FileSelected=true;
						sfFile = new File(btnReturn.getText().toString()+"/"+selSoundfont);


						selSoundfont = sfFile.getAbsolutePath();
						//testToast("Liste Instrumente geladen");
						getInstrumentsFromNativeList(selSoundfont);//selSoundfont);


						/*
						try
						{
							//testToast("Soundfont selected");

							setListeFromSoundfondbank(sfFile);
							testToast("Liste Instrumente geladen");
						}
						catch (IOException e)
						{}

						*/
						if(listeInstrumenteNames.isEmpty()){

							listedInstrumentsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, soundfontList);

						}else{

							listedInstrumentsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, listeInstrumenteNames);

						}

						lvInstruments.setAdapter(listedInstrumentsAdapter);

					}else{

						if(ext == null /*&& !*/){

							listeInstrumenteNames.clear();
							soundfontList.clear();
							sfFile = new File(btnReturn.getText().toString()+"/"+selSoundfont);
							listFiles(sfFile);
						}else{

							//do nothing if no sf2 was selected


						}


					}

				}

		});


		btnReturn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View p1)
			{
				Button btn = (Button) p1;
				//File internalStorage = Environment.getExternalStorageDirectory();

				if(btn.getText().toString().compareToIgnoreCase("/storage")==0 || btn.getText().toString().compareToIgnoreCase("/storage/emulated/0")==0){
					listeInstrumenteNames.clear();
					soundfontList.clear();
					listFiles(null);
					//testToast("if1");Environment.getExternalStorageDirectory().getParentFile()

				}/*else if((fi.compareTo(internalStorage.getParentFile())==0) || fi.compareTo(internalStorage)==0){

							//testToast("if2: "+fi.getName());
							listeInstrumenteNames.clear();
							soundfontList.clear();
							//do nothing
							listFiles(null);

					}else if(sdcard!=null && fi.compareTo(sdcard)==0){

						listeInstrumenteNames.clear();
						soundfontList.clear();
						listFiles(null);

					}*/else{


					listeInstrumenteNames.clear();
					soundfontList.clear();
					//testToast("if3");
					listFiles(fi.getParentFile());

				}

			}





		});
		try
		{
			createDir();

			try
			{
				readFile();
			}
			catch (IOException e)
			{

				testToast("Problem"+e.getMessage());


			}

			if(pathOfLastFile.toString()!=""){



				fi=new File(pathOfLastFile);
			}


			//ordnerstruktur anzeigen
			if(fi==null){

				//testToast("fi null");
				listFiles(null);

			}else{

				//testToast("fi not null");
				listFiles(fi);
			}

		}
		finally
		{


		}

		btnOk = (Button) findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{

					requestCode = RESULT_OK;
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

					//finish();
				}

		});


	}

	public String getExt(String filePath){
		int strLength = filePath.lastIndexOf(".");
		if(strLength > 0)
			return filePath.substring(strLength + 1).toLowerCase();
		return null;
	}

	private void readFile() throws IOException{

		fileOfLastPath=  new File(myDirSettings, myLastPath);

		FileInputStream is;// = getAssets().open(usbDeviceName);

		BufferedReader reader;



		if (fileOfLastPath.exists()) {

			is = new FileInputStream(fileOfLastPath);

			reader = new BufferedReader(new InputStreamReader(is));


			pathOfLastFile = reader.readLine();

			// testToast("readline: "+pathOfLastFile);
			is.close();

			reader.close();



		}
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

	}//createdir

	private void listFiles(File fil)
	{
		fi=fil;

		if(fi!=null && fi.getAbsolutePath().compareToIgnoreCase("/storage")==0){

			fi=null;

		}

		File internalStorage = Environment.getExternalStorageDirectory();

		File innerDir = Environment.getExternalStorageDirectory().getParentFile();

		File rootDirectory;

		if(fi!=null){

			rootDirectory = fi;

			for(File f: rootDirectory.listFiles()){


				if(f.getName().startsWith(".")){


				}else{
					soundfontList.add(f.getName());
				}
				//soundfontList.add(f.getName());

			}


		}else{

			rootDirectory = innerDir.getParentFile();
			File[] files = rootDirectory.listFiles();

			Log.d("Files", "Size: " + files.length);

			if(files[0].compareTo(innerDir)!=0){

				sdcard = files[0];

				soundfontList.add(files[0].getName());
				soundfontList.add(innerDir.getName()+"/"+ internalStorage.getName());

			}
		}

		String path = rootDirectory.getAbsolutePath();

		btnReturn.setText(""+path);

		listedSoundfontsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, soundfontList);
		lvSoundfonts.setAdapter(listedSoundfontsAdapter);

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

		Log.d("end of listFiles", "listFiles: ");


	}
	public void testToast(String s)
	{

		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();


	}

	private void setListToMainActivity()
	{


		for(int i=0;i<lvInstruments.getCount();i++){


			if(lvInstruments.isItemChecked(i)){

				selectedInstrumentList.add(lvInstruments.getItemAtPosition(i).toString());


			}

		}



		finish();

	}

	@Override
	public void finish()
	{



		Intent data = new Intent();
		data.putExtra("selectedInstrumentList", selectedInstrumentList);
		data.putExtra("soundfontpath", selSoundfont);
		data.putExtra("listeSF2Presets",listeSF2Presets);

		if(percussionClicked){

			//selPercussionIns = lvPercussion.getSelectedItem().toString();

			drumKey = drumKeySel+35;
			data.putExtra("drumName",selPercussionIns);

			data.putExtra("drumKey",drumKey);






		}

		setResult(requestCode, data);

		try
		{
			savePath();
		}
		catch (IOException e)
		{}

		super.finish();




	}

	private void savePath() throws FileNotFoundException, IOException{


		String lastPath = btnReturn.getText().toString();



		//testToast("lastpath: "+lastPath);


		FileOutputStream stream = new FileOutputStream(fileOfLastPath);


		stream.write((lastPath).getBytes());

		stream.close();




	}

	//wird nicht benötigt
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private String copyExtFileToTmpFile(String fileName, File fileObjekt) {


		//testToast("copyextfile start bevore try "+fileName);
		try (InputStream is = new FileInputStream(fileObjekt)) {
			String tempFileName = "tmp_" + fileName;
			//testToast("copyextfile start"+tempFileName);

			try (FileOutputStream fos = openFileOutput(tempFileName, Context.MODE_PRIVATE)) {
				int bytes_read;
				byte[] buffer = new byte[4096];
				while ((bytes_read = is.read(buffer)) != -1) {
					fos.write(buffer, 0, bytes_read);
				}
			}
			Log.d("return copyExtFile", getFilesDir() + "/" + tempFileName);
			return getFilesDir() + "/" + tempFileName;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			//testToast(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			//testToast(e.getMessage());
		}

		return "";
	}


	private void getInstrumentsFromNativeList(String selSoundfontpath)
	{

		fluidsynthGetPresetName_to_ChooseActivity(selSoundfontpath);
		
		
		
	}
	
	public void getPresetnamesFromList() throws InterruptedException {



        for(SF2Preset sf2presetName: listeSF2Presets){

        	if(loadeddrums==1){

        		/*
        		if(sf2presetName.getPresetbank()>127){


					listeInstrumenteNames.add(sf2presetName.getPresetname());



				}

        		*/
				if(sf2presetName.getPresetbank()>=120){
					if (sf2presetName.getPresetname().contains("Drumkit") || sf2presetName.getPresetname().contains("Drums")
							|| sf2presetName.getPresetname().contains("Perc.")) {


						//testToast("" + sf2presetName.getPresetbank());

						listeInstrumenteNames.add(sf2presetName.getPresetname());

					}
				}





        	}else{

				listeInstrumenteNames.add(sf2presetName.getPresetname());

			}



            //txtvInstr.setText(""+sf2presetName.getPresetname());
            //Thread.sleep(100);


        }



    }

    public void setListeInstrumenteObjects(String presetname,int ibank, int iprogr) throws InterruptedException {



        listeSF2Presets.add(new SF2Preset(presetname,ibank,iprogr));
		//txtvInstr.setText(""+presetname);
		//Thread.sleep(100);
		//testToast(" setListeInstrumenteObjects from native-lib");

    }
	
	
	
	//#############################################################################################
	public native void fluidsynthGetPresetName_to_ChooseActivity(String soundfontPath);


}
