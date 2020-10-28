package com.frimo.codesaver.filemanager;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtility
{
    public void zip(File file, File zipArchive) throws IOException {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipArchive));
        doZip(file, new File(file.getName()), out);
        out.close();
    }

    private static void doZip(File absolutePath, File path, ZipOutputStream out) throws IOException {
        if (absolutePath.isDirectory())
            for (File f : absolutePath.listFiles())
                doZip(f, new File(path + File.separator + f.getName()), out);
        else {
            out.putNextEntry(new ZipEntry(path.getPath()));
            write(new FileInputStream(absolutePath), out);
        }
    }

    private static void write(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0)
            out.write(buffer, 0, len);
        in.close();
    }
}