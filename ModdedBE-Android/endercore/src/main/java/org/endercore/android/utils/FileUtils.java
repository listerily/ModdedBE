package org.endercore.android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public final class FileUtils {
    public static void copy(File from, File to) throws IOException {
        if (!from.exists())
            throw new IOException("File " + from.getAbsolutePath() + " does not exists.");
        if (!to.exists()) {
            if (to.getParentFile() != null && !to.getParentFile().exists()) {
                boolean mkdirsResult = to.getParentFile().mkdirs();
                if (!mkdirsResult)
                    throw new IOException("Failed to mkdirs: " + to.getParentFile().getAbsolutePath() + ".");
            }
            boolean createdNewFile = to.createNewFile();
            if (!createdNewFile)
                throw new IOException("Failed to create new file at: " + to.getAbsolutePath() + ".");
        }
        FileChannel input = new FileInputStream(from).getChannel();
        FileChannel output = new FileOutputStream(to).getChannel();
        output.transferFrom(input, 0, input.size());
        input.close();
        output.close();
    }

    public static void copy(InputStream from, File to) throws IOException {
        if (!to.exists()) {
            if (to.getParentFile() != null && !to.getParentFile().exists()) {
                boolean mkdirsResult = to.getParentFile().mkdirs();
                if (!mkdirsResult)
                    throw new IOException("Failed to mkdirs: " + to.getParentFile().getAbsolutePath() + ".");
            }
            boolean createdNewFile = to.createNewFile();
            if (!createdNewFile)
                throw new IOException("Failed to create new file at: " + to.getAbsolutePath() + ".");
        }
        FileOutputStream output = new FileOutputStream(to);
        copy(from, output);
        output.close();
    }

    public static void copy(File from, OutputStream to) throws IOException {
        FileInputStream input = new FileInputStream(from);
        copy(input, to);
        input.close();
    }

    public static void copy(InputStream from, OutputStream to) throws IOException {
        byte[] buffer = new byte[1 << 10];
        int len;
        while ((len = from.read(buffer)) > 0) {
            to.write(buffer, 0, len);
        }
    }

    public static void removeFiles(File path) {
        if (!path.exists())
            return;
        File[] listFiles = path.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory())
                    removeFiles(file);
                else
                    file.delete();
            }
        }
        path.delete();
    }

    public static String readFileAsString(File file) throws IOException {
        if (!file.exists())
            throw new IOException("File " + file.getAbsolutePath() + " dose not exists.");
        return readFileAsString(new FileReader(file));
    }

    public static String readFileAsString(FileReader input) throws IOException {
        char[] buffer = new char[1024];
        int len;
        StringBuilder builder = new StringBuilder();
        while ((len = input.read(buffer)) > 0) {
            builder.append(buffer, 0, len);
        }
        input.close();
        return builder.toString();
    }
}
