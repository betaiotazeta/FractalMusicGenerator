package org.betaiotazeta.fractalmusicgenerator;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class DeviceManager {

    public DeviceManager(FmgApp fmgApp) {
        this.fmgApp = fmgApp;
        initGervill();
    }

    // Gervill, Java internal synthesizer
    public void initGervill() {
        try {
            if (synthesizer == null) {
                synthesizer = MidiSystem.getSynthesizer();
            }
            if (!synthesizer.isOpen()) {
                synthesizer.open();
            }
            if (gervillReceiver == null) {
                gervillReceiver = synthesizer.getReceiver();
            }
        } catch (MidiUnavailableException ex) {
            fmgApp.getTab1TextArea().append("Internal Midi system not available!" + nl);
        }
    }

    public void toggleGervill() {
        if (fmgApp.getGervillToggleButton().isSelected()) {
            initGervill();
        } else {
            gervillReceiver.close();
            gervillReceiver = null;
            // if Gervill is closed instrument selection will not work
//            synthesizer.close();
        }
    }

    // External Midi devices
    public void fillExternalDevicesTable() {
        JTable devicesTable = fmgApp.getExternalDevicesTable();
        DefaultTableModel tableModel = (DefaultTableModel) devicesTable.getModel();
        tableModel.setRowCount(0);
        resetExternalDeviceReceiver();
        fmgApp.getConfigurator().setExternalDeviceName(null);
        MidiDevice.Info[] deviceInfoArray = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info deviceInfo : deviceInfoArray) {
            try {
                MidiDevice device = MidiSystem.getMidiDevice(deviceInfo);
                if (!(device instanceof Sequencer) && !(device instanceof Synthesizer)) {
                    int maxReceivers = device.getMaxReceivers();
                    String maxReceiversString;
                    if (maxReceivers != 0) {
                        if (maxReceivers == -1) {
                            maxReceiversString = "Unlimited";
                        } else {
                            maxReceiversString = maxReceivers + "";
                        }
                        tableModel.insertRow(tableModel.getRowCount(), new Object[]{
                            deviceInfo.getName(), deviceInfo.getDescription(),
                            deviceInfo.getVendor(), deviceInfo.getVersion(), maxReceiversString});
                    }
                }
            } catch (MidiUnavailableException ex) {
                fmgApp.getTab1TextArea().append("Midi device not available!" + nl);
            }
        }
    }

    public void changeExternalDevice() {
        JTable devicesTable = fmgApp.getExternalDevicesTable();
        TableModel tableModel = devicesTable.getModel();
        int index = devicesTable.getSelectedRow();
        if (index == -1) {
            resetExternalDeviceReceiver();
            fmgApp.getConfigurator().setExternalDeviceName(null);
            return;
        }
        String deviceName = tableModel.getValueAt(index, 0).toString();
        initExternalDeviceReceiver(deviceName);
        fmgApp.getConfigurator().setExternalDeviceName(deviceName);
    }

    public void selectExternalDevice(String deviceName) {
        // used when loading from configurator
        if (deviceName == null) {
            resetExternalDeviceReceiver();
            return;
        }
        fillExternalDevicesTable();
        JTable devicesTable = fmgApp.getExternalDevicesTable();
        TableModel tableModel = devicesTable.getModel();
        devicesTable.clearSelection();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (deviceName.equals(tableModel.getValueAt(i, 0))) {
                devicesTable.setRowSelectionInterval(i, i);
                break;
            }
        }
        initExternalDeviceReceiver(deviceName);
    }

    public void initExternalDeviceReceiver(String deviceName) {
        resetExternalDeviceReceiver();
        MidiDevice.Info[] deviceInfoArray = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info deviceInfo : deviceInfoArray) {
            if (deviceInfo.getName().equals(deviceName)) {
                try {
                    midiDevice = MidiSystem.getMidiDevice(deviceInfo);
                    if (midiDevice.getMaxReceivers() != 0) {
                        midiDevice.open();
                        externalDeviceReceiver = midiDevice.getReceiver();
                        break;
                    }
                } catch (MidiUnavailableException ex) {
                    fmgApp.getTab1TextArea().append("Midi device not available!" + nl);
                }
            }
        }
    }

    public void resetExternalDeviceReceiver() {
        if (externalDeviceReceiver != null) {
            externalDeviceReceiver.close();
            externalDeviceReceiver = null;
        }
        if (midiDevice != null) {
            midiDevice.close();
            midiDevice = null;
        }
    }

    // Jack
    public void initJack() {
        fmgJackClient = new FmgJackClient(fmgApp);
        jackReceiver = new JackReceiver(fmgJackClient);
    }

    public void toggleJack() {
        if (fmgApp.getJackToggleButton().isSelected()) {
            initJack();
        } else {
            jackReceiver = null;
            if (fmgJackClient != null) {
                fmgJackClient.requestShutdown();
            }
        }
    }

    // Dump midi data to jTextArea
    public void toggleDump() {
        if (fmgApp.getDumpToggleButton().isSelected()) {
            dumpReceiver = new DumpReceiver(fmgApp.getTab1TextArea(), false);
        } else {
            dumpReceiver = null;
        }
    }

    // Midi Recorder
    public void initRecorder() {
        if (midiRecorder == null) {
        midiRecorder = new MidiRecorder();
        recorderReceiver = midiRecorder.getRecorderReceiver();
        } else {
            throw new IllegalStateException("Recorder already instantiated.");
        }
    }

    public void toggleRecorder() {
        if (fmgApp.getRecorderToggleButton().isSelected()) {
            fmgApp.getTab1TextArea().append("Starting Midi Recorder!" + nl);
            initRecorder();
        } else {
            fmgApp.getTab1TextArea().append("Stopping Midi Recorder!" + nl);
            midiRecorder.writeMidiFile();
            midiRecorder.reset();
            midiRecorder = null;
            recorderReceiver = null;
        }
    }

    public void closeAll() {
        if (gervillReceiver != null) {
            gervillReceiver.close();
        }
        if (synthesizer != null) {
            synthesizer.close();
        }
        resetExternalDeviceReceiver();
        if (fmgJackClient != null) {
            fmgJackClient.requestShutdown();
        }
        if (midiRecorder != null) {
            midiRecorder.reset();
        }
    }

    // Getters
    public Synthesizer getSynthesizer() {
        return synthesizer;
    }

    public Receiver getGervillReceiver() {
        return gervillReceiver;
    }

    public Receiver getExternalDeviceReceiver() {
        return externalDeviceReceiver;
    }

    public FmgJackClient getFmgJackClient() {
        return fmgJackClient;
    }

    public JackReceiver getJackReceiver() {
        return jackReceiver;
    }

    public Receiver getDumpReceiver() {
        return dumpReceiver;
    }

    public Receiver getRecorderReceiver() {
        return recorderReceiver;
    }

    // Variables
    private String nl = System.lineSeparator();
    private FmgApp fmgApp;
    private Synthesizer synthesizer;
    private Receiver gervillReceiver;
    private FmgJackClient fmgJackClient;
    private JackReceiver jackReceiver;
    private MidiDevice midiDevice;
    private Receiver externalDeviceReceiver;
    private Receiver dumpReceiver;
    private MidiRecorder midiRecorder;
    private Receiver recorderReceiver;
}
