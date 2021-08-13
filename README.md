Description
====
with MULTImidiSOUNDFONTplayer you can mix diverent instruments
from a sf2-file (or diverse in future)  for each midi-controller you plug on your
smartphone (os android) via usb-otg, so you can have your
own ochestra for each midi-controller. 




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
- iterate instruments per usbdevice
- diverent volumes for each instrument
- transpose buttons
- save/open selected instruments and settings
- add more effects (reverb, delay,etc.) - circle-seekbar
- file-chooser for sf2-files
- cleanup code
- record/play option
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
