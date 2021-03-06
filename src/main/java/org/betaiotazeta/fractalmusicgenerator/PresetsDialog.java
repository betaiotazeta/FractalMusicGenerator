package org.betaiotazeta.fractalmusicgenerator;

import java.util.concurrent.ThreadPoolExecutor;
import javax.swing.JOptionPane;

public class PresetsDialog extends javax.swing.JDialog {

    public PresetsDialog(FmgApp fmgApp, boolean modal) {
        super(fmgApp, modal);
        initComponents();
        setLocationRelativeTo(fmgApp);
        this.fmgApp = fmgApp;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        presetsPanel = new javax.swing.JPanel();
        preset01Button = new javax.swing.JButton();
        preset02Button = new javax.swing.JButton();
        preset03Button = new javax.swing.JButton();
        preset04Button = new javax.swing.JButton();
        preset05Button = new javax.swing.JButton();
        preset06Button = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Presets");

        presetsPanel.setLayout(new java.awt.GridLayout(2, 3, 10, 10));

        preset01Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/betaiotazeta/fractalmusicgenerator/images/presets/01_mandelbrot.png"))); // NOI18N
        preset01Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preset01ButtonActionPerformed(evt);
            }
        });
        presetsPanel.add(preset01Button);

        preset02Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/betaiotazeta/fractalmusicgenerator/images/presets/02_tricorn.png"))); // NOI18N
        preset02Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preset02ButtonActionPerformed(evt);
            }
        });
        presetsPanel.add(preset02Button);

        preset03Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/betaiotazeta/fractalmusicgenerator/images/presets/03_logaritmic.png"))); // NOI18N
        preset03Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preset03ButtonActionPerformed(evt);
            }
        });
        presetsPanel.add(preset03Button);

        preset04Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/betaiotazeta/fractalmusicgenerator/images/presets/04_rectangle.png"))); // NOI18N
        preset04Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preset04ButtonActionPerformed(evt);
            }
        });
        presetsPanel.add(preset04Button);

        preset05Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/betaiotazeta/fractalmusicgenerator/images/presets/05_klingon.png"))); // NOI18N
        preset05Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preset05ButtonActionPerformed(evt);
            }
        });
        presetsPanel.add(preset05Button);

        preset06Button.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/betaiotazeta/fractalmusicgenerator/images/presets/06_crown.png"))); // NOI18N
        preset06Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preset06ButtonActionPerformed(evt);
            }
        });
        presetsPanel.add(preset06Button);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(presetsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(presetsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void preset01ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preset01ButtonActionPerformed
        String path = "/org/betaiotazeta/fractalmusicgenerator/presets/01_mandelbrot.xml";
        executePreset(path);
    }//GEN-LAST:event_preset01ButtonActionPerformed

    private void preset02ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preset02ButtonActionPerformed
        String path = "/org/betaiotazeta/fractalmusicgenerator/presets/02_tricorn.xml";
        executePreset(path);
    }//GEN-LAST:event_preset02ButtonActionPerformed

    private void preset03ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preset03ButtonActionPerformed
        String path = "/org/betaiotazeta/fractalmusicgenerator/presets/03_logaritmic.xml";
        executePreset(path);
    }//GEN-LAST:event_preset03ButtonActionPerformed

    private void preset04ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preset04ButtonActionPerformed
        String path = "/org/betaiotazeta/fractalmusicgenerator/presets/04_rectangle.xml";
        executePreset(path);
    }//GEN-LAST:event_preset04ButtonActionPerformed

    private void preset05ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preset05ButtonActionPerformed
        String path = "/org/betaiotazeta/fractalmusicgenerator/presets/05_klingon.xml";
        executePreset(path);
    }//GEN-LAST:event_preset05ButtonActionPerformed

    private void preset06ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preset06ButtonActionPerformed
        String path = "/org/betaiotazeta/fractalmusicgenerator/presets/06_crown.xml";
        executePreset(path);
    }//GEN-LAST:event_preset06ButtonActionPerformed

    public void executePreset(String path) {
        this.dispose();
        fmgApp.getResetButton().doClick();
        fmgApp.getPersistence().convert(path);
        fmgApp.getTab1TextArea().append("Setting up preset, please wait..." + nl);
        
        // before clicking play the new fractal image must be rendered first
        Runnable presetPlay = new Runnable() {
            @Override
            public void run() {
                int timeOut = 0;
                int threadFractalCount;
                do {
                    try {
                        Thread.sleep(100);
                        timeOut = timeOut + 100;
                    } catch (InterruptedException ex) {
                        return;
                    }
                    if (timeOut == 3000) {
                        String message = "Selected preset has not been properly initialized!";
                        JOptionPane.showMessageDialog(fmgApp, message, "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    threadFractalCount = ((ThreadPoolExecutor) fmgApp.getFractalExecutorService()).getActiveCount();
                } while (threadFractalCount > 0);
                fmgApp.getOutlinePlayButton().doClick();
                // serves as an example of snapshotting the outline and then,
                // while in unlocked mode, playing internally
                if (path.contains("04_rectangle")) {
                    // loaded fractal is smaller, zooming in with unlocked outline
                    // following values are good looking
                    fmgApp.getFractalPanel().setZoom(0.004256910950646845);
                    fmgApp.getFractalPanel().setMinA(-3.1186029263288773);
                    fmgApp.getFractalPanel().setMaxB(1.412123471429728);
                    fmgApp.getFractalPanel().redrawImages();
                }
                fmgApp.setLocationRelativeTo(null);
            }
        };    
        Thread presetPlayThread = new Thread(presetPlay);
        presetPlayThread.start();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton preset01Button;
    private javax.swing.JButton preset02Button;
    private javax.swing.JButton preset03Button;
    private javax.swing.JButton preset04Button;
    private javax.swing.JButton preset05Button;
    private javax.swing.JButton preset06Button;
    private javax.swing.JPanel presetsPanel;
    // End of variables declaration//GEN-END:variables

    // Custom variables:
    private String nl = System.lineSeparator();
    private FmgApp fmgApp;
}
