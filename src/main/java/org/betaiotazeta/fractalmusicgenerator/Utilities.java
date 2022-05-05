package org.betaiotazeta.fractalmusicgenerator;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class Utilities {

    // baseMin, baseMax: min, max of the origin interval
    // limitMin, limitMax: min, max of the destination interval
    public static double scale(final double valueIn, final double baseMin, final double baseMax, final double limitMin, final double limitMax) {
        return ((limitMax - limitMin) * (valueIn - baseMin) / (baseMax - baseMin)) + limitMin;
    }

    public static void setTextAreaBufferLimiter(JTextArea jTextArea) {
        jTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (jTextArea.getDocument().getLength() > TEXTBUFFER) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if (jTextArea.getDocument().getLength() > TEXTBUFFER) {
                                try {
                                    jTextArea.getDocument().remove(0, jTextArea.getDocument().getLength() / 2);
                                } catch (BadLocationException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // do nothing
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // not fired for PlainDocument (default for jTextArea)
            }

        });
    }
    
    // Variables
    private static final int TEXTBUFFER = 20000;
}
