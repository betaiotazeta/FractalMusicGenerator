package org.betaiotazeta.fractalmusicgenerator;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Persistence {

    public Persistence(FmgApp fmgApp) {
        this.fmgApp = fmgApp;
    }

    public void open() {
        File dataDir = new File("projects");
        JFileChooser chooser = new JFileChooser(dataDir);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
        chooser.setFileFilter(filter);
        if (chooser.showOpenDialog(fmgApp) == JFileChooser.APPROVE_OPTION) {
            File inputCfgFile = chooser.getSelectedFile();
            convert(inputCfgFile);
        } else {
            return;
        }
    }
    
    public void convert(Object object) {
        Configurator cfg = null;
        XStream xstream = new XStream();
        // customizing XStream
        xstream.alias("ApplicationSettings", Configurator.class);
        // clear out existing permissions and set own ones
        xstream.addPermission(NoTypePermission.NONE);
        // allow some basics
        xstream.allowTypeHierarchy(Collection.class);
        xstream.allowTypes(new Class[]{java.lang.String.class, org.betaiotazeta.fractalmusicgenerator.Configurator.class});
        try {
            if (object instanceof File) {
                cfg = (Configurator) xstream.fromXML((File) object);
            }
            if (object instanceof String) {
                InputStream inputStream = getClass().getResourceAsStream((String) object);
                cfg = (Configurator) xstream.fromXML(inputStream);
            }
        } catch (Exception ex) {
            String message = ex.toString();
            JOptionPane.showMessageDialog(fmgApp, message, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        fmgApp.setConfigurator(cfg);
        fmgApp.updateGuiFromConfigurator();
        fmgApp.getSoundGenerator().setMaxSDL(cfg.getMaxSdlSlider());
        fmgApp.getColorPanel().createPalette();
        fmgApp.setupGui();
        fmgApp.repaint();
    }
    
    public void save() {
        fmgApp.updateConfiguratorFromGui();
        File dataDir = new File("projects");
        JFileChooser chooser = new JFileChooser(dataDir);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
        chooser.setFileFilter(filter);
        if (chooser.showSaveDialog(fmgApp) == JFileChooser.APPROVE_OPTION) {
            File outputCfgFile = chooser.getSelectedFile();
            String filePath = outputCfgFile.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".xml")) {
                filePath += ".xml";
                outputCfgFile = new File(filePath);
            }
            XStream xstream = new XStream();
            // customizing XStream
            xstream.alias("ApplicationSettings", Configurator.class);
            String xml = xstream.toXML(fmgApp.getConfigurator());
            Path outputCfgPath = outputCfgFile.toPath();
            try (BufferedWriter writer = Files.newBufferedWriter(outputCfgPath, StandardCharsets.UTF_8)) {
                writer.write(xml);
            } catch (IOException ex) {
                String message = ex.getMessage();
                JOptionPane.showMessageDialog(fmgApp, message, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void saveOnExit() {
        String title = "Closing Application";
        String message = "Save settings before exit?";
        int reply = JOptionPane.showConfirmDialog(fmgApp, message, title, JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            save();
        }
    }
    
    // Variables:
    private FmgApp fmgApp;
}