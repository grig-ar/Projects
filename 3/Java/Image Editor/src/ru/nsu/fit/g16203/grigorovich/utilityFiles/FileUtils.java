package ru.nsu.fit.g16203.grigorovich.utilityFiles;

import java.io.File;
import java.util.Objects;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

public class FileUtils {
    private static File dataDirectory = null;
    private static File lastOpen = null;
    private static File lastSave = null;

    public static void getDataDirectory() {
        if (dataDirectory == null) {
            dataDirectory = new File(System.getProperty("user.dir"));
            if (!dataDirectory.exists()) dataDirectory = new File(".");
            for (File f : Objects.requireNonNull(dataDirectory.listFiles())) {
                if (f.isDirectory() && f.getName().endsWith("_Data")) {
                    dataDirectory = f;
                    break;
                }
            }
        }
        lastOpen = dataDirectory;
        lastSave = dataDirectory;
    }


    public static File getSaveFileName(JFrame parent, String extension, String description) {
        JFileChooser fileChooser = new JFileChooser();
        FileFilter filter = new ExtensionFileFilter(extension, description);
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setCurrentDirectory(lastSave);
        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            if (!f.getName().contains("."))
                f = new File(f.getParent(), f.getName() + "." + extension);
            lastSave = f.getParentFile();
            return f;
        }
        return null;
    }

    public static File getOpenFileName(JFrame parent, String extension, String description) {
        JFileChooser fileChooser = new JFileChooser();
        FileFilter filter = new ExtensionFileFilter(extension, description);
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setCurrentDirectory(lastOpen);
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            if (!f.getName().contains("."))
                f = new File(f.getParent(), f.getName() + "." + extension);
            lastOpen = f.getParentFile();
            return f;
        }
        return null;
    }
}
