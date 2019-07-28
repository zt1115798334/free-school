package com.example.school.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/3/12 11:15
 * description:
 */
public class ZipUtils {
    private ZipUtils() {
    }

    private static void doCompress(String srcFile, String zipFile) throws IOException {
        doCompress(new File(srcFile), new File(zipFile));
    }

    /**
     * 文件压缩
     *
     * @param srcFile 目录或者单个文件
     * @param zipFile 压缩后的ZIP文件
     */
    private static void doCompress(File srcFile, File zipFile) throws IOException {
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile))) {
            doCompress(srcFile, srcFile.getName(), out);
        }
        //记得关闭资源
    }

    static void doCompress(String fileName, String newFileName, ZipOutputStream out) throws IOException {
        doCompress(new File(fileName), newFileName, out);
    }

    private static void doCompress(File file, String newFileName, ZipOutputStream out) throws IOException {
        doCompress(file, newFileName, out, "");
    }

    private static void doCompress(File inFile, String newFileName, ZipOutputStream out, String dir) throws IOException {
        if (inFile.isDirectory()) {
            File[] files = inFile.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    String name = inFile.getName();
                    if (!"".equals(dir)) {
                        name = dir + "/" + name;
                    }
                    ZipUtils.doCompress(file, newFileName, out, name);
                }
            }
        } else {
            ZipUtils.doZip(inFile, newFileName, out, dir);
        }
    }

    private static void doZip(File inFile, String newFileName, ZipOutputStream out, String dir) throws IOException {

        ZipEntry entry = new ZipEntry(newFileName);
        out.putNextEntry(entry);
        FileChannel fileChannel = new FileInputStream(inFile).getChannel();
        ByteBuffer buff = ByteBuffer.allocateDirect(786432);
        int bufferSize = 131072;
        int nRead, nGet;
        byte[] byteArr = new byte[bufferSize];

        while ((nRead = fileChannel.read(buff)) != -1) {
            if (nRead == 0) {
                continue;
            }
            buff.position(0);
            buff.limit(nRead);
            while (buff.hasRemaining()) {
                nGet = Math.min(buff.remaining(), bufferSize);
                // read bytes from disk
                buff.get(byteArr, 0, nGet);
                // write bytes to output
                out.write(byteArr);
                out.flush();
            }
            buff.clear();
        }

        out.closeEntry();
        fileChannel.close();
    }

    public static void main(String[] args) throws IOException {
        doCompress("D:/java/", "D:/java.zip");
    }
}
