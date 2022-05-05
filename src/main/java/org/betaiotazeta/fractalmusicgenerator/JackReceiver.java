package org.betaiotazeta.fractalmusicgenerator;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class JackReceiver implements Receiver {

    public JackReceiver(FmgJackClient fmgJackClient) {
        this.fmgJackClient = fmgJackClient;
    }
    
    @Override
    public void send(MidiMessage message, long timeStamp) {
        fmgJackClient.addMidiMessage(message);
    }

    @Override
    public void close() {
        // do nothing
    }
    
    // Variables
    FmgJackClient fmgJackClient;
}
