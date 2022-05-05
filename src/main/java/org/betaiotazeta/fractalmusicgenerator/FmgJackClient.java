package org.betaiotazeta.fractalmusicgenerator;

import java.util.EnumSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.sound.midi.MidiMessage;
import javax.swing.JOptionPane;
import org.jaudiolibs.jnajack.Jack;
import org.jaudiolibs.jnajack.JackClient;
import org.jaudiolibs.jnajack.JackException;
import org.jaudiolibs.jnajack.JackMidi;
import org.jaudiolibs.jnajack.JackOptions;
import org.jaudiolibs.jnajack.JackPort;
import org.jaudiolibs.jnajack.JackPortFlags;
import org.jaudiolibs.jnajack.JackPortType;
import org.jaudiolibs.jnajack.JackProcessCallback;
import org.jaudiolibs.jnajack.JackShutdownCallback;
import org.jaudiolibs.jnajack.JackStatus;

public class FmgJackClient implements JackProcessCallback, JackShutdownCallback {

    public FmgJackClient(FmgApp fmgApp) {
        this.fmgApp = fmgApp;
        messageQueue = new ConcurrentLinkedQueue<>();
        EnumSet<JackOptions> options = EnumSet.of(JackOptions.JackNoStartServer);
        EnumSet<JackStatus> status = EnumSet.noneOf(JackStatus.class);
        EnumSet<JackPortFlags> outputFlags = EnumSet.of(JackPortFlags.JackPortIsOutput);
        try {
            Jack jack = Jack.getInstance();
            jackClient = jack.openClient("FmgJackMidi", options, status);
            if (!status.isEmpty()) {
                fmgApp.getTab1TextArea().append("JACK client status: " + status + nl);
            }
            outputMidiJackPort = jackClient.registerPort("MIDI out", JackPortType.MIDI, outputFlags);
            jackClient.setProcessCallback(this);
            jackClient.onShutdown(this);
            jackClient.activate();
        } catch (JackException ex) {
            if (!status.isEmpty()) {
                fmgApp.getTab1TextArea().append("JACK client status: " + status + nl);
            }
            String message = ex.getMessage();
            JOptionPane.showMessageDialog(fmgApp, message, "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public boolean process(JackClient client, int nframes) {
        try {
            JackMidi.clearBuffer(outputMidiJackPort);
            while (messageQueue.size() > 0) {
                midiMessage = messageQueue.remove();
                JackMidi.eventWrite(outputMidiJackPort, 0, midiMessage.getMessage(), midiMessage.getLength());
            }
            return true;
        } catch (JackException ex) {
            String message = ex.getMessage();
            JOptionPane.showMessageDialog(fmgApp, message, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    @Override
    public void clientShutdown(JackClient client) {
        String message = "Jack Client Shutdown!";
        JOptionPane.showMessageDialog(fmgApp, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void requestShutdown() {
        if (jackClient != null) {
            jackClient.deactivate();
            jackClient.close();
        }
    }   
    
    public void addMidiMessage(MidiMessage midiMessage) {
        messageQueue.add(midiMessage);
    }
    
    // Variables
    private FmgApp fmgApp;
    private String nl = System.lineSeparator();
    private JackClient jackClient;
    private JackPort outputMidiJackPort;
    private ConcurrentLinkedQueue<MidiMessage> messageQueue;
    private MidiMessage midiMessage;
}
