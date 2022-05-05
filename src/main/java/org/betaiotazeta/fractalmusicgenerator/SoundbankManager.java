package org.betaiotazeta.fractalmusicgenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SoundbankManager {

    public SoundbankManager(FmgApp fmgApp) {
        this.fmgApp = fmgApp;
    }
    
    public void unloadAllInstruments() {
        fmgApp.getTab1TextArea().append("Unloading all instruments..." + nl);
        fmgApp.resetSound();
        Synthesizer synthesizer = fmgApp.getDeviceManager().getSynthesizer();
        Instrument[] instruments = synthesizer.getLoadedInstruments();
        for (Instrument instrument : instruments) {
            synthesizer.unloadInstrument(instrument);
        }
        fillInstrumentsMelodyList();
        fillDrumkitsList();
        fmgApp.getTab1TextArea().append("All instruments unloaded successfully!" + nl);
        fmgApp.getOutlinePlayButton().setEnabled(true);
    }
    
    public void loadDefaultSoundbank() {
        fmgApp.getTab1TextArea().append("Loading default soundfont..." + nl);
        fmgApp.resetSound();
        Synthesizer synthesizer = fmgApp.getDeviceManager().getSynthesizer();
        Soundbank soundbank = synthesizer.getDefaultSoundbank();
        synthesizer.loadAllInstruments(soundbank);       
        fillInstrumentsMelodyList();
        fillDrumkitsList();
        fmgApp.getTab1TextArea().append("Default soundfont loaded successfully!" + nl);
        fmgApp.getOutlinePlayButton().setEnabled(true);
    }
    
    public void loadSoundbank() {
        fmgApp.getTab1TextArea().append("Loading soundfont..." + nl);
        Soundbank soundbank;
        File inputSoundfontFile;
        File dataDir = new File("soundfonts");
        JFileChooser chooser = new JFileChooser(dataDir);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("SF2 Files", "sf2");
        chooser.setFileFilter(filter);
        if (chooser.showOpenDialog(fmgApp) == JFileChooser.APPROVE_OPTION) {
            inputSoundfontFile = chooser.getSelectedFile();
        } else {
            fmgApp.getTab1TextArea().append("Loading soundfont cancelled." + nl);
            return;
        }
        fmgApp.resetSound();       
        try {
            soundbank = MidiSystem.getSoundbank(inputSoundfontFile);
        } catch (InvalidMidiDataException | IOException ex) {
            String message = ex.toString();
            JOptionPane.showMessageDialog(fmgApp, message, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean supported = fmgApp.getDeviceManager().getSynthesizer().isSoundbankSupported(soundbank);
        if (supported) {
            fmgApp.getDeviceManager().getSynthesizer().loadAllInstruments(soundbank);
            fmgApp.getTab1TextArea().append("Soundfont loaded successfully!" + nl);
        } else {
            String message = "Soundbank not supported!";
            JOptionPane.showMessageDialog(fmgApp, message, "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        displaySoundbankInfo(soundbank);
        fillInstrumentsMelodyList();
        fillDrumkitsList();
        fmgApp.getOutlinePlayButton().setEnabled(true);
    }
    
    public void displaySoundbankInfo(Soundbank soundbank) {
        fmgApp.getTab1TextArea().append("--------------------------------------------------" + nl);
        fmgApp.getTab1TextArea().append("Loaded instruments:" + nl);
        Instrument[] instruments = soundbank.getInstruments();
        for (Instrument instrument : instruments) {
            fmgApp.getTab1TextArea().append(instrument.toString() + nl);
        }
        fmgApp.getTab1TextArea().append("--------------------------------------------------" + nl);
        fmgApp.getTab1TextArea().append("Name: " + soundbank.getName() + nl);
        fmgApp.getTab1TextArea().append("Description: " + soundbank.getDescription() + nl);
        fmgApp.getTab1TextArea().append("Vendor: " + soundbank.getVendor() + nl);
        fmgApp.getTab1TextArea().append("Version: " + soundbank.getVersion() + nl);
        fmgApp.getTab1TextArea().append(instruments.length + " instruments successfully loaded!" + nl);
    }    
    
    // Instrument management
    public void fillInstrumentsMelodyList() {
        JList<String> instrumentsMelodyList = fmgApp.getInstrumentsMelodyList();
        DefaultListModel<String> dlm = new DefaultListModel<>();
        ArrayList<String> instrumentNamesArrayList = sortMelodyPatches();
        for (String name : instrumentNamesArrayList) {
            dlm.addElement(name);
        }
        instrumentsMelodyList.setModel(dlm);
        instrumentsMelodyList.setSelectionModel(new FmgSelectionModel(fmgApp, instrumentsMelodyList, 15));
        instrumentsMelodyList.setSelectedIndex(0);
    }

    public void fillDrumkitsList() {
        JList<String> drumkitsList = fmgApp.getDrumkitsList();
        DefaultListModel<String> dlm = new DefaultListModel<>();
        ArrayList<String> instrumentNamesArrayList = sortDrumkitPatches();
        for (String name : instrumentNamesArrayList) {
            dlm.addElement(name);
        }
        drumkitsList.setModel(dlm);
        drumkitsList.setSelectionModel(new FmgSelectionModel(fmgApp, drumkitsList, 1));
        drumkitsList.setSelectedIndex(0);
    }    
    
    public void changeInstrumentsMelody() {
        // 15 instruments can be selected for channels 0-8 and 10-15 except for 9 (drums)
        List<String> namesList = fmgApp.getInstrumentsMelodyList().getSelectedValuesList();
        int namesSize = namesList.size();
        if (namesSize == 0) {
            return;
        }
        int index = 0;
        int channel = 0;
        for (int i = 0; i < 15; i++) {
            if (channel == 9) {
                channel++;
            }
            if (index == namesSize) {
                index = 0;
            }
            sendInstrument(namesList.get(index), channel);
            index++;
            channel++;
        }
    }

    public void changeDrumkits() {
        // only one drumkit can be selected at a time for channel 9
        List<String> namesList = fmgApp.getDrumkitsList().getSelectedValuesList();
        int namesSize = namesList.size();
        if (namesSize == 0) {
            return;
        }
        sendInstrument(namesList.get(0), 9);
    }

    public void selectInstruments(JList jList, List<String> instrumentsList) {
        // used when loading from configurator
        jList.clearSelection();
        ListModel listModel = jList.getModel();
        int selectionCount = 0;
        for (String instrumentName : instrumentsList) {
            int index = ((DefaultListModel) listModel).indexOf(instrumentName);
            if (index >= 0) {
                jList.addSelectionInterval(index, index);
                selectionCount++;
            }
        }
        if (selectionCount == 0) {
            jList.setSelectedIndex(0);
        }
    }

    public ArrayList<String> sortMelodyPatches() {        
        Instrument[] instruments = fmgApp.getDeviceManager().getSynthesizer().getLoadedInstruments();
        ArrayList<String> instrumentNamesArrayList = new ArrayList<>(instruments.length);
        for (Instrument instrument : instruments) {
            String s = instrument.toString();
            if (s.startsWith("Instrument")) {
                instrumentNamesArrayList.add(instrument.getName());
            }
        }
        return instrumentNamesArrayList;
    }
    
    public ArrayList<String> sortDrumkitPatches() {        
        Instrument[] instruments = fmgApp.getDeviceManager().getSynthesizer().getLoadedInstruments();
        ArrayList<String> instrumentNamesArrayList = new ArrayList<>(instruments.length);
        for (Instrument instrument : instruments) {
            String s = instrument.toString();
            if (s.startsWith("Drumkit")) {
                instrumentNamesArrayList.add(instrument.getName());
            }
        }
        return instrumentNamesArrayList;
    }    

    public void sendInstrument(String name, int channel) {
        if (name == null) {
            return;
        }
        Instrument selectedInstrument = null;
        Instrument[] instruments = fmgApp.getDeviceManager().getSynthesizer().getLoadedInstruments();       
        for (Instrument instrument : instruments) {
            if (name.equals(instrument.getName())) {
                selectedInstrument = instrument;
                break;
            }
        }
        if (selectedInstrument == null) {
            String message = "Error while selecting instrument!";
            JOptionPane.showMessageDialog(fmgApp, message, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int bank = selectedInstrument.getPatch().getBank();
        int program = selectedInstrument.getPatch().getProgram();

        Receiver gervillReceiver = fmgApp.getDeviceManager().getGervillReceiver();
        if (gervillReceiver != null) {
            sendInstrumentChange(gervillReceiver, channel, bank, program);
        }
        Receiver externalDeviceReceiver = fmgApp.getDeviceManager().getExternalDeviceReceiver();
        if (externalDeviceReceiver != null) {
            sendInstrumentChange(externalDeviceReceiver, channel, bank, program);
        }
        Receiver jackReceiver = fmgApp.getDeviceManager().getJackReceiver();
        if (jackReceiver != null) {
            sendInstrumentChange(jackReceiver, channel, bank, program);
        }
        Receiver dumpReceiver = fmgApp.getDeviceManager().getDumpReceiver();
        if (dumpReceiver != null) {
            sendInstrumentChange(dumpReceiver, channel, bank, program);
        }
        Receiver recorderReceiver = fmgApp.getDeviceManager().getRecorderReceiver();
        if (recorderReceiver != null) {
            sendInstrumentChange(recorderReceiver, channel, bank, program);
        }
    }
        
    public void sendInstrumentChange(Receiver receiver, int channel, int bank, int program) {
        ShortMessage message;
        try {
            message = new ShortMessage(ShortMessage.CONTROL_CHANGE, channel, 0, bank >> 7);
            receiver.send(message, -1);
            message = new ShortMessage(ShortMessage.CONTROL_CHANGE, channel, 32, bank & 0x7f);
            receiver.send(message, -1);
            message = new ShortMessage(ShortMessage.PROGRAM_CHANGE, channel, program, 0);
            receiver.send(message, -1);
        } catch (InvalidMidiDataException ex) {
            fmgApp.getTab1TextArea().append("Invalid Midi data when sending instrument change!" + nl);
        }
    }

    // Variables:
    private String nl = System.lineSeparator();
    private FmgApp fmgApp;
}
