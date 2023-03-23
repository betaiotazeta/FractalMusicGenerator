package org.betaiotazeta.fractalmusicgenerator;

import com.aparapi.Kernel;
import com.aparapi.Range;
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
    
    public static int testExecutionMode() {
        float checkValue = 0;
        final float[] firstArray = {1, 2, 3};
        final float[] secondArray = {4, 5, 6};
        // first and second array length must be the same
        final float[] calculatedArray = new float[firstArray.length];

        Kernel kernel = new Kernel() {
            @Override
            public void run() {
                int gid = getGlobalId();
                calculatedArray[gid] = firstArray[gid] + secondArray[gid];
            }
        };

        Range range = Range.create(calculatedArray.length);

        kernel.setExecutionMode(Kernel.EXECUTION_MODE.GPU);
        try {
            System.out.println("Testing GPU execution mode...");
            kernel.execute(range);
        } catch (Exception ex) {
            String message = "Error, something went wrong while running Aparapi in GPU execution mode: ";
            System.out.println(message + ex.getMessage());
        }
        checkValue = calculatedArray[0] + calculatedArray[1] + calculatedArray[2];
        System.out.println("Calculated GPU value is: " + checkValue);
        if (checkValue == 21) { // 1+4 + 2+5 + 3+6
            kernel.dispose();
            return 0;

        } else {
            kernel.dispose();
            kernel.setExecutionMode(Kernel.EXECUTION_MODE.AUTO);
            try {
                System.out.println("Testing AUTO execution mode...");
                kernel.execute(range);
                kernel.execute(range); // two times
            } catch (Exception ex) {
                String message = "Warning, exception while running Aparapi in AUTO execution mode: ";
                System.out.println(message + ex.getMessage());
            }
            checkValue = calculatedArray[0] + calculatedArray[1] + calculatedArray[2];
            System.out.println("Calculated AUTO value is: " + checkValue);
            if (checkValue == 21) {
                kernel.dispose();
                return 1;

            } else {
                kernel.dispose();
                kernel.setExecutionMode(Kernel.EXECUTION_MODE.JTP);
                try {
                    System.out.println("Testing JTP execution mode...");
                    kernel.execute(range);
                } catch (Exception ex) {
                    String message = "Error, something went wrong while running Aparapi in JTP execution mode: ";
                    System.out.println(message + ex.getMessage());
                }
                checkValue = calculatedArray[0] + calculatedArray[1] + calculatedArray[2];
                System.out.println("Calculated JTP value is: " + checkValue);
                if (checkValue == 21) {
                    kernel.dispose();
                    return 2;

                } else {
                    String message = "Error, cannot find a suitable execution mode!";
                    System.out.println(message);
                    kernel.dispose();
                    return 3;
                }
            }
        }
    }

    // Variables
    private static final int TEXTBUFFER = 20000;
}
