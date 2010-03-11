package ru.spbu.math.ontologycomparison.zhukova.visualisation.ui;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author Anna R. Zhukova
 */
public class FileChoosers {
    private static Main main;
    private static final String[] ACCEPTED_FORMATS = new String[]{".owl", ".rdf", ".rdfs", ".obo", ".n3"};

    private static final JFileChooser fileChooser = new JFileChooser("./resources/ontologyexamples");

    private static final FileFilter filter = new FileFilter() {
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String path = f.getAbsolutePath();
            for (String format : ACCEPTED_FORMATS) {
                if (path.endsWith(format)) {
                    return true;
                }
            }
            return false;
        }

        public String getDescription() {
            return null;
        }
    };

    public static void setMain(Main main) {
        FileChoosers.main = main;
    }

    public static File getOpenFileChooser(String title) {
        FileChoosers.fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        FileChoosers.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileChoosers.fileChooser.addChoosableFileFilter(FileChoosers.filter);
        int value = FileChoosers.fileChooser.showDialog(main.getFrame(), title);
        if (value == JFileChooser.APPROVE_OPTION) {
            return FileChoosers.fileChooser.getSelectedFile();
        }
        return null;
    }
}
