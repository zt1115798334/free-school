package com.example.school.common.utils;

import com.example.school.common.mysql.entity.FileInfo;
import com.example.school.common.utils.module.UploadFile;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/2/20 14:46
 * description:
 */
@Slf4j
public class FileUtils {

    public static String getFolderPath(String folder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = formatter.format(LocalDate.now());
        return System.getProperty("user.dir") + File.separator + "file" + File.separator + folder + File.separator + date + File.separator;
    }


    /**
     * 创建文件
     *
     * @param fileName    文件名称
     * @param fileContent 文件内容
     * @return 是否创建成功，成功则返回true
     */
    public static UploadFile createFile(String folderPath, String fileName, String fileContent) throws IOException {
        String filenameTemp = folderPath + fileName + ".txt";//文件路径+名称+文件类型
        //创建创建目录
        Files.createDirectories(Paths.get(folderPath));

        if (Files.notExists(Paths.get(filenameTemp))) {
            Files.createFile(Paths.get(filenameTemp));
        }
        byte[] contentBytes = fileContent.getBytes();
        FileOutputStream fos = new FileOutputStream(filenameTemp, true);
        FileChannel channel = fos.getChannel();
        ByteBuffer buf = ByteBuffer.wrap(contentBytes);
        buf.put(contentBytes);
        buf.flip();
        long fileSize = channel.size();

        channel.write(buf);
        channel.close();
        String fileMD5 = DigestUtils.md5Hex(contentBytes);
        fos.close();
        return new UploadFile(filenameTemp, StringUtils.EMPTY, String.valueOf(fileSize), fileMD5, fileName + ".txt", fileName + ".txt", fileName, "txt");
    }

    /**
     * JDK8 NIO读取文件
     *
     * @return
     */
    public static List<String> readFileNIO(List<String> filePaths) {
        return filePaths.stream().map(filePath -> {
            try {
                return Files.lines(Paths.get(filePath), StandardCharsets.UTF_8).collect(Collectors.joining());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
    }

    public static UploadFile uploadFile(MultipartFile file, String folderPath) {
        try {
            Files.createDirectories(Paths.get(folderPath));
            return transferFile(folderPath, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传文件，并获取上传文件（单文件上传）
     *
     * @param request    [requewt请求]
     * @param folderPath [文件保存路]
     * @return [文件自定义实体类]
     */
    public static UploadFile uploadFile(HttpServletRequest request, String folderPath) {
        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            // 新建目录
            Files.createDirectories(Paths.get(folderPath));
            // 判断 request 是否有文件上传,即多部分请求
            if (multipartResolver.isMultipart(request)) {
                // 转换成多部分request
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                // 取得request中的所有文件名
                Iterator<String> iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    UploadFile f = transferFile(folderPath, multiRequest.getFile(iter.next()));
                    if (f != null) {
                        return f;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传文件，并获取上传文件列表（多文件上传）
     *
     * @param request    [requewt请求]
     * @param folderPath [文件保存路]
     * @return [文件自定义实体类]
     */
    public static List<UploadFile> batchUploadFile(HttpServletRequest request, String folderPath) {
        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 新建目录
        try {
            Files.createDirectories(Paths.get(folderPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<UploadFile> files = new ArrayList<>();
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // 取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                List<MultipartFile> multipartFiles = multiRequest.getFiles(iter.next());
                for (MultipartFile multipartFile : multipartFiles) {
                    UploadFile f = null;
                    try {
                        f = transferFile(folderPath, multipartFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (f != null) {
                        files.add(f);
                    }
                }

            }
        }
        return files;
    }


    /**
     * 文件写入磁盘
     *
     * @param folderPath 文件路径
     * @param file       文件
     * @return 文件信息
     * @throws IOException io一场
     */
    private static UploadFile transferFile(String folderPath, MultipartFile file) throws IOException {
        if (file != null) {
            byte[] fileBytes = file.getBytes();
            // 取得当前上传文件的文件名称
            String fileMD5 = DigestUtils.md5Hex(fileBytes);
            String originalFileName = file.getOriginalFilename();//原名称 带后缀
            if (StringUtils.isNotEmpty(originalFileName)) {

                String fileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));//原名称
                String suffixName = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);//后缀名

                double size = (file.getSize() * 1.0) / (1024 * 1.0) / (1024 * 1.0);
                BigDecimal bg = new BigDecimal(size);
                double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                String fileSize = f1 + "";

                // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                // 重命名上传后的文件名
                String newName = UUID.randomUUID().toString() + "." + suffixName;
                // 定义上传路径
                String tarPath = folderPath + File.separator + newName;

                Path path = Paths.get(tarPath);
                //保存在本地
                Files.write(path, fileBytes);
                File serverFile = new File(tarPath);

                // 定义上传路径
                String tarCompressPath = null;
//
                if (ImagesUtils.isImage(serverFile)) {//是图片
                    // 新建目录
                    Files.createDirectories(Paths.get(folderPath + File.separator + "compress"));
                    tarCompressPath = folderPath + File.separator + "compress" + File.separator + newName;
                    ImagesUtils.scaleImage(tarPath, tarCompressPath, 0.15f, suffixName);
                }

                return new UploadFile(tarPath, tarCompressPath, fileSize, fileMD5, newName, originalFileName, fileName, suffixName);
            }
        }
        return null;
    }

    /**
     * 下载文件
     *
     * @param request  request
     * @param response response
     * @param filePath filePath
     * @param fileName fileName
     * @throws Exception exception
     */
    public static boolean downLoadFile(HttpServletRequest request, HttpServletResponse response,
                                       String filePath, String fileName) throws Exception {
        boolean bl = false;
        if (request != null && response != null
                && StringUtils.isNotEmpty(filePath) && StringUtils.isNotEmpty(fileName)) {
            final String browserType = request.getParameter("browserType");
            final String userAgent = request.getHeader("USER-AGENT");
            log.info("userAgent==:" + userAgent);
            // filePath是指欲下载的文件的路径。
            filePath = URLDecoder.decode(filePath, "UTF-8");
            File file = new File(filePath);
            if (!file.exists()) {
                log.info("############ FilePath: " + filePath);
                throw new Exception("文件不存在！");
            }
            if (!file.isFile()) {
                log.info("############ FilePath: " + filePath);
                throw new Exception("非文件类型！");
            }

            if ("IE".equals(browserType)) {// IE浏览器,页面传过来的值，只用于判断是否为IE浏览器
                log.info("ie浏览器");
                fileName = URLEncoder.encode(fileName, "UTF8");
            } else {
                if (userAgent.contains("Mozilla")) {// google,火狐浏览器
                    fileName = new String(fileName.getBytes(), "ISO8859-1");
                    log.info("火狐浏览器");
                } else {
                    log.info("其他浏览器");
                    fileName = URLEncoder.encode(fileName, "UTF8");// 其他浏览器
                }
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            FileChannel fileChannel = fileInputStream.getChannel();
            long fileLength =fileChannel.size();

            // 提示框设置
            response.reset(); // reset the response
//            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");//告诉浏览器输出内容为流
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentLengthLong(fileLength);
            //NIO 实现
            int bufferSize = 131072;
            //读出文件到i/o流

            // 6x128 KB = 768KB byte buffer
            ByteBuffer buff = ByteBuffer.allocateDirect(786432);
            byte[] byteArr = new byte[bufferSize];
            int nRead, nGet;
            try {
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
                        response.getOutputStream().write(byteArr);
                    }
                    buff.clear();
                    log.info("文件下载完毕！");
                    bl = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                buff.clear();
                fileChannel.close();
                fileInputStream.close();
            }
        }
        return bl;

    }

    /**
     * 下载文件
     *
     * @param request  request
     * @param response response
     * @param filePath filePath
     * @param fileName fileName
     * @throws Exception exception
     */
    public static boolean showFile(HttpServletRequest request, HttpServletResponse response,
                                   String filePath, String fileName) throws Exception {
        boolean bl = false;
        if (request != null && response != null
                && StringUtils.isNotEmpty(filePath) && StringUtils.isNotEmpty(fileName)) {
            final String browserType = request.getParameter("browserType");
            final String userAgent = request.getHeader("USER-AGENT");
            log.info("userAgent==:" + userAgent);
            // filePath是指欲下载的文件的路径。
            filePath = URLDecoder.decode(filePath, "UTF-8");
            File file = new File(filePath);
            if (!file.exists()) {
                log.info("############ FilePath: " + filePath);
                throw new Exception("文件不存在！");
            }
            if (!file.isFile()) {
                log.info("############ FilePath: " + filePath);
                throw new Exception("非文件类型！");
            }
            // 提示框设置
            response.reset(); // reset the response
            //NIO 实现
            int bufferSize = 131072;
            //读出文件到i/o流
            FileInputStream fileInputStream = new FileInputStream(file);
            FileChannel fileChannel = fileInputStream.getChannel();
            // 6x128 KB = 768KB byte buffer
            ByteBuffer buff = ByteBuffer.allocateDirect(786432);
            byte[] byteArr = new byte[bufferSize];
            int nRead, nGet;
            try {
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
                        response.getOutputStream().write(byteArr);
                    }
                    buff.clear();
                    log.info("文件下载完毕！");
                    bl = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                buff.clear();
                fileChannel.close();
                fileInputStream.close();
            }
        }
        return bl;

    }

    /**
     * 下载文件
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public static void fileBatchDownLoadZip(HttpServletRequest request,
                                            HttpServletResponse response,
                                            List<FileInfo> fileInfoList) throws Exception {
        String zipName = "fileInfo.zip";
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipName);
        ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
        try {
            for (FileInfo fileInfo : fileInfoList) {
                ZipUtils.doCompress(fileInfo.getFilePath(), fileInfo.getOriginalFileName(), out);
                response.flushBuffer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(String filePath) {
        Path directory = Paths.get(filePath);
        try {
            return Files.deleteIfExists(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除文件与目录
     *
     * @param filePath 文件与目录路径
     * @return boolean
     */
    public static boolean deleteFolder(String filePath) {
        Path path = Paths.get(filePath);
        // 判断目录或文件是否存在
        if (!Files.exists(path)) { // 不存在返回 false
            return false;
        } else {
            // 判断是否为文件
            if (Files.isDirectory(path)) { // 为目录时调用删除目录方法
                return deleteDirectory(filePath);
            } else { // 为文件时调用删除文件方法
                return deleteFile(filePath);
            }
        }
    }

    /**
     * 删除目录
     *
     * @param filePath 目录路径
     * @return boolean
     */
    public static boolean deleteDirectory(String filePath) {
        Path directory = Paths.get(filePath);
        Set<FileVisitOption> opts = Sets.newHashSet(FileVisitOption.FOLLOW_LINKS);
        try {
            Files.walkFileTree(directory, opts, Integer.MAX_VALUE, new DeleteDirectory());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        Path path1 = Paths.get("D:\\123\\ddd");
        Path path2 = Paths.get("D:\\123\\123.txt");
        System.out.println(Files.isDirectory(path1));
        System.out.println(Files.isDirectory(path2));
    }
}
