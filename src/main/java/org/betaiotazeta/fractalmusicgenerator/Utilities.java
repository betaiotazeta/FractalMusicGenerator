package org.betaiotazeta.fractalmusicgenerator;

import com.formdev.flatlaf.util.SystemInfo;
import java.awt.Desktop;
import java.awt.desktop.AboutEvent;
import java.awt.desktop.AboutHandler;
import java.awt.desktop.QuitEvent;
import java.awt.desktop.QuitHandler;
import java.awt.desktop.QuitResponse;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
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

    public static boolean isMacDarkMode() {
        try {
            Process process = new ProcessBuilder("defaults", "read", "-g", "AppleInterfaceStyle").start();
            return process.waitFor() == 0;
        } catch (Exception ex) {
            return false;
        }
    }

    public static void setupMacGuiExtra(FmgApp fmgApp) {
        if (SystemInfo.isMacOS) {
            if (SystemInfo.isMacFullWindowContentSupported) {
                // Enable transparent title bar
                fmgApp.getRootPane().putClientProperty("apple.awt.transparentTitleBar", true);
                // Support full screen mode instead of just zoom (for Java 8 - 10; not necessary for Java 11+)
                fmgApp.getRootPane().putClientProperty("apple.awt.fullscreenable", true);
            }
            // MacOS adds an "Application menu" with: About, Preferences (hidden) and Quit.
            // When screen (global) menu bar is enabled the app menu is also moved up.
            // About dialog
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.APP_ABOUT)) {
                desktop.setAboutHandler(new AboutHandler() {
                    @Override
                    public void handleAbout(AboutEvent e) {
                        AboutDialog dialog = new AboutDialog(fmgApp, true);
                        dialog.setVisible(true);
                    }
                });
                fmgApp.getAboutMenuItem().setVisible(false);
            }
            // Quit
            if (desktop.isSupported(Desktop.Action.APP_QUIT_HANDLER)) {
                desktop.setQuitHandler(new QuitHandler() {
                    @Override
                    public void handleQuitRequestWith(QuitEvent e, QuitResponse response) {
                        if (fmgApp.requestQuit()) {
                            // response.performQuit(); // Works, but we prefer only one shutdown mechanism
                            response.cancelQuit(); // prevent AWT's default quit handling
                            fmgApp.dispose();
                            System.exit(0);
                        } else {
                            response.cancelQuit();
                        }
                    }
                });
            }
        }
    }

    public static File findFileForMacAppImage(String folderName) {
        if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
            return null;
        }

        try {
            Optional<String> execPathnameOpt = ProcessHandle.current().info().command();
            if (execPathnameOpt.isEmpty()) {
                return null;
            }
            Path execPathname = Paths.get(execPathnameOpt.get());
            // .../FractalMusicGenerator.app/Contents/MacOS/FractalMusicGenerator
            Path macOsDir = execPathname.getParent();
            if (macOsDir == null) {
                return null;
            }
            Path contentsDir = macOsDir.getParent();
            if (contentsDir == null) {
                return null;
            }
            Path appBundle = contentsDir.getParent();
            if ((appBundle == null) || (!appBundle.getFileName().toString().endsWith(".app"))) {
                return null;
            }
            File dataDir = appBundle.resolveSibling(folderName).toFile();
            return dataDir.isDirectory() ? dataDir : null;
        } catch (Exception ex) {
            return null;
        }
    }

    // Variables
    private static final int TEXTBUFFER = 20000;
}
