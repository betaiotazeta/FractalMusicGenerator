package org.betaiotazeta.fractalmusicgenerator;

import java.io.File;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MidiRecorder {

    public MidiRecorder() {
        try {
            initRecorder();
        } catch (Exception ex) {
            String message = ex.toString();
            JOptionPane.showMessageDialog(fmgApp, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void initRecorder() throws MidiUnavailableException, InvalidMidiDataException {
        recorderSequencer = MidiSystem.getSequencer(false);
        recorderSequencer.open();
        recorderReceiver = recorderSequencer.getReceiver();

        int tracksQuantity = 17; // 16 tracks + 1 fake
        int ticksPerQuarterNote = 32500; // 24 // 480 // for vlc 32500 is still ok, 33000 and up isn't accepted
        Sequence sequence = new Sequence(Sequence.PPQ, ticksPerQuarterNote, tracksQuantity);

        // if sequence is empty sequencer will stop running
        // inserting a fake MidiEvent will lengthen the track
        ShortMessage message = new ShortMessage(ShortMessage.NOTE_ON, 0, 60, 127); // channel, note, velocity
        long timeStamp = 100000000; // to keep sequencer running
        // 1M -> 0 minutes 15 seconds at 32500 tpqn // 10M -> 2:33 at 32500 // 100M should be 25,5 min at 32500
        MidiEvent event = new MidiEvent(message, timeStamp);
        Track track = sequence.getTracks()[16];
        track.add(event);
        track.remove(event);

        recorderSequencer.setSequence(sequence);

        Track[] tracksArray = sequence.getTracks();
        for (int i = 0; i < tracksQuantity - 1; i++) {
            track = tracksArray[i];
            recorderSequencer.recordEnable(track, i); // -1 means all channels
        }

        recorderSequencer.startRecording();
    }

    public void reset() {
        recorderSequencer.stop();
        recorderSequencer.close();
        recorderSequencer = null;
        recorderReceiver = null;
    }

    public void writeMidiFile() {
        String title = "Save";
        String message = "Save Midi file?";
        int reply = JOptionPane.showConfirmDialog(fmgApp, message, title, JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.NO_OPTION) {
            return;
        }

        Sequence sequence = recorderSequencer.getSequence();

        // remove last fake track
        Track track = sequence.getTracks()[16];
        sequence.deleteTrack(track);
        
        track = sequence.getTracks()[0];
        if (track.ticks() == 100000000) {
            message = "The generated Midi file may contain errors!";
            JOptionPane.showMessageDialog(fmgApp, message, "Warning", JOptionPane.WARNING_MESSAGE);
        }
        
        File dataDir = new File("midi");
        JFileChooser chooser = new JFileChooser(dataDir);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("MIDI Files", "mid");
        chooser.setFileFilter(filter);
        if (chooser.showSaveDialog(fmgApp) == JFileChooser.APPROVE_OPTION) {
            File outputMidiFile = chooser.getSelectedFile();
            String filePath = outputMidiFile.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".mid")) {
                filePath += ".mid";
                outputMidiFile = new File(filePath);
            }
         
            try {
                MidiSystem.write(sequence, 1, outputMidiFile);
            } catch (Exception ex) {
                message = ex.toString();
                JOptionPane.showMessageDialog(fmgApp, message, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Getters
    public Receiver getRecorderReceiver() {
        return recorderReceiver;
    }

    // Variables
    private String nl = System.lineSeparator();
    private FmgApp fmgApp;
    private Sequencer recorderSequencer;
    private Receiver recorderReceiver;
}
