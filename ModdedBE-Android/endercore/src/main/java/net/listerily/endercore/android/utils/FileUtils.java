package net.listerily.endercore.android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

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
        to.getParentFile().mkdirs();
        to.createNewFile();
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

    public static String readJsonToString(File file) throws IOException
    {
        return readJsonToString(new FileInputStream(file));
    }

    public static String readJsonToString(InputStream input) throws IOException
    {
        byte[] buffer = new byte[1 << 10];
        int len;
        StringBuilder builder = new StringBuilder();
        while ((len = input.read(buffer)) > 0) {
            builder.append(new String(buffer,len));
        }
        return builder.toString();
    }

    public static String getDigestMD5(File path) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        ArrayList<File> existedFiles = new ArrayList<>();
        Queue<File> directories = new LinkedList<>();
        if(path.isDirectory())
            directories.add(path);
        else
            existedFiles.add(path);
        while(!directories.isEmpty())
        {
            File front = directories.poll();
            if(front == null)
                break;
            if(front.isDirectory())
            {
                File[] listFiles = front.listFiles();
                if(listFiles != null){
                    directories.addAll(Arrays.asList(listFiles));
                }
            }
            else
                existedFiles.add(front);
        }
        int i = 0;
        while(!existedFiles.isEmpty())
        {
            digest.update(existedFiles.get(i).getAbsolutePath().getBytes());
            InputStream input = new FileInputStream(existedFiles.get(i));
            byte[] buffer = new byte[1 << 10];
            int len;
            while ((len = input.read(buffer)) > 0) {
                digest.update(buffer,0,len);
            }
            i++;
        }
        return digest.toString();
    }

    public static String getDigestSHA(File path) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA");
        ArrayList<File> existedFiles = new ArrayList<>();
        Queue<File> directories = new LinkedList<>();
        if(path.isDirectory())
            directories.add(path);
        else
            existedFiles.add(path);
        while(!directories.isEmpty())
        {
            File front = directories.poll();
            if(front == null)
                break;
            if(front.isDirectory())
            {
                File[] listFiles = front.listFiles();
                if(listFiles != null){
                    directories.addAll(Arrays.asList(listFiles));
                }
            }
            else
                existedFiles.add(front);
        }
        int i = 0;
        while(!existedFiles.isEmpty())
        {
            digest.update(existedFiles.get(i).getAbsolutePath().getBytes());
            InputStream input = new FileInputStream(existedFiles.get(i));
            byte[] buffer = new byte[1 << 10];
            int len;
            while ((len = input.read(buffer)) > 0) {
                digest.update(buffer,0,len);
            }
            i++;
        }
        return digest.toString();
    }
}
