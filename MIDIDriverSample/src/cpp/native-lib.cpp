#include <jni.h>
#include <string>
#include <fluidsynth.h>
#include <unistd.h>
#include <stdio.h>
#include <list>
using namespace std;



fluid_settings_t *temp_settings;
fluid_synth_t *temp_synth;
fluid_audio_driver_t *temp_adriver;
int sf;
int more=1;



class MyFluidsynth {

private:
    fluid_settings_t *settings;
    fluid_synth_t *synth;
    fluid_audio_driver_t *audiodriver;
    fluid_sfont_t *pSoundFont;
    fluid_preset_t *preset;

    int usbId;
    bool volumeOff =false;
    const char *presetName;
    int local_velocity = 0;




public:
    MyFluidsynth(JNIEnv *env, jstring jSoundfontPath, jint usbId){

        setUsbId(usbId);
        setDefaultSettings(env,jSoundfontPath);



    }
    MyFluidsynth(JNIEnv *env, jstring jSoundfontPath, jint usbId, jint channel, jint bank, jint progr){

        setUsbId(usbId);
        setDefaultSettings(env,jSoundfontPath);
        setProgramchange(channel,progr,bank);
        setPresetName(progr,bank);




    }
    ~MyFluidsynth(){

        deleteFluidSynth();

    }

    void setVelocity(jint velocity){

        this->local_velocity=velocity;

    }

    void setUsbId(jint usbId){

        this->usbId=usbId;

    }
    int getUsbId(){

        return usbId;
    }
    void setDefaultSettings(JNIEnv *env, jstring jSoundfontPath){

        settings = new_fluid_settings();



        fluid_settings_setstr(settings, "audio.driver", "oboe");
        fluid_settings_setstr(settings, "audio.oboe.performance-mode", "LowLatency");
        fluid_settings_setstr(settings, "audio.oboe.sharing-mode", "Exclusive");
        fluid_settings_setnum(settings,"synth.sample-rate",48000);
        fluid_settings_setint(settings,"audio.periods",2);
        fluid_settings_setint(settings,"audio.period-size",256);
        fluid_settings_setint(settings,"synth.cpu-cores",8);


        synth = new_fluid_synth(settings);

        audiodriver = new_fluid_audio_driver(settings, synth);

        // Load sample soundfont
        const char *soundfontPath = env->GetStringUTFChars(jSoundfontPath, nullptr);
        sf = fluid_synth_sfload(synth, soundfontPath, 1);

    }

    void setRoomsize(float roomsize){


        fluid_settings_setnum(settings,"synth.reverb.room-size",roomsize);
        fluid_settings_setnum(settings,"synth.reverb.level",roomsize);
        fluid_settings_setnum(settings,"synth.reverb.width",roomsize*10);



    }
    void setPresetName(int progr,int bank){

        presetName = getPresetName(progr,bank);
    }

    const char *getPresetName(){


        return presetName;

    }

    const char *getPresetName(int progr, int bank){


        if(synth!=NULL){




            pSoundFont = fluid_synth_get_sfont(synth, 0);
            preset = fluid_sfont_get_preset(pSoundFont, bank, progr);
            return (fluid_preset_get_name(preset));


        }else{


           return "";
        }

    }

    bool getVolumeState(){

        return volumeOff;
    }
    void setVolumeOff(){


        volumeOff = true;



    }

    void setVolumeOn(){

        volumeOff = false;

    }
    void setProgramchange(jint channel,jint program,jint bank){

        fluid_synth_bank_select(synth,channel,bank);
        fluid_synth_program_change(synth, channel, program);

    }


    void onNoteOn(jint channel, jint note,
                  jint velocity,jint usbId){

        if(usbId==this->usbId && volumeOff==false){//maybe it has to be &&

            if(local_velocity>0){

                fluid_synth_noteon( synth, channel, note, local_velocity );

            }else{

                fluid_synth_noteon( synth, channel, note, velocity );
            }

        }else{

            //do nothing

        }

    }

    void onNoteOff(jint channel, jint note,jint usbId){

        if(usbId==this->usbId && volumeOff==false){//maybe it has to be &&


            fluid_synth_noteoff(synth, channel, note);
        }else{

            //do nothing

        }


    }
    void deleteFluidSynth(){

        //preset=nullptr;

        //fluid_synth_sfunload(synth,fluid_sfont_get_id(pSoundFont), true);
        //fluid_synth_remove_sfont(temp_synth,pSoundFont);
        delete_fluid_audio_driver(audiodriver);
        delete_fluid_synth(synth);
        delete_fluid_settings(settings);

        delete presetName;



    }



};

void setJavaText(const char *list);

list <MyFluidsynth*> myfluidsynthList;

//START OF APP WITH SOUNDCHECK
extern "C" JNIEXPORT void JNICALL Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynthHelloWorld(JNIEnv *env, jobject, jstring jSoundfontPath) {
    // Setup synthesizer

    temp_settings = new_fluid_settings();

    fluid_settings_setnum(temp_settings, "temp_synth.reverb.room-size", 0.5);
    fluid_settings_setstr(temp_settings, "audio.driver", "oboe");
    fluid_settings_setstr(temp_settings, "audio.oboe.performance-mode", "LowLatency");
    fluid_settings_setstr(temp_settings, "audio.oboe.sharing-mode", "Exclusive");
    fluid_settings_setnum(temp_settings, "temp_synth.sample-rate", 48000);
    fluid_settings_setint(temp_settings, "audio.periods", 2);
    fluid_settings_setint(temp_settings, "audio.period-size", 256);


    temp_synth = new_fluid_synth(temp_settings);

    temp_adriver = new_fluid_audio_driver(temp_settings, temp_synth);

    // Load sample soundfont
    const char *soundfontPath = env->GetStringUTFChars(jSoundfontPath, nullptr);
    fluid_synth_sfload(temp_synth, soundfontPath, 1);

    fluid_synth_bank_select(temp_synth, 0, 12);
    fluid_synth_program_change(temp_synth, 0, 48);//Orchestra

    fluid_synth_noteon(temp_synth, 0, 70, 127); // play middle B

    sleep(1); // sleep for 1 second
    fluid_synth_noteoff(temp_synth, 0, 70); // stop playing middle B

    fluid_synth_noteon(temp_synth, 0, 65, 127);//F

    sleep(1);
    fluid_synth_noteoff(temp_synth, 0, 65);

    fluid_synth_noteon(temp_synth, 0, 68, 127);//As

    sleep(1);
    fluid_synth_noteoff(temp_synth, 0, 68);

    fluid_synth_noteon(temp_synth, 0, 60, 127);//As

    sleep(1);
    fluid_synth_noteoff(temp_synth, 0, 60);

    fluid_synth_noteon(temp_synth, 0, 58, 127);//B

    sleep(1);
    fluid_synth_noteoff(temp_synth, 0, 58);

    //delete_fluid_audio_driver(temp_adriver);
    //delete_fluid_synth(temp_synth);
    //delete_fluid_settings(temp_settings);

}

extern "C" JNIEXPORT void JNICALL Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynthSendNoteOnMessage(JNIEnv *env, jobject thiz,
                                                                        jint channel, jint note,
                                                                        jint velocity) {

    fluid_synth_noteon(temp_synth, channel, note, velocity );

}

extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynthSendNoteOffMessage(JNIEnv *env, jobject thiz,
                                                                         jint channel, jint note) {

    fluid_synth_noteoff(temp_synth, channel, note);

}

extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynthDeleteSynth(JNIEnv *env, jobject thiz) {

    delete_fluid_audio_driver(temp_adriver);
    delete_fluid_synth(temp_synth);
    delete_fluid_settings(temp_settings);
    //delete_fluid_midi_driver(mdriver);
}

extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynthProgrammChange(JNIEnv *env, jobject thiz,
                                                                     jint programm) {
    fluid_synth_program_change(temp_synth, 0, programm);
}

int ibank = 0;
int iprog = 0;

int const maxPresets = 128 * 128;
int iPresets = 0;

//Get List of all presetnames of loaded Soundfont
extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynthGetPresetName(
        JNIEnv *env, jobject thiz) {
    fluid_preset_t *preset;
    fluid_sfont_t *pSoundFont = fluid_synth_get_sfont(temp_synth, 0);

    while(more) {
        preset = fluid_sfont_get_preset(pSoundFont, ibank, iprog);

        if(iPresets>maxPresets){

            more=0;
            jclass clazz = env->FindClass(
                    "jp/kshoji/driver/midi/sample/MIDIDriverMultipleSampleActivity");

            //jmethodID setInstrmentspinner = env->GetMethodID(clazz, "setInstrmentspinner", "()V");

            jmethodID getPresetnamesFromList = env->GetMethodID(clazz, "getPresetnamesFromList", "()V");
            env->CallVoidMethod(thiz, getPresetnamesFromList);

        }else if(iprog>127){

            iprog=-1;
            ibank++;

        }else if(preset==NULL){

            //do nothing

        }else{

            jint jbank = fluid_preset_get_banknum(preset);
            jint jprog = iprog;
            jstring jstr = env->NewStringUTF(fluid_preset_get_name(preset));

            jclass clazz = env->FindClass(
                    "jp/kshoji/driver/midi/sample/MIDIDriverMultipleSampleActivity");

            jmethodID setListeInstrumenteObjects = env->GetMethodID(clazz,"setListeInstrumenteObjects",
                                                                    "(Ljava/lang/String;II)V");
            //jmethodID setListeInstrumenteNames = env->GetMethodID(clazz, "setListeInstrumenteNames", "(Ljava/lang/String;)V");

            env->CallVoidMethod(thiz, setListeInstrumenteObjects, jstr,jbank,jprog);


        }

        iPresets++;
        iprog++;

    }


}

extern "C"
JNIEXPORT jint JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynthgetMore(JNIEnv *env,
                                                                                     jobject thiz) {
    // TODO: implement fluidsynthgetMore()

    return (jint)more;
}


//TODO: kann gelöscht werden
extern "C"
JNIEXPORT jint JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_startFluidsynth_1Add_1Synth(
        JNIEnv *env, jobject thiz, jstring soundfont_path, jint usb_id) {


    myfluidsynthList.push_back(new MyFluidsynth(env,soundfont_path,usb_id));

    if(myfluidsynthList.empty()){

        return 0;
    }else{

        return 1;
    }


}extern "C"
JNIEXPORT jint JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_deleteFluidsynth_1Synth_1List(
        JNIEnv *env, jobject thiz) {


    if(myfluidsynthList.empty()){



    }else {


        list<MyFluidsynth *>::iterator it;
        for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); it++) {


            it.operator*()->deleteFluidSynth();

            it = myfluidsynthList.erase(it);
            delete *it;

        }
        myfluidsynthList.clear();

    }

    jstring str = env->NewStringUTF("Liste gelöscht!");
    jclass clazz = env->FindClass(
            "jp/kshoji/driver/midi/sample/MIDIDriverMultipleSampleActivity");

    jmethodID setText = env->GetMethodID(clazz,"testToast",
                                         "(Ljava/lang/String;)V");



    env->CallVoidMethod(thiz, setText, str);
    //TODO: liste löschen
}

//List Programchange



extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_Fluidsynth_1Synth_1List_1ProgramChange(
        JNIEnv *env, jobject thiz, jint channel, jint bank, jint progr) {

    list<MyFluidsynth*>::iterator it;
    for(it = myfluidsynthList.begin();it != myfluidsynthList.end();it++){




        it.operator*()->setProgramchange(channel,progr,bank);

    }


}

extern "C"
JNIEXPORT jint JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_startFluidsynth_1Add_1Synth_1to_1List(
        JNIEnv *env, jobject thiz, jstring soundfont_path, jint usb_id, jint channel, jint bank,
        jint progr) {

    myfluidsynthList.push_back(new MyFluidsynth(env,soundfont_path,usb_id,channel,bank,progr));

    if(myfluidsynthList.empty()){

        return 0;
    }else{

        return 1;
    }


}

extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynth_1ListSendNoteOnMessage(
        JNIEnv *env, jobject thiz, jint channel, jint note, jint velocity, jint i_usb_id) {
    // TODO: implement fluidsynth_ListSendNoteOnMessage()


    list<MyFluidsynth*>::iterator it;
    for(it = myfluidsynthList.begin();it != myfluidsynthList.end();it++){


        it.operator*()->onNoteOn(channel,note,velocity,i_usb_id);

    }


}
extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynth_1ListSendNoteOffMessage(
        JNIEnv *env, jobject thiz, jint channel, jint note, jint i_usb_id) {
    // TODO: implement fluidsynth_ListSendNoteOffMessage()

    list<MyFluidsynth*>::iterator it;
    for(it = myfluidsynthList.begin();it != myfluidsynthList.end();it++){



        it.operator*()->onNoteOff(channel,note,i_usb_id);

    }

}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynth_1ListSetInstrumentMuteOnOff(
        JNIEnv *env, jobject thiz, jint channel, jint i_usb_id, jstring str_presetname) {
    // TODO: implement fluidsynth_ListSetInstrumentMuteOnOff()



    jclass clazz = env->FindClass(
            "jp/kshoji/driver/midi/sample/MIDIDriverMultipleSampleActivity");


    jmethodID setInstrMuteOn = env->GetMethodID(clazz,"setInstrumentMuteOn",
                                         "()V");


    jmethodID setInstrMuteOff = env->GetMethodID(clazz,"setInstrumentMuteOff",
                                                    "()V");



    list<MyFluidsynth*>::iterator it;
    for(it = myfluidsynthList.begin();it != myfluidsynthList.end();it++){


        //jstring jstr = env->NewStringUTF("hellofrommuteonoff");

        const char *getPresetNameFromJava = env->GetStringUTFChars(str_presetname, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava,getPresetNameFromInstrList)==0 && i_usb_id ==listUsbId){

            bool volumeStateMuteOn = it.operator*()->getVolumeState();
            if(volumeStateMuteOn == false){

                it = myfluidsynthList.erase(it);
                //it.operator*()->setVolumeOff();


                env->CallVoidMethod(thiz, setInstrMuteOn);

                //setInstrumentMuteOn

            }else if(volumeStateMuteOn == true){

                it.operator*()->setVolumeOn();

                env->CallVoidMethod(thiz, setInstrMuteOff);


            }
        }


    }



}

void setJavaText(const char *text) {

    JNIEnv *env;

    jstring jstr = env->NewStringUTF(text);

    jclass clazz = env->FindClass(
            "jp/kshoji/driver/midi/sample/MIDIDriverMultipleSampleActivity");

    jmethodID setText = env->GetMethodID(clazz,"testToast",
                                         "(Ljava/lang/String;)V");
    //jmethodID setListeInstrumenteNames = env->GetMethodID(clazz, "setListeInstrumenteNames", "(Ljava/lang/String;)V");

    jobject thiz;
    env->CallVoidMethod(thiz, setText, jstr);



}

extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_Fluidsynth_1Synth_1List_1RoomsizeChange(
        JNIEnv *env, jobject thiz, jfloat roomsize) {


    list<MyFluidsynth*>::iterator it;
    for(it = myfluidsynthList.begin();it != myfluidsynthList.end();it++){



        it.operator*()->setRoomsize(roomsize);


    }

}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynth_1ListDeleteInstrument(
        JNIEnv *env, jobject thiz, jint channel, jint i_usb_id, jstring str_presetname) {

    jclass clazz = env->FindClass(
            "jp/kshoji/driver/midi/sample/MIDIDriverMultipleSampleActivity");


    jmethodID setInstrMuteOn = env->GetMethodID(clazz,"setInstrumentMuteOn",
                                                "()V");

    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); it++) {

        const char *getPresetNameFromJava = env->GetStringUTFChars(str_presetname, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            i_usb_id == listUsbId) {

            it = myfluidsynthList.erase(it);
            env->CallVoidMethod(thiz, setInstrMuteOn);


        }
    }
}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynthListSetVelocity(
        JNIEnv *env, jobject thiz, jint global_channel, jint temp_usb_id, jstring temp_preset_name,
        jint int_veloc) {

    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); it++) {

        const char *getPresetNameFromJava = env->GetStringUTFChars(temp_preset_name, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            temp_usb_id == listUsbId) {

            it.operator*()->setVelocity(int_veloc);
            //myfluidsynthList.erase(it);
            //env->CallVoidMethod(thiz, setInstrMuteOn);


        }
    }

}extern "C"
JNIEXPORT jint JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynth_1ListDeleteInstrumentFinal(
        JNIEnv *env, jobject thiz, jint channel, jint i_usb_id, jstring str_presetname) {
    // TODO: implement fluidsynth_ListDeleteInstrumentFinal()


    //jclass clazz = env->FindClass("jp/kshoji/driver/midi/sample/MIDIDriverMultipleSampleActivity");


    //jstring jstr = env->NewStringUTF("Instrument deleted");
    //jmethodID setInstrMuteOn = env->GetMethodID(clazz,"setInstrumentMuteOn","()V";
    //jmethodID setText = env->GetMethodID(clazz,"testToast","(Ljava/lang/String;)V");



    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); it++) {

        const char *getPresetNameFromJava = env->GetStringUTFChars(str_presetname, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            i_usb_id == listUsbId) {

            it = myfluidsynthList.erase(it);
            //env->CallVoidMethod(thiz, setText,jstr);
            return 1;


        } else{

            return 0;
        }


    }


}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_InstrumentChooseActivity_fluidsynthGetPresetName_1to_1ChooseActivity(
        JNIEnv *env, jobject thiz, jstring soundfont_path) {

    fluid_preset_t *preset;
    fluid_sfont_t *pSoundFont = fluid_synth_get_sfont(temp_synth, 0);

    while(more) {
        preset = fluid_sfont_get_preset(pSoundFont, ibank, iprog);

        if(iPresets>maxPresets){


            more=0;
            jclass clazz = env->FindClass(
                    "jp/kshoji/driver/midi/sample/InstrumentChooseActivity");



            jmethodID getPresetnamesFromList = env->GetMethodID(clazz, "getPresetnamesFromList", "()V");
            env->CallVoidMethod(thiz, getPresetnamesFromList);

        }else if(iprog>127){

            iprog=-1;
            ibank++;

        }else if(preset==NULL){

            //do nothing

        }else{

            jint jbank = fluid_preset_get_banknum(preset);
            jint jprog = iprog;
            jstring jstr = env->NewStringUTF(fluid_preset_get_name(preset));

            jclass clazz = env->FindClass(
                    "jp/kshoji/driver/midi/sample/InstrumentChooseActivity");

            jmethodID setListeInstrumenteObjects = env->GetMethodID(clazz,"setListeInstrumenteObjects",
                                                                    "(Ljava/lang/String;II)V");


            env->CallVoidMethod(thiz, setListeInstrumenteObjects, jstr,jbank,jprog);


        }

        iPresets++;
        iprog++;

    }
}