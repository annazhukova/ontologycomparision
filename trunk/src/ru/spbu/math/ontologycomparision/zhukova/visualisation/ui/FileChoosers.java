package ru.spbu.math.ontologycomparision.zhukova.visualisation.ui;

import ru.spbu.math.ontologycomparision.zhukova.visualisation.reading.DependencyGraphReader;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.saving.DependencyGraphSaver;
import ru.spbu.math.ontologycomparision.zhukova.visualisation.model.impl.GraphModel;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author Anna R. Zhukova
 */
public class FileChoosers {
    private static Main main;

    private static final JFileChooser fileChooser = new JFileChooser();

    private static final FileFilter filter = new FileFilter() {
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String path = f.getAbsolutePath();
            return path.endsWith(".owl");
        }

        public String getDescription() {
            return null;
        }
    };

    public static void setMain(Main main) {
        FileChoosers.main = main;
    }

    public static void getOpenSavedFileChooser() {
        FileChoosers.fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        FileChoosers.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileChoosers.fileChooser.removeChoosableFileFilter(FileChoosers.filter);
        int value = fileChooser.showDialog(main.getFrame(), "Select File");
        if (value == JFileChooser.APPROVE_OPTION) {
            try {
                FileChoosers.main.getGraphPane().deselectVertices();
                GraphModel gm = DependencyGraphReader.read(main.getGraphPane(),
                        FileChoosers.fileChooser.getSelectedFile());
                FileChoosers.main.setGraphModel(gm);
                FileChoosers.main.setIsChanged(true);
            } catch (Exception exc) {
                exc.printStackTrace();
                JOptionPane.showMessageDialog(FileChoosers.fileChooser, "Cannot open this file");
            }
        }
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

    public static void getSaveFileChooser(boolean exitAfterSaving) {
        FileChoosers.fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        FileChoosers.fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FileChoosers.fileChooser.removeChoosableFileFilter(FileChoosers.filter);
        int value = FileChoosers.fileChooser.showDialog(FileChoosers.main.getFrame(),
                "Select File");
        if (value == JFileChooser.APPROVE_OPTION) {
            File f = FileChoosers.fileChooser.getSelectedFile();
            if (f != null) {
                try {
                    if (f.isFile()) {
                        int replace = JOptionPane.showConfirmDialog(FileChoosers.fileChooser,
                                "File " + f.getName() + " already exists. Replace it?");
                        if (replace == JOptionPane.OK_OPTION) {
                            DependencyGraphSaver.save(FileChoosers.main.getGraphModel(), f);
                        }
                    } else {
                        f.createNewFile();
                        DependencyGraphSaver.save(main.getGraphModel(), f);
                    }
                    if (exitAfterSaving) {
                        System.exit(0);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(FileChoosers.fileChooser, "Cannot save");
                }
            }
        }
    }
}
