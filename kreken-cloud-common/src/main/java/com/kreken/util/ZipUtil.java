package com.kreken.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    /**
     *
     *
     * @param inFile
     * @param zos
     * @param dir
     * @throws IOException
     */
    public final static void zipFile(File inFile, ZipOutputStream zos, String dir) throws IOException {
        if (inFile.isDirectory()) {
            File[] files = inFile.listFiles();
            if (files != null && files.length > 0 ) {
                for (File file : files) {
                    String name = inFile.getName();
                    if (!"".equals(dir)) {
                        name = dir + "/" + name;
                    }
                    zipFile(file, zos, name);
                }
            }
        } else {
            String entryName = null;
            if (!"".equals(dir)) {
                entryName = dir + "/" + inFile.getName();
            } else {
                entryName = inFile.getName();
            }
            ZipEntry entry = new ZipEntry(entryName);
            zos.putNextEntry(entry);
            try (InputStream is = new FileInputStream(inFile)) {
                int len = 0;
                while ((len = is.read()) != -1) {
                    zos.write(len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文档解压
     *
     * @param source 需要解压缩的文档名称
     * @param path   需要解压缩的路径
     */
    public final static void unCompress(File source, String path) throws IOException {
        ZipEntry zipEntry = null;
        FileUtil.createPaths(path);
        try (
                ZipFile zipFile = new ZipFile(source);
                ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(source))
        ) {
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String fileName = zipEntry.getName();
                File temp = new File(path + "/" + fileName);
                if (!temp.getParentFile().exists()) {
                    temp.getParentFile().mkdirs();
                }
                try (OutputStream os = new FileOutputStream(temp);
                     //通过ZipFile的getInputStream方法拿到具体的ZipEntry的输入流
                     InputStream is = zipFile.getInputStream(zipEntry)) {
                    int len = 0;
                    while ((len = is.read()) != -1) {
                        os.write(len);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
