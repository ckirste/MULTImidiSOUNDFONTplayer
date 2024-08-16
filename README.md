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
- diverent volumes for each instrument * done *
- transpose buttons * done *
- save/open selected instruments and settings * done *
- add more effects (reverb, delay,etc.) * done *
- file-chooser for sf2-files * done *
- record/play midi option
- option of drum companion
...

![Screenshot_20240816_205542_MIDIDriverSample](https://github.com/user-attachments/assets/e13646fb-44d7-4b8e-82a2-902189ac0237)

![Screenshot_20240816_205617_MIDIDriverSample](https://github.com/user-attachments/assets/a81e67e0-ca0a-4b92-98a8-adbb194f8379)

![Screenshot_20240816_205631_MIDIDriverSample](https://github.com/user-attachments/assets/8e7c515e-b33b-4bf3-900c-c16158594c58)

![Screenshot_20240816_205700_MIDIDriverSample](https://github.com/user-attachments/assets/e4e959ef-6fcb-4b45-82d5-7f58d7f9d2cb)

![Screenshot_20240816_205720_MIDIDriverSample](https://github.com/user-attachments/assets/3a042386-dacb-4991-8df3-9718778a4127)

![Screenshot_20240816_205739_MIDIDriverSample](https://github.com/user-attachments/assets/6069cc84-b3e8-4920-922a-4a7465a19473)

![Screenshot_20240816_212309_MIDIDriverSample](https://github.com/user-attachments/assets/ac048996-e45a-440b-ac8b-1791b31317b5)

![Screenshot_20240816_205807_MIDIDriverSample](https://github.com/user-attachments/assets/a462ca11-cd9f-4598-a60a-fc888650b7bf)

![Screenshot_20240816_205829_MIDIDriverSample](https://github.com/user-attachments/assets/1b597847-4262-4821-bb4e-c922b5f3a1be)

![Screenshot_20240816_205840_MIDIDriverSample](https://github.com/user-attachments/assets/31975b93-4a42-477b-9754-924ff0681ff3)

![Screenshot_20240816_205917_MIDIDriverSample](https://github.com/user-attachments/assets/959121a3-fd93-4aea-a956-ea7567a299c6)

![Screenshot_20240816_205955_MIDIDriverSample](https://github.com/user-attachments/assets/428a870e-882b-43bf-b527-d3941b4fb4bc)

Thanks
====
- fluidsynth
- https://github.com/HectorRicardo/fluidsynth-android-hello-world
- https://github.com/kshoji/USB-MIDI-Driver
- https://github.com/agangzz/SherlockMidi
- javax.sound.midi
- https://github.com/kshoji/javax.sound.midi-for-Android
