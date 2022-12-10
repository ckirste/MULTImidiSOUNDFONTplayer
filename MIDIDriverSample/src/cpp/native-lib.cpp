#include <jni.h>
#include <string>
#include <fluidsynth.h>
#include <unistd.h>
#include <stdio.h>
#include <list>
using namespace std;

/*int ibank = 0;
int iprog = 0;

int const maxPresets = 128 * 128;
int iPresets = 0;*/

fluid_settings_t *temp_settings;
fluid_synth_t *local_synth;
fluid_audio_driver_t *temp_adriver;
//int sf;
//int more=1;

float chorusDepth=0.0;
float chorusLevel=0.0;
int chorusNr=0;
float chorusSpeed=0.0;

float reverbDamp=0.2;
float reverbLevel=0.5;
float reverbRoomSize=0.9;
float reverbWidth=0.7;


class MyFluidsynth {

private:

    //fluid_ladspa_fx_t *fx;
    fluid_settings_t *settings;
    fluid_synth_t *synth = NULL;
    fluid_audio_driver_t *audiodriver = NULL;
    fluid_sfont_t *pSoundFont;
    fluid_preset_t *preset;

    int transpo=0;
    int usbId;
    bool volumeOff =false;
    const char *presetName;
    //int local_velocity = 0;
    int local_velocity=127;
    bool boolFixedVelocity=true;

    bool boolSplitNotes=false;

    bool boolMuteNotesSmalerThan=false;
    bool boolMuteNotesGreaterThan=false;

    int local_note;



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

    void setBoolSplitNotes(jboolean spliteNotes){

        this->boolSplitNotes = spliteNotes;
    }

    void setBoolMuteNotesSmalerThan(jboolean muteNotesSmalerThan){

        this->boolMuteNotesSmalerThan=muteNotesSmalerThan;

    }

    void setBoolMuteNotesGreaterThan(jboolean muteNotesGreaterThan){

        this->boolMuteNotesGreaterThan=muteNotesGreaterThan;

    }


    void setVelocity(jint velocity){

        this->local_velocity=velocity;

    }

    int getLocal_velocity(int intVelocity)
    {

        if(boolFixedVelocity==true){

            return local_velocity;

        }else{

            if(intVelocity>=local_velocity){


                return local_velocity;

            }else{

                return intVelocity;

            }
        }
    }

    void setBoolFixedVelocity(bool boolFixedVelocity)
    {
        this->boolFixedVelocity = boolFixedVelocity;
    }
    bool isBoolFixedVelocity()
    {
        return boolFixedVelocity;
    }

    void setUsbId(jint usbId){

        this->usbId=usbId;

    }
    int getUsbId(){

        return usbId;
    }

    void setSynthSettings(JNIEnv *env,jstring synth_setting,jfloat synth_setting_value){


        const char *settStr = env->GetStringUTFChars(synth_setting, nullptr);

        float fl_synth_setting_value = synth_setting_value;

        fluid_settings_setnum(settings,settStr,fl_synth_setting_value);


    }

    void setSynthSettingsChorusNr(JNIEnv *env,int synth_setting_value){


        //const char *settStr = env->GetStringUTFChars(synth_setting, nullptr);

//        int int_synth_setting_value = synth_setting_value;


        chorusNr=synth_setting_value;
        fluid_settings_setint(settings,"synth.chorus.nr",synth_setting_value);


    }

    void setSynthSettingsChorusDepth(JNIEnv *env,float synth_setting_value){


        //const char *settStr = env->GetStringUTFChars(synth_setting, nullptr);

        //int int_synth_setting_value = synth_setting_value;

        chorusDepth=synth_setting_value;
        fluid_settings_setnum(settings,"synth.chorus.depth",(double)synth_setting_value);


    }

    void setSynthSettingsChorusLevel(JNIEnv *env,float synth_setting_value){


        //const char *settStr = env->GetStringUTFChars(synth_setting, nullptr);

        //int int_synth_setting_value = synth_setting_value;

        chorusLevel=synth_setting_value;
        fluid_settings_setnum(settings,"synth.chorus.level",(double)synth_setting_value);


    }

    void setSynthSettingsChorusSpeed(JNIEnv *env,float synth_setting_value){


        //const char *settStr = env->GetStringUTFChars(synth_setting, nullptr);

        //int int_synth_setting_value = synth_setting_value;

        chorusSpeed=synth_setting_value;
        fluid_settings_setnum(settings,"synth.chorus.speed",(double)synth_setting_value);


    }

    void setSynthSettingsReverbLevel(JNIEnv *env,float synth_setting_value){


        //const char *settStr = env->GetStringUTFChars(synth_setting, nullptr);

        //int int_synth_setting_value = synth_setting_value;

        reverbLevel=synth_setting_value;
        fluid_settings_setnum(settings,"synth.reverb.level",(double)synth_setting_value);


    }

    void setSynthSettingsReverbDamp(JNIEnv *env,float synth_setting_value){


        //const char *settStr = env->GetStringUTFChars(synth_setting, nullptr);

        //int int_synth_setting_value = synth_setting_value;

        reverbDamp=synth_setting_value;
        fluid_settings_setnum(settings,"synth.reverb.damp",(double)synth_setting_value);


    }

    void setSynthSettingsReverbRS(JNIEnv *env,float synth_setting_value){


        //const char *settStr = env->GetStringUTFChars(synth_setting, nullptr);

        //int int_synth_setting_value = synth_setting_value;

        reverbRoomSize=synth_setting_value;

        fluid_settings_setnum(settings,"synth.reverb.room-size",(double)synth_setting_value);


    }

    void setSynthSettingsReverbWidth(JNIEnv *env,float synth_setting_value){


        //const char *settStr = env->GetStringUTFChars(synth_setting, nullptr);

        //int int_synth_setting_value = synth_setting_value;

        reverbWidth=synth_setting_value;
        fluid_settings_setnum(settings,"synth.reverb.width",(double)synth_setting_value);


    }




    void setSynthSettingsReverbActive(){

        fluid_settings_setint(settings,"synth.reverb.active", 1);



    }

    void setSynthSettingsChorusActive(){

        fluid_settings_setint(settings,"synth.chorus.active", 1);


    }

    void setDefaultSettings(JNIEnv *env, jstring jSoundfontPath){

        settings = new_fluid_settings();
        int sf;


        int coreCount=_SC_NPROCESSORS_CONF;
        //int core = android_getCpuCount();

        fluid_settings_setnum(settings,"synth.chorus.depth",chorusDepth);
        fluid_settings_setint(settings,"synth.chorus.nr",chorusNr);
        fluid_settings_setnum(settings,"synth.chorus.level",chorusLevel);
        fluid_settings_setnum(settings,"synth.chorus.speed",chorusSpeed);

        fluid_settings_setnum(settings,"synth.reverb.damp",reverbDamp);
        fluid_settings_setnum(settings,"synth.reverb.room-size",reverbRoomSize);
        fluid_settings_setnum(settings,"synth.reverb.level",reverbLevel);
        fluid_settings_setnum(settings,"synth.reverb.width",reverbWidth);


        fluid_settings_setstr(settings, "audio.driver", "oboe");
        fluid_settings_setstr(settings, "audio.oboe.performance-mode", "LowLatency");
        fluid_settings_setstr(settings, "audio.oboe.sharing-mode", "Exclusive");
        fluid_settings_setnum(settings,"synth.sample-rate",48000);
        fluid_settings_setint(settings,"audio.periods",2);
        fluid_settings_setint(settings,"audio.period-size",256);
        fluid_settings_setint(settings,"synth.cpu-cores",coreCount);


        synth = new_fluid_synth(settings);

        audiodriver = new_fluid_audio_driver(settings, synth);

        // Load sample soundfont
        const char *soundfontPath = env->GetStringUTFChars(jSoundfontPath, nullptr);
        sf = fluid_synth_sfload(synth, soundfontPath, 1);

        /*
        fx = fluid_synth_get_ladspa_fx(synth);
        int fluidLadspa = fluid_ladspa_activate(fx);
        if(fluidLadspa){

            jclass clazz = env->FindClass("jp/kshoji/driver/midi/sample/MIDIDriverMultipleSampleActivity");

            jstring jstr = env->NewStringUTF("ladspa activated");

            jmethodID setText = env->GetMethodID(clazz,"testToast","(Ljava/lang/String;)V");

            jobject obj = env->AllocObject(clazz);
            env->CallVoidMethod(obj, setText,jstr);

            int fluidEffect = fluid_ladspa_add_effect(fx,"e1","/usr/lib/ladspa/delay.so","delay");

            if(fluidEffect){

                fluid_ladspa_add_buffer(fx,"fxbuffer");
                int fluidLink = fluid_ladspa_effect_link(fx,"e1","Output","fxbuffer");
                if(fluidLink){

                    fluid_ladspa_effect_set_control(fx,"e1","Output",1.5);

                }

            }
        }
        */

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


        if(usbId==this->usbId){
            if(boolSplitNotes){

                local_note = note;
                boolSplitNotes = false;
            }

            int transp=transpo;
            int velo = getLocal_velocity(velocity);

            if(boolMuteNotesGreaterThan){

                if(note>local_note){



                }else {

                    fluid_synth_noteon(synth, channel, note + transp, velo);

                }
            }else if(boolMuteNotesSmalerThan){

                if(note<local_note){



                }else{

                    fluid_synth_noteon(synth, channel, note + transp, velo);
                }

            }else{

                fluid_synth_noteon(synth, channel, note + transp, velo);
            }


        }else{

            //do nothing

        }

    }

    void onNoteOff(jint channel, jint note,jint usbId){

        if(usbId==this->usbId){


            if(boolSplitNotes){

                local_note = note;
                boolSplitNotes = false;
            }
            int transp=transpo;

            if(boolMuteNotesGreaterThan){

                if(note>local_note){



                }else {

                    fluid_synth_noteoff(synth, channel, note + transp);

                }
            }else if(boolMuteNotesSmalerThan){

                if(note<local_note){



                }else{

                    fluid_synth_noteoff(synth, channel, note + transp);
                }

            }else{

                fluid_synth_noteoff(synth, channel, note + transp);
            }


            fluid_synth_noteoff(synth, channel, note + transp);




        }else{

            //do nothing

        }


    }

    void settTranspo(int transpo){


        this->transpo=transpo;



    }

    void deleteFluidSynth(){

        if(audiodriver){



            delete_fluid_audio_driver(audiodriver);

        }
        if(synth){


            delete_fluid_synth(synth);

        }
        if(settings){

            delete_fluid_settings(settings);

        }




        //delete presetName;



    }



};

void setJavaText(const char *list);

list <MyFluidsynth*> myfluidsynthList;

//START OF APP WITH SOUNDCHECK
extern "C" JNIEXPORT void JNICALL Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynthHelloWorld(JNIEnv *env, jobject, jstring jSoundfontPath) {
    // Setup synthesizer

    fluid_settings_t *temp_settings;
    fluid_synth_t *temp_synth;
    fluid_audio_driver_t *temp_adriver;

    temp_settings = new_fluid_settings();

    fluid_settings_setnum(temp_settings, "synth.reverb.room-size", 0.5);
    fluid_settings_setstr(temp_settings, "audio.driver", "oboe");
    fluid_settings_setstr(temp_settings, "audio.oboe.performance-mode", "LowLatency");
    fluid_settings_setstr(temp_settings, "audio.oboe.sharing-mode", "Exclusive");
    fluid_settings_setnum(temp_settings, "synth.sample-rate", 48000);
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

    delete_fluid_audio_driver(temp_adriver);
    delete_fluid_synth(temp_synth);
    delete_fluid_settings(temp_settings);

}

extern "C" JNIEXPORT void JNICALL Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynthSendNoteOnMessage(JNIEnv *env, jobject thiz,
                                                                        jint channel, jint note,
                                                                        jint velocity) {

    //fluid_synth_noteon(local_synth, channel, note, velocity );

}

extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynthSendNoteOffMessage(JNIEnv *env, jobject thiz,
                                                                         jint channel, jint note) {

    //fluid_synth_noteoff(local_synth, channel, note);

}

extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynthDeleteSynth(JNIEnv *env, jobject thiz) {

    //delete_fluid_audio_driver(temp_adriver);
    //delete_fluid_synth(local_synth);
    //delete_fluid_settings(temp_settings);
    //delete_fluid_midi_driver(mdriver);
}

extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynthProgrammChange(JNIEnv *env, jobject thiz,
                                                                     jint programm) {
    //fluid_synth_program_change(local_synth, 0, programm);
}





extern "C"
JNIEXPORT jint JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynthgetMore(JNIEnv *env,
                                                                                     jobject thiz) {
    // TODO: implement fluidsynthgetMore()

    return 1;
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


}

extern "C"
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
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); ++it) {

        const char *getPresetNameFromJava = env->GetStringUTFChars(str_presetname, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            i_usb_id == listUsbId) {

            it.operator*()->deleteFluidSynth();

            it = myfluidsynthList.erase(it);
            break;

            //env->CallVoidMethod(thiz, setInstrMuteOn);


        }
    }
    env->CallVoidMethod(thiz, setInstrMuteOn);
}

extern "C"
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

}

extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynthListSetVelocity_1For_1All(
        JNIEnv *env, jobject thiz, jint global_channel, jint usb_device_id, jint int_velocity) {
    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); it++) {


        int listUsbId = it.operator*()->getUsbId();

        if (usb_device_id == listUsbId) {

            it.operator*()->setVelocity(int_velocity);



        }
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynthListsetFixedVel_1For_1All(
        JNIEnv *env, jobject thiz, jint channel, jint i_usb_id, jboolean fixed) {

    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); it++) {


        int listUsbId = it.operator*()->getUsbId();

        if (i_usb_id == listUsbId) {

            it.operator*()->setBoolFixedVelocity(fixed);



        }
    }


}

extern "C"
JNIEXPORT jint JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynth_1ListDeleteInstrumentFinal(
        JNIEnv *env, jobject thiz, jint channel, jint i_usb_id, jstring str_presetname) {
    // TODO: implement fluidsynth_ListDeleteInstrumentFinal()


    //jclass clazz = env->FindClass("jp/kshoji/driver/midi/sample/MIDIDriverMultipleSampleActivity");

    //jstring jstr = env->NewStringUTF("Instrument deleted");

    //jmethodID setText = env->GetMethodID(clazz,"testToast","(Ljava/lang/String;)V");

    //env->CallVoidMethod(thiz, setText,jstr);

    list<MyFluidsynth *>::iterator it;

    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); it++) {

        const char *getPresetNameFromJava = env->GetStringUTFChars(str_presetname, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            i_usb_id == listUsbId) {

            it.operator*()->deleteFluidSynth();

            it = myfluidsynthList.erase(it);

            return 1;


        } else{

            return 0;
        }


    }


}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_InstrumentChooseActivity_fluidsynthGetPresetName_1to_1ChooseActivity(
        JNIEnv *env, jobject thiz, jstring soundfont_path) {


    int ibank = 0;
    int iprog = 0;

    int const maxPresets = 128 * 128;
    int iPresets = 0;
    int sf;
    int more=1;
    fluid_settings_t *temp_settings;
    fluid_synth_t *temp_synth;
    fluid_audio_driver_t *temp_adriver;




        temp_settings = new_fluid_settings();
        //fluid_settings_setnum(temp_settings, "synth.reverb.room-size", 0.8);
        fluid_settings_setstr(temp_settings, "audio.driver", "oboe");
        fluid_settings_setstr(temp_settings, "audio.oboe.performance-mode", "LowLatency");
        fluid_settings_setstr(temp_settings, "audio.oboe.sharing-mode", "Exclusive");
        fluid_settings_setnum(temp_settings, "synth.sample-rate", 48000);
        fluid_settings_setint(temp_settings, "audio.periods", 2);
        fluid_settings_setint(temp_settings, "audio.period-size", 256);



    temp_synth = new_fluid_synth(temp_settings);

    temp_adriver = new_fluid_audio_driver(temp_settings, temp_synth);

    // Load sample soundfont
    const char *soundfontPath = env->GetStringUTFChars(soundfont_path, nullptr);

    sf= fluid_synth_sfload(temp_synth, soundfontPath, 1);

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


            //more=0;

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

    delete_fluid_audio_driver(temp_adriver);
    delete_fluid_synth(temp_synth);
    delete_fluid_settings(temp_settings);


}

//Get List of all presetnames of loaded Soundfont
extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynthGetPresetName(
        JNIEnv *env, jobject thiz, jstring soundfont_path) {
    int ibank = 0;
    int iprog = 0;

    int const maxPresets = 128 * 128;
    int iPresets = 0;
    int sf;
    int more=1;
    fluid_settings_t *temp_settings;
    fluid_synth_t *temp_synth;
    fluid_audio_driver_t *temp_adriver;

    temp_settings = new_fluid_settings();

    //fluid_settings_setnum(temp_settings, "synth.reverb.room-size", 0.5);
    fluid_settings_setstr(temp_settings, "audio.driver", "oboe");
    fluid_settings_setstr(temp_settings, "audio.oboe.performance-mode", "LowLatency");
    fluid_settings_setstr(temp_settings, "audio.oboe.sharing-mode", "Exclusive");
    fluid_settings_setnum(temp_settings, "synth.sample-rate", 48000);
    fluid_settings_setint(temp_settings, "audio.periods", 2);
    fluid_settings_setint(temp_settings, "audio.period-size", 256);


    temp_synth = new_fluid_synth(temp_settings);

    temp_adriver = new_fluid_audio_driver(temp_settings, temp_synth);

    // Load sample soundfont
    const char *soundfontPath = env->GetStringUTFChars(soundfont_path, nullptr);

    sf= fluid_synth_sfload(temp_synth, soundfontPath, 1);

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
    delete_fluid_audio_driver(temp_adriver);
    delete_fluid_synth(temp_synth);
    delete_fluid_settings(temp_settings);


}
extern "C"
JNIEXPORT jint JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_deleteFluidsynth_1Synth_1List_1Final(
        JNIEnv *env, jobject thiz) {

    if(myfluidsynthList.empty()){


        return 0;

    }else {


        list<MyFluidsynth *>::iterator it;
        for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); it++) {


            it.operator*()->deleteFluidSynth();

            it = myfluidsynthList.erase(it);
            delete *it;

        }
        myfluidsynthList.clear();

        return 1;

    }

}

extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_util_InstrumentButton_addInstrumentToFluidsynthList(JNIEnv *env,
                                                                                      jobject thiz,
                                                                                      jstring soundfontpath,
                                                                                      jint usb_device_id,
                                                                                      jint channel,
                                                                                      jint bank,
                                                                                      jint programm) {

    myfluidsynthList.push_back(new MyFluidsynth(env,soundfontpath,usb_device_id,channel,bank,programm));

    if(myfluidsynthList.empty()){

        //return 0;
    }


}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_util_InstrumentButton_deleteInstrumentFromFluidsynthList(
        JNIEnv *env, jobject thiz, jint channel, jint i_usb_id, jstring str_presetname) {

    list<MyFluidsynth *>::iterator it;

    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); it++) {

        const char *getPresetNameFromJava = env->GetStringUTFChars(str_presetname, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            i_usb_id == listUsbId) {

            it.operator*()->deleteFluidSynth();

            it = myfluidsynthList.erase(it);
            //env->CallVoidMethod(thiz, setText,jstr);
            //return 1;
            break;



        } else {

            //return 0;
            break;
        }


    }

}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_EffectActivity_setFluidsynthChorusActive(JNIEnv *env,
                                                                           jobject thiz,
                                                                           jint global_channel,
                                                                           jint usb_device_id,
                                                                           jstring instr,
                                                                           jboolean all) {
    // TODO: implement setFluidsynthChorusActive()
    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); ++it) {

        const char *getPresetNameFromJava = env->GetStringUTFChars(instr, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            usb_device_id == listUsbId && all== false) {

            it.operator*()->setSynthSettingsChorusActive();


            break;

        }else if(all){

            it.operator*()->setSynthSettingsChorusActive();


        }
    }



}extern "C"
JNIEXPORT jint JNICALL
Java_jp_kshoji_driver_midi_sample_EffectActivity_setFluidsynthReverbActive(JNIEnv *env,
                                                                           jobject thiz,
                                                                           jint global_channel,
                                                                           jint usb_device_id,
                                                                           jstring instr,
                                                                           jboolean all) {
    // TODO: implement setFluidsynthReverbActive()
    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); ++it) {

        const char *getPresetNameFromJava = env->GetStringUTFChars(instr, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            usb_device_id == listUsbId && all== false) {

            it.operator*()->setSynthSettingsReverbActive();

            return 1;

            break;

        }else if(all){

            it.operator*()->setSynthSettingsReverbActive();

            return 0;

        }
    }




}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_EffectActivity_fluidsynthListSetVelocity(JNIEnv *env,
                                                                           jobject thiz,
                                                                           jint global_channel,
                                                                           jint usb_device_id,
                                                                           jstring instr,
                                                                           jint int_veloc) {
    // TODO: implement fluidsynthListSetVelocity()
    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); it++) {

        const char *getPresetNameFromJava = env->GetStringUTFChars(instr, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            usb_device_id == listUsbId) {


            it.operator*()->setVelocity(int_veloc);
            break;



        }
    }




}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_EffectActivity_fluidsynthListsetFixedVel(JNIEnv *env,
                                                                           jobject thiz,
                                                                           jint channel,
                                                                           jint i_usb_id,jstring instr,
                                                                           jboolean fixed) {
    // TODO: implement fluidsynthListsetFixedVel()




    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); it++) {

        const char *getPresetNameFromJava = env->GetStringUTFChars(instr, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            i_usb_id == listUsbId) {


            it.operator*()->setBoolFixedVelocity(fixed);
            break;



        }
    }



}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_EffectActivity_fluidsynthListSetVelocity_1For_1All(JNIEnv *env,
                                                                                     jobject thiz,
                                                                                     jint global_channel,
                                                                                     jint usb_device_id,
                                                                                     jint int_velocity) {
    // TODO: implement fluidsynthListSetVelocity_For_All()
    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); it++) {

        //const char *getPresetNameFromJava = env->GetStringUTFChars(instr, nullptr);
        //const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (usb_device_id == listUsbId) {


            it.operator*()->setVelocity(int_velocity);
            break;



        }
    }






}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_EffectActivity_Fluidsynth_1Synth_1List_1Synthesizer_1Settings(
        JNIEnv *env, jobject thiz, jint global_channel, jint usb_device_id, jstring instr,
        jstring synth_setting, jfloat synth_setting_value, jboolean all) {
    // TODO: implement Fluidsynth_Synth_List_Synthesizer_Settings()


    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); ++it) {

        const char *getPresetNameFromJava = env->GetStringUTFChars(instr, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            usb_device_id == listUsbId && !all) {

            it.operator*()->setSynthSettings(env,synth_setting,synth_setting_value);


            break;

        }else if(all){

            it.operator*()->setSynthSettings(env,synth_setting,synth_setting_value);


        }
    }



}extern "C"
JNIEXPORT jint JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_fluidsynth_1ListDeleteInstrumentOfUsbId(
        JNIEnv *env, jobject thiz, jint global_channel, jint usb_device_id) {


    list<MyFluidsynth *>::iterator it;


    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); it++) {


        int listUsbId = it.operator*()->getUsbId();

        if (usb_device_id != listUsbId) {



            ///delete *it;

        }else{
            it.operator*()->deleteFluidSynth();

            it = myfluidsynthList.erase(it);



        }
/*
        if (usb_device_id == listUsbId) {

            it.operator*()->deleteFluidSynth();

            it = myfluidsynthList.erase(it--);

            ///delete *it;

        }

*/




    }



    return 1;
}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_EffectActivity_Fluidsynth_1Synth_1List_1ChorusDepth(JNIEnv *env,
                                                                                      jobject thiz,
                                                                                      jint global_channel,
                                                                                      jint usb_device_id,
                                                                                      jstring instr,
                                                                                      jfloat setting_value,
                                                                                      jboolean all) {
    // TODO: implement Fluidsynth_Synth_List_ChorusDepth()

    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); ++it) {

        it.operator*()->setSynthSettingsChorusDepth(env,setting_value);


        /*
        const char *getPresetNameFromJava = env->GetStringUTFChars(instr, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            usb_device_id == listUsbId) {

            it.operator*()->setSynthSettingsChorusDepth(env,setting_value);


            break;

        }else if(all){

            it.operator*()->setSynthSettingsChorusDepth(env,setting_value);


        }*/
    }



}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_EffectActivity_Fluidsynth_1Synth_1List_1ChorusLevel(JNIEnv *env,
                                                                                      jobject thiz,
                                                                                      jint global_channel,
                                                                                      jint usb_device_id,
                                                                                      jstring instr,
                                                                                      jfloat setting_value,
                                                                                      jboolean all) {
    // TODO: implement Fluidsynth_Synth_List_ChorusLevel()

    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); ++it) {


        it.operator*()->setSynthSettingsChorusLevel(env,setting_value);

/*
        const char *getPresetNameFromJava = env->GetStringUTFChars(instr, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            usb_device_id == listUsbId) {

            it.operator*()->setSynthSettingsChorusLevel(env,setting_value);


            break;

        }else if(all){

            it.operator*()->setSynthSettingsChorusLevel(env,setting_value);


        }*/
    }


}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_EffectActivity_Fluidsynth_1Synth_1List_1ChorusNr(JNIEnv *env,
                                                                                   jobject thiz,
                                                                                   jint global_channel,
                                                                                   jint usb_device_id,
                                                                                   jstring instr,
                                                                                   jint setting_value,
                                                                                   jboolean all) {
    // TODO: implement Fluidsynth_Synth_List_ChorusNr()

    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); ++it) {

        it.operator*()->setSynthSettingsChorusNr(env,setting_value);

        /*
        const char *getPresetNameFromJava = env->GetStringUTFChars(instr, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            usb_device_id == listUsbId) {

            it.operator*()->setSynthSettingsChorusNr(env,setting_value);


            break;

        }else if(all){

            it.operator*()->setSynthSettingsChorusNr(env,setting_value);


        }*/
    }






}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_EffectActivity_Fluidsynth_1Synth_1List_1ChorusSpeed(JNIEnv *env,
                                                                                      jobject thiz,
                                                                                      jint global_channel,
                                                                                      jint usb_device_id,
                                                                                      jstring instr,
                                                                                      jfloat setting_value,
                                                                                      jboolean all) {
    // TODO: implement Fluidsynth_Synth_List_ChorusSpeed()

    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); ++it) {

        it.operator*()->setSynthSettingsChorusSpeed(env,setting_value);


        /*
        const char *getPresetNameFromJava = env->GetStringUTFChars(instr, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            usb_device_id == listUsbId && !all) {

            it.operator*()->setSynthSettingsChorusSpeed(env,setting_value);


            break;

        }else if(all){

            it.operator*()->setSynthSettingsChorusSpeed(env,setting_value);


        }*/
    }

}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_EffectActivity_Fluidsynth_1Synth_1List_1ReverbDamp(JNIEnv *env,
                                                                                     jobject thiz,
                                                                                     jint global_channel,
                                                                                     jint usb_device_id,
                                                                                     jstring instr,
                                                                                     jfloat setting_value,
                                                                                     jboolean all) {
    // TODO: implement Fluidsynth_Synth_List_ReverbDamp()

    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); ++it) {

        it.operator*()->setSynthSettingsReverbDamp(env,setting_value);

        /*
        const char *getPresetNameFromJava = env->GetStringUTFChars(instr, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            usb_device_id == listUsbId) {

            it.operator*()->setSynthSettingsReverbDamp(env,setting_value);


            break;

        }else if(all){

            it.operator*()->setSynthSettingsReverbDamp(env,setting_value);


        }*/
    }

}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_EffectActivity_Fluidsynth_1Synth_1List_1ReverbLevel(JNIEnv *env,
                                                                                      jobject thiz,
                                                                                      jint global_channel,
                                                                                      jint usb_device_id,
                                                                                      jstring instr,
                                                                                      jfloat setting_value,
                                                                                      jboolean all) {
    // TODO: implement Fluidsynth_Synth_List_ReverbLevel()


    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); ++it) {

        it.operator*()->setSynthSettingsReverbLevel(env,setting_value);

        /*
        const char *getPresetNameFromJava = env->GetStringUTFChars(instr, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
        usb_device_id == listUsbId) {

            it.operator*()->setSynthSettingsReverbLevel(env,setting_value);


            break;

        }else if(all){

            it.operator*()->setSynthSettingsReverbLevel(env,setting_value);


        }*/
    }

}extern "C"
JNIEXPORT jint JNICALL
Java_jp_kshoji_driver_midi_sample_EffectActivity_Fluidsynth_1Synth_1List_1ReverbRoomsize(
        JNIEnv *env, jobject thiz, jint global_channel, jint usb_device_id, jstring instr,
        jfloat setting_value, jboolean all) {




    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); ++it) {


        it.operator*()->setSynthSettingsReverbRS(env,setting_value);
        /*
        const char *getPresetNameFromJava = env->GetStringUTFChars(instr, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            usb_device_id == listUsbId) {

            it.operator*()->setSynthSettingsReverbRS(env,setting_value);

            return 1;

            break;

        }else if(all== true){

            it.operator*()->setSynthSettingsReverbRS(env,setting_value);

            return 0;

        }
        */

    }
    return 1;


}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_EffectActivity_Fluidsynth_1Synth_1List_1ReverbWidth(JNIEnv *env,
                                                                                      jobject thiz,
                                                                                      jint global_channel,
                                                                                      jint usb_device_id,
                                                                                      jstring instr,
                                                                                      jfloat setting_value,
                                                                                      jboolean all) {
    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); ++it) {

        it.operator*()->setSynthSettingsReverbWidth(env, setting_value);
/*
        const char *getPresetNameFromJava = env->GetStringUTFChars(instr, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            usb_device_id == listUsbId) {

            it.operator*()->setSynthSettingsReverbWidth(env, setting_value);


            break;

        } else if (all) {

            it.operator*()->setSynthSettingsReverbWidth(env, setting_value);


        }*/
    }


}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_EffectActivity_fluidsynthListSetTranspo(JNIEnv *env, jobject thiz,
                                                                          jint global_channel,
                                                                          jint usb_device_id,
                                                                          jstring instr,
                                                                          jint int_transpo) {
    // TODO: implement fluidsynthListSetTranspo()

    list<MyFluidsynth *>::iterator it;
    for (it = myfluidsynthList.begin(); it != myfluidsynthList.end(); it++) {

        const char *getPresetNameFromJava = env->GetStringUTFChars(instr, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();

        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            usb_device_id == listUsbId) {


            it.operator*()->settTranspo(int_transpo);
            break;



        }
    }


}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_MIDIDriverMultipleSampleActivity_Fluidsynth_1Synth_1List_1ProgramChange_1drums(
        JNIEnv *env, jobject thiz, jint channel, jint bank, jint progr, jstring str_presetname,
        jint usb_device_id) {

    list<MyFluidsynth*>::iterator it;
    for(it = myfluidsynthList.begin();it != myfluidsynthList.end();it++){

        const char *getPresetNameFromJava = env->GetStringUTFChars(str_presetname, nullptr);
        const char *getPresetNameFromInstrList = it.operator*()->getPresetName();

        int listUsbId = it.operator*()->getUsbId();


        if (strcmp(getPresetNameFromJava, getPresetNameFromInstrList) == 0 &&
            usb_device_id == listUsbId) {


            it.operator*()->setProgramchange(channel, progr, bank);


        }

    }

}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_Synthesizer_fluidsynth_1ListSendNoteOnMessage(JNIEnv *env,
                                                                                jobject thiz,
                                                                                jint channel,
                                                                                jint note,
                                                                                jint velocity,
                                                                                jint i_usb_id) {


    list<MyFluidsynth*>::iterator it;
    for(it = myfluidsynthList.begin();it != myfluidsynthList.end();it++){


        it.operator*()->onNoteOn(channel,note,velocity,i_usb_id);

    }


}extern "C"
JNIEXPORT void JNICALL
Java_jp_kshoji_driver_midi_sample_Synthesizer_fluidsynth_1ListSendNoteOffMessage(JNIEnv *env,
                                                                                 jobject thiz,
                                                                                 jint channel,
                                                                                 jint note,
                                                                                 jint i_usb_id) {

    list<MyFluidsynth*>::iterator it;
    for(it = myfluidsynthList.begin();it != myfluidsynthList.end();it++){



        it.operator*()->onNoteOff(channel,note,i_usb_id);

    }
}