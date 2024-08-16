Description
====
with MULTImidiSOUNDFONTplayer you can mix diverent instruments
from a sf2-file (or diverse in future)  for each midi-controller you plug on your
smartphone (os android) via usb-otg, so you can build your
own orchestra for each midi-controller.

Attention: Screeenshots are not updated.




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

![Screenshot_20240816_205955_MIDIDriverSample](https://github.com/user-attachments/assets/654e061b-613b-4e78-8903-f5c550f3ceea)
![Screenshot_20240816_205917_MIDIDriverSample](https://github.com/user-attachments/assets/232849f8-a147-4bf4-bd66-af7631124ac3)
![Screenshot_20240816_205840_MIDIDriverSample](https://github.com/user-attachments/assets/363b61ee-ec61-4eb3-9bce-3ff89b30ef66)
![Screenshot_20240816_205829_MIDIDriverSample](https://github.com/user-attachments/assets/d0f6e24f-1430-4661-a4b5-66c440ce0e75)
![Screenshot_20240816_205807_MIDIDriverSample](https://github.com/user-attachments/assets/d0d6cdf7-dc64-4922-b457-7ade32a6a259)
![Screenshot_20240816_205739_MIDIDriverSample](https://github.com/user-attachments/assets/b90163ef-0eab-462e-91fc-0519b19caffe)
![Screenshot_20240816_205720_MIDIDriverSample](https://github.com/user-attachments/assets/9fae0c03-8d57-4d9a-93a0-c0d7db1f6d40)
![Screenshot_20240816_205700_MIDIDriverSample](https://github.com/user-attachments/assets/bcb2de8e-025d-4509-8bd5-2ebb30d3c564)
![Screenshot_20240816_205631_MIDIDriverSample](https://github.com/user-attachments/assets/ccd50f79-46b7-41cc-9a26-537e9d6a1f97)
![Screenshot_20240816_205617_MIDIDriverSample](https://github.com/user-attachments/assets/2eb8ee55-7bce-42af-9ead-231acb3751c8)
![Screenshot_20240816_205542_MIDIDriverSample](https://github.com/user-attachments/assets/2da87fde-197b-44a3-ae36-bda2f73775c8)


![Gif:](/20220127_141131.gif?raw=true "Gif Animation")

Thanks
====
- fluidsynth
- https://github.com/HectorRicardo/fluidsynth-android-hello-world
- https://github.com/kshoji/USB-MIDI-Driver
- https://github.com/agangzz/SherlockMidi
- javax.sound.midi
- https://github.com/kshoji/javax.sound.midi-for-Android
