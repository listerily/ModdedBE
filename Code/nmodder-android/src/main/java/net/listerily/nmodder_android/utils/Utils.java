package net.listerily.nmodder_android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class Utils {
    public static void copy(File from, File to) throws IOException
    {
        FileChannel input = null;
        FileChannel output = null;

        input = new FileInputStream(from).getChannel();
        output = new FileOutputStream(to).getChannel();
        output.transferFrom(input, 0, input.size());
    }

    public static void copy(InputStream from, File to) throws IOException
    {
        InputStream in = from;
        OutputStream out = null;

        out = new FileOutputStream(to);

        byte[] buffer = new byte[1 << 10];
        int len;

        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }

    public static void copy(File from, OutputStream to) throws IOException
    {
        InputStream in = null;
        OutputStream out = to;

        in = new FileInputStream(from);

        byte[] buffer = new byte[1 << 10];
        int len;

        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }
}
