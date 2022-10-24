#include <jni.h>
#include <string>
#include <fluidsynth.h>
#include <unistd.h>
#include <mutex>



fluid_settings_t *settings;
fluid_synth_t *synth;
fluid_audio_driver_t *adriver;
std::mutex synthMutex;
std::mutex mnotesruningMutex;

struct SF2PluginData
{
    int midiNote;
    float lastVelocity;
    fluid_voice_t * fluidVoice;


} ;

int m_notesRunning[128];

/*
int handle_midi_event(void* data, fluid_midi_event_t* event)
{
    //printf("event type: %d\n", );


    int int_key = fluid_midi_event_get_key(event);
    int int_veloc = fluid_midi_event_get_velocity(event);
    int eventtyp = fluid_midi_event_get_type(event);


    switch (eventtyp) {


        case 128 ... 143: fluid_synth_noteoff(synth, 0, int_key);// Note off
        case 144 ... 159: fluid_synth_noteon(synth, 0, int_key, int_veloc);//Note 0n

    }

}
*/
extern "C" JNIEXPORT void JNICALL Java_com_example_myapplication_MainActivity_fluidsynthHelloWorld(JNIEnv *env, jobject, jstring jSoundfontPath) {
    // Setup synthesizer

    settings = new_fluid_settings();


    fluid_settings_setstr(settings, "audio.driver", "oboe");
    fluid_settings_setstr(settings, "audio.oboe.performance-mode", "LowLatency");
    fluid_settings_setstr(settings, "audio.oboe.sharing-mode", "Exclusive");
    fluid_settings_setnum(settings,"synth.sample-rate",48000);
    fluid_settings_setint(settings,"audio.periods",2);
    fluid_settings_setint(settings,"audio.period-size",256);
    synth = new_fluid_synth(settings);


    adriver = new_fluid_audio_driver(settings, synth);

    // Load sample soundfont
    const char *soundfontPath = env->GetStringUTFChars(jSoundfontPath, nullptr);
    fluid_synth_sfload(synth, soundfontPath, 1);

    fluid_synth_program_change(synth, 0, 48);

    fluid_synth_noteon(synth, 0, 60, 127); // play middle C

    sleep(1); // sleep for 1 second
    fluid_synth_noteoff(synth, 0, 60); // stop playing middle C


    fluid_synth_noteon(synth, 0, 62, 127);

    sleep(1);
    fluid_synth_noteoff(synth, 0, 62);


    fluid_synth_noteon(synth, 0, 64, 127);

    sleep(1);
    fluid_synth_noteoff(synth, 0, 64);


}
using namespace std;

extern "C" JNIEXPORT void JNICALL Java_com_example_myapplication_MainActivity_fluidsynthSendNoteOnMessage(JNIEnv *env, jobject thiz,
                                                                        jint channel, jint note,
                                                                        jint velocity) {

    lock_guard<mutex> lock(synthMutex);

    // get list of current voice IDs so we can easily spot the new
    // voice after the fluid_synth_noteon() call
    const int poly = fluid_synth_get_polyphony( synth );
    fluid_voice_t * voices[poly];
    unsigned int id[poly];
    fluid_synth_get_voicelist( synth, voices, poly, -1 );
    for( int i = 0; i < poly; ++i )
    {
        id[i] = 0;
    }
    for( int i = 0; i < poly && voices[i]; ++i )
    {
        id[i] = fluid_voice_get_id( voices[i] );
    }

    fluid_synth_noteon( synth, channel, note, velocity );

    // get new voice and save it
    fluid_synth_get_voicelist( synth, voices, poly, -1 );
    for( int i = 0; i < poly && voices[i]; ++i )
    {
        const unsigned int newID = fluid_voice_get_id( voices[i] );
        if( id[i] != newID || newID == 0 )
        {
            //n->fluidVoice = voices[i];
            break;
        }
    }

    lock_guard<mutex> unlock(synthMutex);


}

/*
void noteOn( SF2PluginData * n )
{
    (lock_guard<mutex>(synthMutex));

    // get list of current voice IDs so we can easily spot the new
    // voice after the fluid_synth_noteon() call
    const int poly = fluid_synth_get_polyphony( synth );
    fluid_voice_t * voices[poly];
    unsigned int id[poly];
    fluid_synth_get_voicelist( synth, voices, poly, -1 );
    for( int i = 0; i < poly; ++i )
    {
        id[i] = 0;
    }
    for( int i = 0; i < poly && voices[i]; ++i )
    {
        id[i] = fluid_voice_get_id( voices[i] );
    }

    fluid_synth_noteon( synth, 0, n->midiNote, n->lastVelocity );

    // get new voice and save it
    fluid_synth_get_voicelist( synth, voices, poly, -1 );
    for( int i = 0; i < poly && voices[i]; ++i )
    {
        const unsigned int newID = fluid_voice_get_id( voices[i] );
        if( id[i] != newID || newID == 0 )
        {
            n->fluidVoice = voices[i];
            break;
        }
    }

    (lock_guard<mutex>(synthMutex));
    

    lock_guard<mutex> lock(mnotesruningMutex);


    ++m_notesRunning[ n->midiNote ];

    lock_guard<mutex> unlock(mnotesruningMutex);
    
}
*/

extern "C"
JNIEXPORT void JNICALL
Java_com_example_myapplication_MainActivity_fluidsynthSendNoteOffMessage(JNIEnv *env, jobject thiz,
                                                                         jint channel, jint note) {

    lock_guard<mutex> lock(synthMutex);
    fluid_synth_noteoff(synth, channel, note);
    lock_guard<mutex> unlock(synthMutex);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_myapplication_MainActivity_fluidsynthDeleteSynth(JNIEnv *env, jobject thiz) {


    delete_fluid_audio_driver(adriver);
    delete_fluid_synth(synth);
    delete_fluid_settings(settings);
    //delete_fluid_midi_driver(mdriver);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_myapplication_MainActivity_fluidsynthProgrammChange(JNIEnv *env, jobject thiz,
                                                                     jint programm) {
    fluid_synth_program_change(synth, 0, programm);
}

