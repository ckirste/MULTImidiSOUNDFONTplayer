Description
====
with MULTImidiSOUNDFONTplayer you can mix diverent instruments
from a sf2-file (or diverse in future)  for each midi-controller you plug on your
smartphone (os android) via usb-otg, so you can build your
own orchestra for each midi-controller. 




Repository Overview
====
- Library Project : `MIDIDriver`
    - The driver for connecting an USB MIDI device.

- Sample Project : `MIDIDriverSample`
    - The sample implementation of the synthesizer / MIDI event logger.
    - the synthesizer uses fluidsynth
  


Library Project Usages
====

See the [project wiki](https://github.com/kshoji/USB-MIDI-Driver/wiki) for the library usages.
AND fluidsynth.org


Todo
====
- iterate instruments per usbdevice * done *
- diverent volumes for each instrument * work in progress *
- transpose buttons * done *
- save/open selected instruments and settings * done *
- add more effects (reverb, delay,etc.) * work in progress *
- file-chooser for sf2-files * done *
- record/play midi option
- option of drum companion
...

Thanks
====
- fluidsynth
- https://github.com/HectorRicardo/fluidsynth-android-hello-world
- https://github.com/kshoji/USB-MIDI-Driver
- https://github.com/agangzz/SherlockMidi
- javax.sound.midi
- https://github.com/kshoji/javax.sound.midi-for-Android
