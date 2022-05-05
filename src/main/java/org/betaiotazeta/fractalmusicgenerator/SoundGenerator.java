package org.betaiotazeta.fractalmusicgenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SoundGenerator {

    public SoundGenerator(FmgApp fmgApp) {
        this.fmgApp = fmgApp;
    }

    public void playWave(OrbitCalculator orbitCalculator) throws InterruptedException, LineUnavailableException {
        int sampleRate = 44100;
        // AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian) with a linear PCM encoding.
        AudioFormat af = new AudioFormat((float) sampleRate, 16, 2, true, false);
        SourceDataLine sdl = null;
        boolean antiautowakeup = true;
        ComplexNumber[] orbitsArray = orbitCalculator.getOrbitsArray();
        if (orbitsArray[0] == null) {
            return;
        }
        if (fmgApp.getSkipDivergentWaveCheckBox().isSelected()) {
            if (orbitsArray[orbitsArray.length - 1] == null) {
                return;
            }
        }
        int bufferSize = (int) fmgApp.getBufferSizeSpinner().getValue(); // 88200

        synchronized (lineMonitor) {
            if (occupiedSDL > maxSDL) {
                return;
            }
            if (requestedSDL == true) {
                return;
            }
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            while (occupiedSDL == maxSDL) {
                if (antiautowakeup == true) {
                    sdlCheck = sdlArrayList.get(0);
                    // retreived playing sdl                    
                    sdlArrayList.remove(0);
                    sdlArrayList.add(sdlCheck);                   
                    antiautowakeup = false;
                    requestedSDL = true;
                }
                lineMonitor.wait();
            }
            if (antiautowakeup == false) {
                requestedSDL = false;
            }
            if (sdlArrayList.size() < maxSDL) {
                sdlCheck = AudioSystem.getSourceDataLine(af);
                // created sdl
                sdlArrayList.add(sdlCheck);
            } else {
                if (antiautowakeup == true) {
                    sdlCheck = sdlArrayList.get(0);
                    // retreived idle sdl
                    sdlArrayList.remove(0);
                    sdlArrayList.add(sdlCheck);
                }
            }

            int maxSDLcurrent = fmgApp.getMaxSdlSlider().getValue();
            if (maxSDLcurrent < sdlArrayList.size()) {                
                int sdlArrayListLength = sdlArrayList.size();
                int sdlToClose = sdlArrayListLength - maxSDLcurrent;
                for (int i = 0; i < sdlToClose; i++) {
                    sdlCheck = sdlArrayList.get(0);
                    sdlArrayList.remove(0);
                    if (sdlCheck.isOpen()) {
                        sdlCheck.stop();
                        sdlCheck.flush();
                        sdlCheck.close();
                    }
                }
                sdlCheck = sdlArrayList.get(0);
                sdlArrayList.remove(0);
                sdlArrayList.add(sdlCheck);
                maxSDL = maxSDLcurrent;
                String message = "closed " + sdlToClose + " wave lines of " + sdlArrayListLength + nl;
                fmgApp.getTab1TextArea().append(message);
            }
            
            if (maxSDLcurrent != maxSDL) {
                maxSDL = maxSDLcurrent;
            }
            
            sdl = sdlCheck;
            occupiedSDL++;

            if (!sdl.isOpen()) {
                sdl.open(af, bufferSize);
            }
            sdl.start();
        }

        int durationInSeconds = fmgApp.getDurationWaveSlider().getValue();
        int interpolationPoints = (int) fmgApp.getInterpolationPointsSpinner().getValue(); // never 0 // 100
        int keepOrbitForTotSamples = (int) fmgApp.getKeepOrbitSpinner().getValue(); // never 0 // 25
        int bufferPolling = (int) fmgApp.getBufferPollingSpinner().getValue(); // 1
        int volume = fmgApp.getVolumeWaveSlider().getValue(); // max 32767
        double minZxDirectSpinner = (double) fmgApp.getMinZxDirectSpinner().getValue();
        double maxZxDirectSpinner = (double) fmgApp.getMaxZxDirectSpinner().getValue();
        double minZyDirectSpinner = (double) fmgApp.getMinZyDirectSpinner().getValue();
        double maxZyDirectSpinner = (double) fmgApp.getMaxZyDirectSpinner().getValue();
        double minZySineSpinner = (double) fmgApp.getMinZySineSpinner().getValue();
        double maxZySineSpinner = (double) fmgApp.getMaxZySineSpinner().getValue();
        int minFreqSpinner = (int) fmgApp.getMinFreqSpinner().getValue();
        int maxFreqSpinner = (int) fmgApp.getMaxFreqSpinner().getValue();
        boolean verbose = fmgApp.getVerboseCheckBox().isSelected();
        boolean directRadioButton = fmgApp.getDirectRadioButton().isSelected();
        boolean sineRadioButton = fmgApp.getSineRadioButton().isSelected();
        int loops = ((sampleRate * durationInSeconds) / interpolationPoints);
        int maxAudioIterations = orbitCalculator.getMaxAudioIterations();
        byte[] buffer = new byte[4 * loops * interpolationPoints];
        int index = -1;
        double xValue = 0;
        int interpolationPosition = 0;
        double fadeIn = 0;
        double fadeOut = 1;
        int frameCounter = 0;

        double zx = orbitsArray[0].getRealValue();
        double zy = orbitsArray[0].getImaginaryValue();

        // find min and max of all orbits for x
        double maxZx = zx;
        double minZx = zx;
        for (int i = 1; i < orbitsArray.length; i++) {
            if (orbitsArray[i] == null) {
                break;
            }
            double tmpZx = orbitsArray[i].getRealValue();
            if (tmpZx > maxZx) {
                maxZx = tmpZx;
            }
            if (tmpZx < minZx) {
                minZx = tmpZx;
            }
        }

        for (int i = 0; i < loops; i++) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            if (i % keepOrbitForTotSamples == 0) { // keep orbit value for amount
                index = index + 1;
            }
            if (index + 1 == maxAudioIterations) { // length of array
                index = 0;
                if (verbose) {
                    fmgApp.getTab1TextArea().append("Wave audio note duration exceeds computed audio iterations: orbits will be repeated." + nl);
                }
            }
            if (orbitsArray[index + 1] == null) {
                if (verbose) {
                    fmgApp.getTab1TextArea().append("Pixel with diverging orbits for Wave detected! Not in set." + nl);
                }
                break;
            }

            // interpolation
            double zxNext = orbitsArray[index + 1].getRealValue();
            double zyNext = orbitsArray[index + 1].getImaginaryValue();
            double zxStep = (zxNext - zx) / interpolationPoints;
            double zyStep = (zyNext - zy) / interpolationPoints;
            for (int j = 0; j < interpolationPoints; j++) {
                double zxPlay = zx + (zxStep * j);
                double zyPlay = zy + (zyStep * j);

                if (directRadioButton) {
                    double sampleLd = Utilities.scale(zxPlay, minZxDirectSpinner, maxZxDirectSpinner, -volume, volume);
                    double sampleRd = Utilities.scale(zyPlay, minZyDirectSpinner, maxZyDirectSpinner, -volume, volume);
                    short sampleL = (short) (sampleLd * fadeIn * fadeOut);
                    short sampleR = (short) (sampleRd * fadeIn * fadeOut);
                    buffer[(j * 4) + interpolationPosition] = (byte) (sampleL & 0xFF); //     write 8 bits ________VVVVVVVV out of 16
                    buffer[(j * 4 + 1) + interpolationPosition] = (byte) (sampleL >> 8); //   write 8 bits VVVVVVVV________ out of 16
                    buffer[(j * 4 + 2) + interpolationPosition] = (byte) (sampleR & 0xFF); // write 8 bits ________VVVVVVVV out of 16
                    buffer[(j * 4 + 3) + interpolationPosition] = (byte) (sampleR >> 8); //   write 8 bits VVVVVVVV________ out of 16
                }

                if (sineRadioButton) {
                    double pan = Utilities.scale(zxPlay, minZx, maxZx, 0, 1);
                    int frequency = (int) Utilities.scale(zyPlay, minZySineSpinner, maxZySineSpinner, minFreqSpinner, maxFreqSpinner);
                    double segmentX = 2 * Math.PI * frequency * (1.0 / sampleRate);
                    xValue = xValue + segmentX;
                    double sample = Math.sin(xValue) * (volume * fadeIn * fadeOut); // sample can take from -32767 to 32767
                    short sampleL = (short) (sample * (1 - pan));
                    short sampleR = (short) (sample * (pan));
                    buffer[(j * 4) + interpolationPosition] = (byte) (sampleL & 0xFF);
                    buffer[(j * 4 + 1) + interpolationPosition] = (byte) (sampleL >> 8);
                    buffer[(j * 4 + 2) + interpolationPosition] = (byte) (sampleR & 0xFF);
                    buffer[(j * 4 + 3) + interpolationPosition] = (byte) (sampleR >> 8);
                }
                // fading
                if (frameCounter < 1024) { // 1024 is an arbitrary value: fade length
                    fadeIn = fadeIn + 0.000976562; // 0.000976562 * 1024 = 1
                }
                if (frameCounter > (sampleRate * durationInSeconds) - 1024) {
                    fadeOut = fadeOut - 0.000976562;
                }
                frameCounter++;
            }
            zx = zxNext;
            zy = zyNext;
            interpolationPosition = interpolationPosition + (interpolationPoints * 4);
        }

        int bytesToWrite;
        int position = 0;
        while (sdl.isOpen()) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            // fast exit
            if (requestedSDL == true && sdl.equals(sdlCheck)) {
                // playing threads must have enough data in line buffer for this block!
                // fadeExit
                int remaining = buffer.length - position;
                if (remaining > 4096) { // 4096 means 1024 frames
                    remaining = 4096;
                } else {
                    break;
                }
                fadeOut = 1;
                int posFrame = position / 4;
                int remFrame = remaining / 4;
                for (int i = posFrame; i < (posFrame + remFrame); i++) {
                    int sampleL = (buffer[i * 4 + 1] << 8) | (buffer[i * 4] & 0xFF);
                    int sampleR = (buffer[i * 4 + 3] << 8) | (buffer[i * 4 + 2] & 0xFF);
                    sampleL = (int) (sampleL * fadeOut);
                    sampleR = (int) (sampleR * fadeOut);
                    buffer[i * 4] = (byte) (sampleL & 0xFF);
                    buffer[i * 4 + 1] = (byte) ((sampleL >> 8) & 0xFF);
                    buffer[i * 4 + 2] = (byte) (sampleR & 0xFF);
                    buffer[i * 4 + 3] = (byte) ((sampleR >> 8) & 0xFF);
                    fadeOut = fadeOut - (1.0 / remFrame);
                }
                sdl.write(buffer, position, remaining);

                synchronized (lineMonitor) {
                    occupiedSDL--;
                    lineMonitor.notifyAll();
                    return;
                }
            }

            // normal write
            if (buffer.length - position < bufferSize) { // consider (bufferSize * 2)
                sdl.write(buffer, position, buffer.length - position);
                break;
            }
            int available = sdl.available();
            if (available > 0) { // if available space is growing, less data in line buffer
                // dangerous, may never become true
                bytesToWrite = available;
            } else {
                bytesToWrite = 0;
            }
            sdl.write(buffer, position, bytesToWrite);
            if (bytesToWrite != 0 && available == sdl.available()) {
                // after write, available should not be the same
                overloadedSDL.compareAndSet(false, true);
            }
            position = position + bytesToWrite;
            Thread.sleep(bufferPolling);
        }

        // before exit
        synchronized (lineMonitor) {
            occupiedSDL--;
            lineMonitor.notifyAll();
        }
    }

    public void playMelody(OrbitCalculator orbitCalculator) throws InterruptedException, MidiUnavailableException, InvalidMidiDataException {
        // Java Synthesizer has max polyphony of 64!
        Sequencer sequencer = null;
        int noteDuration = fmgApp.getNoteDurationSlider().getValue(); // 50
        int noteVelocity = fmgApp.getNoteVelocitySlider().getValue(); // 64        
        int sequenceSpeed = fmgApp.getSequenceSpeedSlider().getValue(); // 10 normal (es.: 1 slower, 100 faster)
        int maxSequencers = fmgApp.getMaxSequencersSlider().getValue(); // 57 max
        boolean preventInterruptions = fmgApp.getPreventInterruptionsCheckBox().isSelected();
        boolean skipDivergent = fmgApp.getSkipDivergentMidiCheckBox().isSelected();
        boolean instrChangeEffect = fmgApp.getInstrChangeEffectCheckBox().isSelected();
        boolean panMidiEffect = fmgApp.getPanMidiEffectCheckBox().isSelected();
        double minZxMidi = (double) fmgApp.getMinZxMidiSpinner().getValue();
        double maxZxMidi = (double) fmgApp.getMaxZxMidiSpinner().getValue();
        int minMidiEffect = (int) fmgApp.getMinMidiEffectSpinner().getValue();
        int maxMidiEffect = (int) fmgApp.getMaxMidiEffectSpinner().getValue();
        double minZyMidi = (double) fmgApp.getMinZyMidiSpinner().getValue();
        double maxZyMidi = (double) fmgApp.getMaxZyMidiSpinner().getValue();
        int minNote = (int) fmgApp.getMinNoteSpinner().getValue();
        int maxNote = (int) fmgApp.getMaxNoteSpinner().getValue();
        boolean verbose = fmgApp.getVerboseCheckBox().isSelected();
        boolean initInstruments = fmgApp.getInitInstrumentsCheckBox().isSelected();
        int reverb = fmgApp.getReverbSlider().getValue();
        int chorus = fmgApp.getChorusSlider().getValue();
        Integer channelOld = null;
        ShortMessage message;
        MidiEvent event;

        ComplexNumber[] orbitArray = orbitCalculator.getOrbitsArray();
        
        // test if c belongs to the set
        if (orbitArray[orbitArray.length - 1] == null) {
            if (verbose) {
                fmgApp.getTab1TextArea().append("Pixel with diverging orbits for Melody detected! Not in set." + nl);
            }
            if (skipDivergent) {
                return;
            }
        }
        
        // prepare sequencer melody
        synchronized (melodyMonitor) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            if (melodySequencersArrayList.size() < maxSequencers) { // 57 max
                sequencer = MidiSystem.getSequencer(false);
                melodySequencersArrayList.add(sequencer);
            } else if (melodySequencersArrayList.size() == maxSequencers) {
                sequencer = melodySequencersArrayList.get(0);
                melodySequencersArrayList.remove(0);
                melodySequencersArrayList.add(sequencer);
            } else {
                int melodySeqArrayListLength = melodySequencersArrayList.size();
                int melodySeqToClose = melodySeqArrayListLength - maxSequencers;
                for (int i = 0; i < melodySeqToClose; i++) {
                    sequencer = melodySequencersArrayList.get(0);
                    melodySequencersArrayList.remove(0);
                    if (sequencer.isOpen()) {
                        sequencer.stop();
                        sequencer.close();
                    }
                }
                String str = "closed " + melodySeqToClose + " melody sequencers out of " + melodySeqArrayListLength + nl;
                str = str + "Maximum sequencers for type Melody set to: " + maxSequencers + nl;
                fmgApp.getTab1TextArea().append(str);
            }

            channel++;
            if (channel == 9) {
                channel++;
            }
            if (channel == 16) {
                channel = 0;
            }

            channelOld = seqChanHashMap.get(sequencer);
            seqChanHashMap.put(sequencer, channel);
        }
        // end of synchronized block        
        
        // prepare sequence melody

        /* The constructor for Sequence takes as arguments a divisionType and a 
        timing resolution. The divisionType argument specifies the units of the
        resolution argument (es. 10 pulses per quarter note.)
        An additional optional argument to the Sequence constructor is a number of
        tracks argument, which would cause the initial sequence to begin with the
        specified number of (initially empty) Tracks. Otherwise the Sequence will be
        created with no initial Tracks; they can be added later as needed.
         */
        Sequence sequence = new Sequence(Sequence.PPQ, sequenceSpeed); // ,0 creates an initial track 
        Track track = sequence.createTrack();
               
        for (int i = 0; i < orbitArray.length; i++) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }

            // may not be already skipping divergent pixels
            if (orbitArray[i] == null) {
                if (preventInterruptions) {
                    // will play what already accepted
                    // will NOT send "reset" on all channels when finished
                    break;
                } else {
                    // will reset the sequencer without playing anything
                    // will send "reset" on all channels
                    sequence.deleteTrack(track);
                    break;
                }
            }
            
            // test if later conversion is possible within particular range
            double zy = orbitArray[i].getImaginaryValue();
            if (zy < minZyMidi || zy > maxZyMidi) {
                if (verbose) {
                    fmgApp.getTab1TextArea().append("Out of range zy for melody conversion settings." + nl);
                }
                if (preventInterruptions) {
                    zy = 0;
                } else {
                    sequence.deleteTrack(track);
                    break;
                }
            }

            byte yNote = (byte) Utilities.scale(zy, minZyMidi, maxZyMidi, minNote, maxNote); // piano -> lowest note: c1 (24), highest: c8 (108) 

            // ShortMessage is a MidiMessage
            // note Middle C is (60), moderately loud (velocity = 100).
            message = new ShortMessage(ShortMessage.NOTE_ON, channel, yNote, noteVelocity); // yNote
            event = new MidiEvent(message, (i * 4) + 2);
            track.add(event);

            message = new ShortMessage(ShortMessage.NOTE_OFF, channel, yNote, 0); // final 0 is mandatory for Java synthesizer
            event = new MidiEvent(message, (i * 4) + 3 + (4 * noteDuration));
            track.add(event);

            double zx = orbitArray[i].getRealValue();
            if (zx < minZxMidi || zx > maxZxMidi) {
                if (verbose) {
                    fmgApp.getTab1TextArea().append("Out of range zx for melody conversion settings." + nl);
                }
                if (preventInterruptions) {
                    zx = 0;
                } else {
                    sequence.deleteTrack(track);
                    break;
                }
            }

            byte effectValue = (byte) Utilities.scale(zx, minZxMidi, maxZxMidi, minMidiEffect, maxMidiEffect);

            if (instrChangeEffect) {
                message = new ShortMessage(ShortMessage.PROGRAM_CHANGE, channel, effectValue, 0); // instrument change
                event = new MidiEvent(message, (i * 4));
                track.add(event);
            }

            if (panMidiEffect) {
                message = new ShortMessage(ShortMessage.CONTROL_CHANGE, channel, 10, effectValue); // panning
                event = new MidiEvent(message, (i * 4) + 1);
                track.add(event);
            }
        }
        // sequence prepared

        // setting up sequencer
        if (sequencer.getTransmitters().isEmpty()) {
            // a transmitter can have only one receiver
            // a receiver can have multiple transmitters
            Receiver gervillReceiver = fmgApp.getDeviceManager().getGervillReceiver();
            if (gervillReceiver != null) {
                sequencer.getTransmitter().setReceiver(gervillReceiver);
            }
            Receiver externalDeviceReceiver = fmgApp.getDeviceManager().getExternalDeviceReceiver();
            if (externalDeviceReceiver != null) {
                sequencer.getTransmitter().setReceiver(externalDeviceReceiver);
            }
            Receiver jackReceiver = fmgApp.getDeviceManager().getJackReceiver();
            if (jackReceiver != null) {
                sequencer.getTransmitter().setReceiver(jackReceiver);
            }
            Receiver dumpReceiver = fmgApp.getDeviceManager().getDumpReceiver();
            if (dumpReceiver != null) {
                sequencer.getTransmitter().setReceiver(dumpReceiver);
            }
            Receiver recorderReceiver = fmgApp.getDeviceManager().getRecorderReceiver();
            if (recorderReceiver != null) {
                sequencer.getTransmitter().setReceiver(recorderReceiver);
            }
        }

        // initialize with selected instruments
        if (initInstruments) {
            fmgApp.getSoundbankManager().changeInstrumentsMelody();
        }
        
        synchronized (melodyMonitor) {
            // send effects directly to all receivers
            List<Transmitter> transmitterList = sequencer.getTransmitters();
            for (Transmitter transmitter : transmitterList) {
                Receiver receiver = transmitter.getReceiver();
                message = new ShortMessage(ShortMessage.CONTROL_CHANGE, channel, 91, reverb); // 91 reverb
                receiver.send(message, -1);
                message = new ShortMessage(ShortMessage.CONTROL_CHANGE, channel, 93, chorus); // 93 chorus
                receiver.send(message, -1);
            }

            // wah-wah pollution, note off management
            int transmittersCount = transmitterList.size();
            Receiver[] receiversArray = new Receiver[transmittersCount];
            for (int i = 0; i < transmittersCount; i++) {
                Transmitter transmitter = transmitterList.get(i);
                Receiver receiver = transmitter.getReceiver();
                receiversArray[i] = receiver;
                if (channelOld != null) {
                    // control change 123 value 0: All Notes Off, for previously used channel
                    // we are detaching a (potentially) playing sequencer
                    message = new ShortMessage(ShortMessage.CONTROL_CHANGE, channelOld, 123, 0);
                    receiver.send(message, -1);
                }
                transmitter.setReceiver(null); // must be synchronized!
            }
            if (preventInterruptions) {
                keepAndEnlargeSequence(sequence);
            }
            sequencer.setSequence(sequence);
            for (int i = 0; i < transmittersCount; i++) {
                Transmitter transmitter = transmitterList.get(i);
                transmitter.setReceiver(receiversArray[i]);
            }
        }
               
        if (!sequencer.isOpen()) {
            sequencer.open();
        }
        
        if (!sequencer.isRunning()) {
            sequencer.start();
        }
    }

    public void playDrums(OrbitCalculator orbitCalculator) throws InterruptedException, MidiUnavailableException, InvalidMidiDataException {
        Sequencer sequencer = null;
        int drumsDuration = fmgApp.getDrumsNoteDurationSlider().getValue();
        int drumsVelocity = fmgApp.getDrumsVelocitySlider().getValue();
        int drumsSequenceSpeed = fmgApp.getDrumsSequenceSpeedSlider().getValue();
        int drumsMaxSequencers = fmgApp.getDrumsMaxSequencersSlider().getValue();
        boolean drumsPreventInterruptions = fmgApp.getDrumsPreventInterruptionsCheckBox().isSelected();
        boolean drumsSkipDivergent = fmgApp.getDrumsSkipDivergentMidiCheckBox().isSelected();
        boolean drumsChangeEffect = fmgApp.getDrumsChangeEffectCheckBox().isSelected();
        boolean drumsPanMidiEffect = fmgApp.getDrumsPanMidiEffectCheckBox().isSelected();
        double drumsMinZxMidi = (double) fmgApp.getDrumsMinZxMidiSpinner().getValue();
        double drumsMaxZxMidi = (double) fmgApp.getDrumsMaxZxMidiSpinner().getValue();
        int drumsMinMidiEffect = (int) fmgApp.getDrumsMinMidiEffectSpinner().getValue();
        int drumsMaxMidiEffect = (int) fmgApp.getDrumsMaxMidiEffectSpinner().getValue();
        double drumsMinZyMidi = (double) fmgApp.getDrumsMinZyMidiSpinner().getValue();
        double drumsMaxZyMidi = (double) fmgApp.getDrumsMaxZyMidiSpinner().getValue();
        int drumsMinNote = (int) fmgApp.getDrumsMinNoteSpinner().getValue();
        int drumsMaxNote = (int) fmgApp.getDrumsMaxNoteSpinner().getValue();
        int channel = 9; // nine means MIDI channel number 10, drums (local variable)
        boolean verbose = fmgApp.getVerboseCheckBox().isSelected();
        boolean drumsInitInstruments = fmgApp.getDrumsInitInstrumentsCheckBox().isSelected();
        int drumsReverb = fmgApp.getDrumsReverbSlider().getValue();
        int drumsChorus = fmgApp.getDrumsChorusSlider().getValue();
        ShortMessage message;
        MidiEvent event;

        // prepare sequence drums
        ComplexNumber[] orbitArray = orbitCalculator.getOrbitsArray();
        
        // test if z belongs to the set
        if (orbitArray[orbitArray.length - 1] == null) {
            if (verbose) {
                fmgApp.getTab1TextArea().append("Pixel with diverging orbits for Drums detected! Not in set." + nl);
            }
            if (drumsSkipDivergent) {
                return;
            }
        }

        Sequence sequence = new Sequence(Sequence.PPQ, drumsSequenceSpeed);
        Track track = sequence.createTrack();
 
        for (int i = 0; i < orbitArray.length; i++) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }           
            // may not be already skipping divergent pixels
            if (orbitArray[i] == null) {
                if (drumsPreventInterruptions) {
                    // will play what already accepted
                    // will NOT send "reset" on all channels when finished
                    break;
                } else {
                    // will reset the sequencer without playing anything
                    // will send "reset" on all channels
                    sequence.deleteTrack(track);
                    break;
                }
            }
            
            // test if zy conversion is possible within particular range
            double zy = orbitArray[i].getImaginaryValue();
            if (zy < drumsMinZyMidi || zy > drumsMaxZyMidi) {
                if (verbose) {
                    fmgApp.getTab1TextArea().append("Out of range zy for drums conversion settings." + nl);
                }
                if (drumsPreventInterruptions) {
                    zy = 0;
                } else {
                    sequence.deleteTrack(track);
                    break;
                }
            }
            byte yNote = (byte) Utilities.scale(zy, drumsMinZyMidi, drumsMaxZyMidi, drumsMinNote, drumsMaxNote); 

            message = new ShortMessage(ShortMessage.NOTE_ON, channel, yNote, drumsVelocity); // yNote
            event = new MidiEvent(message, (i * 4) + 2);
            track.add(event);

            message = new ShortMessage(ShortMessage.NOTE_OFF, channel, yNote, 0); // final 0 is mandatory for Java synthesizer
            event = new MidiEvent(message, (i * 4) + 3 + (4 * drumsDuration));
            track.add(event);

            // test if zx conversion is possible within particular range
            double zx = orbitArray[i].getRealValue();
            if (zx < drumsMinZxMidi || zx > drumsMaxZxMidi) {
                if (verbose) {
                    fmgApp.getTab1TextArea().append("Out of range zx for drums conversion settings." + nl);
                }
                if (drumsPreventInterruptions) {
                    zx = 0;
                } else {
                    sequence.deleteTrack(track);
                    break;
                }
            }
            byte effectValue = (byte) Utilities.scale(zx, drumsMinZxMidi, drumsMaxZxMidi, drumsMinMidiEffect, drumsMaxMidiEffect);

            if (drumsChangeEffect) {
                message = new ShortMessage(ShortMessage.PROGRAM_CHANGE, channel, effectValue, 0); // drumkit change
                event = new MidiEvent(message, (i * 4));
                track.add(event);
            }

            if (drumsPanMidiEffect) {
                message = new ShortMessage(ShortMessage.CONTROL_CHANGE, channel, 10, effectValue); // panning
                event = new MidiEvent(message, (i * 4) + 1);
                track.add(event);
            }
        }
        
        // prepare sequencer drums
        synchronized (drumsMonitor) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            if (drumsSequencersArrayList.size() < drumsMaxSequencers) {
                sequencer = MidiSystem.getSequencer(false);
                drumsSequencersArrayList.add(sequencer);
            } else if (drumsSequencersArrayList.size() == drumsMaxSequencers) {
                sequencer = drumsSequencersArrayList.get(0);
                drumsSequencersArrayList.remove(0);
                drumsSequencersArrayList.add(sequencer);
            } else {
                int drumsSeqArrayListLength = drumsSequencersArrayList.size();
                int drumsSeqToClose = drumsSeqArrayListLength - drumsMaxSequencers;
                for (int i = 0; i < drumsSeqToClose; i++) {
                    sequencer = drumsSequencersArrayList.get(0);
                    drumsSequencersArrayList.remove(0);
                    if (sequencer.isOpen()) {
                        sequencer.stop();
                        sequencer.close();
                    }
                }
                String str = "closed " + drumsSeqToClose + " drums sequencers out of " + drumsSeqArrayListLength + nl;
                str = str + "Maximum sequencers for type Drums set to: " + drumsMaxSequencers + nl;
                fmgApp.getTab1TextArea().append(str);
            }
        }
        
        if (sequencer.getTransmitters().isEmpty()) {
            Receiver gervillReceiver = fmgApp.getDeviceManager().getGervillReceiver();
            if (gervillReceiver != null) {
                sequencer.getTransmitter().setReceiver(gervillReceiver);
            }
            Receiver externalDeviceReceiver = fmgApp.getDeviceManager().getExternalDeviceReceiver();
            if (externalDeviceReceiver != null) {
                sequencer.getTransmitter().setReceiver(externalDeviceReceiver);
            }
            Receiver jackReceiver = fmgApp.getDeviceManager().getJackReceiver();
            if (jackReceiver != null) {
                sequencer.getTransmitter().setReceiver(jackReceiver);
            }
            Receiver dumpReceiver = fmgApp.getDeviceManager().getDumpReceiver();
            if (dumpReceiver != null) {
                sequencer.getTransmitter().setReceiver(dumpReceiver);
            }
            Receiver recorderReceiver = fmgApp.getDeviceManager().getRecorderReceiver();
            if (recorderReceiver != null) {
                sequencer.getTransmitter().setReceiver(recorderReceiver);
            }
        }

        // initialize with selected drumkit
        if (drumsInitInstruments) {
            fmgApp.getSoundbankManager().changeDrumkits();
        }

        synchronized (drumsMonitor) {
            // send effects directly to all receivers
            List<Transmitter> transmitterList = sequencer.getTransmitters();
            for (Transmitter transmitter : transmitterList) {
                Receiver receiver = transmitter.getReceiver();
                message = new ShortMessage(ShortMessage.CONTROL_CHANGE, channel, 91, drumsReverb); // 91 reverb
                receiver.send(message, -1);
                message = new ShortMessage(ShortMessage.CONTROL_CHANGE, channel, 93, drumsChorus); // 93 chorus
                receiver.send(message, -1);
            }

            // wah-wah pollution, note off management 
            List<Transmitter> trasmitterList = sequencer.getTransmitters();
            int transmittersCount = trasmitterList.size();
            Receiver[] receiversArray = new Receiver[transmittersCount];
            for (int i = 0; i < transmittersCount; i++) {
                Transmitter transmitter = trasmitterList.get(i);
                Receiver receiver = transmitter.getReceiver();
                receiversArray[i] = receiver;
                // control change 123 value 0: All Notes Off, for channel 9 (drums)
                message = new ShortMessage(ShortMessage.CONTROL_CHANGE, channel, 123, 0);
                receiver.send(message, -1);
                transmitter.setReceiver(null);
            }
            if (drumsPreventInterruptions) {
                keepAndEnlargeSequence(sequence);
            }
            sequencer.setSequence(sequence);
            for (int i = 0; i < transmittersCount; i++) {
                Transmitter transmitter = trasmitterList.get(i);
                transmitter.setReceiver(receiversArray[i]);
            }
        }

        if (!sequencer.isOpen()) {
            sequencer.open();
        }
        
        if (!sequencer.isRunning()) {
            sequencer.start();
        }
    }

    public void clearAndEnlargeSequence(Sequence sequence) throws InvalidMidiDataException {
        // not used
        sequence.deleteTrack(sequence.getTracks()[0]);
        Track track = sequence.createTrack();
        // if sequence is played out sequencer will send "reset" events on all channels
        // inserting a fake MidiEvent will lengthen the track
        ShortMessage message = new ShortMessage(ShortMessage.NOTE_ON, 0, 60, 127); // channel, note, velocity
        long timeStamp = 100000000; // to keep sequencer running
        // 1M -> 0 minutes 15 seconds at 32500 tpqn // 10M -> 2:33 at 32500 // 100M should be 25,5 min at 32500
        MidiEvent event = new MidiEvent(message, timeStamp);
        track.add(event);
        track.remove(event);
    }
    
    public void keepAndEnlargeSequence(Sequence sequence) throws InvalidMidiDataException {
        Track track = sequence.getTracks()[0];
        ShortMessage message = new ShortMessage(ShortMessage.NOTE_ON, 0, 60, 127); // channel, note, velocity
        long timeStamp = 100000000;
        MidiEvent event = new MidiEvent(message, timeStamp);
        track.add(event);
        track.remove(event);
    }
    
    public void resetSoundGenerator() {
        maxSDL = fmgApp.getMaxSdlSlider().getValue();
        occupiedSDL = 0;
        requestedSDL = false;
        sdlCheck = null;
        overloadedSDL = new AtomicBoolean(false);
        seqChanHashMap.clear();
        channel = -1;
    }

    // getters and setters:
    public ArrayList<SourceDataLine> getSdlArrayList() {
        return sdlArrayList;
    }

    public ArrayList<Sequencer> getMelodySequencersArrayList() {
        return melodySequencersArrayList;
    }

    public ArrayList<Sequencer> getDrumsSequencersArrayList() {
        return drumsSequencersArrayList;
    }

    public void setMaxSDL(int maxSDL) {
        this.maxSDL = maxSDL;
    }

    public AtomicBoolean getOverloadedSDL() {
        return overloadedSDL;
    }

    // Variables:
    private String nl = System.lineSeparator();
    private FmgApp fmgApp;
    private ArrayList<Sequencer> melodySequencersArrayList = new ArrayList<>(100);
    private ArrayList<Sequencer> drumsSequencersArrayList = new ArrayList<>(100);
    private ArrayList<SourceDataLine> sdlArrayList = new ArrayList<>(100);
    private HashMap<Sequencer, Integer> seqChanHashMap = new HashMap<>();
    private int maxSDL = 1;
    private int occupiedSDL;
    private boolean requestedSDL;
    private SourceDataLine sdlCheck;
    private final Object lineMonitor = new Object();
    private final Object melodyMonitor = new Object();
    private final Object drumsMonitor = new Object();
    private AtomicBoolean overloadedSDL = new AtomicBoolean(false);
    private int channel = -1;
}