package org.betaiotazeta.fractalmusicgenerator;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

// https://stackoverflow.com/questions/9917675/jlist-limit-maximum-number-selected-elements
// chenyi1976
public class FmgSelectionModel extends DefaultListSelectionModel {

    public FmgSelectionModel(FmgApp fmgApp, JList list, int maxCount) {
        this.fmgApp = fmgApp;
        this.list = list;
        this.maxCount = maxCount;
    }

    @Override
    public void setSelectionInterval(int index0, int index1) {
        if (index1 - index0 >= maxCount) {
            index1 = index0 + maxCount - 1;
            displayWarning();
        }
        if (Math.abs(index1 - index0) >= maxCount) {
            index1 = index0 - maxCount + 1;
            displayWarning();
        }
        super.setSelectionInterval(index0, index1);
    }

    @Override
    public void addSelectionInterval(int index0, int index1) {
        int selectionLength = list.getSelectedIndices().length;
        if (selectionLength >= maxCount) {
            displayWarning();
            return;
        }
        super.addSelectionInterval(index0, index1);
    }

    public void displayWarning() {
        String message = "All available selection choices already filled!";
        JOptionPane.showMessageDialog(fmgApp, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    // Variables
    private FmgApp fmgApp;
    private JList list;
    private int maxCount;
}
