# Fractal Music Generator

A downloadable cross-platform **application** for creating *polyphonic* **audio** and **midi** *music* from **fractals**.

* * *

# Download

> Releases for **Windows**, **Mac**, **Linux** and **Java** can be downloaded from [**Itch** - click *here*](https://betazeta.itch.io/fractal-music-generator)

* * *

# Presentation video

[Click *here* to watch a **video** about *Fractal Music Generator* on **YouTube**!](https://youtu.be/iOqTAKswWEk)

* * *

# Screenshots

![FractalMusicGenerator_tabs](https://img.itch.zone/aW1hZ2UvMTUwMzkwOC84ODE0Mzk1LnBuZw==/original/K0L9aG.png)

* * *

![FractalMusicGenerator_zoomed](https://img.itch.zone/aW1hZ2UvMTUwMzkwOC84ODE0Mzk2LnBuZw==/original/roRJfn.png)

* * *

![FractalMusicGenerator_presets](https://img.itch.zone/aW1hZ2UvMTUwMzkwOC84ODE0NDE0LnBuZw==/original/XIfs3C.png)

* * *

# Main Features

- Direct conversion from orbital values to audio
  - Customize interpolation and orbit keeping 
- Sinusoidal wave generator with frequency linked to orbital values
  - Customize conversion range and pitch
- Audio wave polyphony
  - 55+ pixels can be played at the same time
- Fractal outline detection
  - Lock outline while exploring details
  - Unlock for playing different pixels while preserving the detected shape
  - Play the original fractal shape on different fractals
- Midi support
  - Extensive customization for conversion from orbital values to midi data
  - Every pixel uses its own sequencer connected to the synthesizers
  - 55+ sequencers playing midi sequences (like midi files) concurrently
  - Separate customizable management of midi drums
  - 55+ sequencers playing drums concurrently
  - Select instruments by hand or let the fractal decide
- Support for external soundfonts
- Save converted orbital values from a fractal to a midi file
- Support for Jack Audio Connection Kit
- External real hardware midi devices
  - Play a fractal on your keyboard
- Virtual midi devices
  - Connect to DAWs
- GPU accelerated fractal rendering
  - Smoothing, histogram, various fractals
- Simple code  

* * *

# Known Issues

- GPU acceleration requires OpenCL
  - GUI will report if a GPU is available: fallback to CPU otherwise
- The built-in soundfont is of different quality for different platforms
  - Please load a high quality one (DLS or SF2 format)​
  - For this reason the built-in presets cannot always be optimized​
- The application can be fully used in standalone mode but it is intended as a source for DAWs​
  - Requires LoopBe1 or VirtualMidiSynth or Virmidi as virtual devices
- Arm-based Mac support is untested

* * *

# License

**GPL version 3**

Do not modify or remove links or donation requesters.

All donations go to the project maintainer, BetaZeta.

* * *

# Dependencies

[**Aparapi**:](https://aparapi.com/)
A framework for executing native Java code on the GPU.
Originally a project conceived and developed by AMD corporation.
2016 - present Syncleus.

[**XStream**:](https://x-stream.github.io/)
A library to serialize objects to XML and back again.

[**Jnajack**:](https://github.com/jaudiolibs/jnajack)
Java bindings to JACK Audio Connection Kit. Neil C Smith.

[**Flatlaf**:](https://www.formdev.com/flatlaf/)
FlatLaf is a modern open-source cross-platform Look and Feel for Java Swing desktop applications.


## Additional code

**DumpReceiver**:
Dumping midi data. 1999 - 2001 Matthias Pfisterer. 2003 Florian Bomers.

**SmartScroller**:
Keep the viewport positioned based on the users interaction with the scrollbar. Rob Camick.
