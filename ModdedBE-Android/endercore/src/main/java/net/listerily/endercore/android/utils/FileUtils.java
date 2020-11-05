package net.listerily.endercore.android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class FileUtils {
    public static void copyFile(File from, File to) throws IOException
    {
        FileChannel input = null;
        FileChannel output = null;

        to.getParentFile().mkdirs();
        to.createNewFile();
        input = new FileInputStream(from).getChannel();
        output = new FileOutputStream(to).getChannel();
        output.transferFrom(input, 0, input.size());
    }

    public static void copyFile(InputStream from, File to) throws IOException
    {
        copyFile(from,new FileOutputStream(to));
    }

    public static void copyFile(File from, OutputStream to) throws IOException
    {
        copyFile(new FileInputStream(from),to);
    }

    public static void copyFile(InputStream from, OutputStream to) throws IOException
    {
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
        if(listFiles != null){
            for (File file : listFiles) {
                if (file.isDirectory())
                    removeFiles(file);
                else
                    file.deleteOnExit();
            }
        }
        path.deleteOnExit();
    }
}
