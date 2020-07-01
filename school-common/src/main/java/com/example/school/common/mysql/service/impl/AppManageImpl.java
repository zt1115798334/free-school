package com.example.school.common.mysql.service.impl;

import com.example.school.common.constant.SysConst;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.repo.AppManageRepository;
import com.example.school.common.mysql.service.AppManage;
import com.example.school.common.utils.DateUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/07/09 10:21
 * description:
 */
@AllArgsConstructor
@Service
public class AppManageImpl implements AppManage {


    private final AppManageRepository appManageRepository;

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public com.example.school.common.mysql.entity.AppManage save(com.example.school.common.mysql.entity.AppManage appManage) {
        appManage.setCreatedTime(DateUtils.currentDateTime());
        appManage.setDeleteState(UN_DELETED);
        return appManageRepository.save(appManage);
    }

    private void deleteAppManager(String systemType) {
        Optional<com.example.school.common.mysql.entity.AppManage> manageOptional = appManageRepository.findBySystemTypeAndDeleteState(systemType, UN_DELETED);
        manageOptional.ifPresent(appManage -> {
            appManage.setDeleteState(DELETED);
            appManageRepository.save(appManage);
        });
    }

    @Override
    @Transactional(rollbackOn = RuntimeException.class)
    public com.example.school.common.mysql.entity.AppManage saveAppManager(com.example.school.common.mysql.entity.AppManage appManage) {
        String systemType = appManage.getSystemType();
        //删除以前的
        deleteAppManager(systemType);
        return this.save(appManage);
    }

    @Override
    public com.example.school.common.mysql.entity.AppManage findAppManager(String systemType) {
        return appManageRepository.findBySystemTypeAndDeleteState(systemType, UN_DELETED).orElseThrow(() -> new OperationException("系统错误，请联系管理员"));
    }

    @Override
    public com.example.school.common.mysql.entity.AppManage saveAppManager(HttpServletRequest request) throws IOException {
        String folderPath = System.getProperty("user.dir") + File.separator + "appFile";
        String version = request.getParameter("version");
        String versionCode = request.getParameter("versionCode");
        String systemType = SysConst.AppSystemType.ANDROID.getType();
        String content = request.getParameter("content");
        Files.createDirectories(Paths.get(folderPath));
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            MultipartFile appFile = multiRequest.getFile("appFile");
            if (appFile != null) {
                String originalFileName = appFile.getOriginalFilename();
                if (StringUtils.isNotEmpty(originalFileName)) {
                    String fileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));//原名称
                    String suffixName = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);//后缀名
                    String newName = fileName + "." + suffixName;
                    // 定义上传路径
                    String tarPath = folderPath + "/" + newName;

                    Path path = Paths.get(tarPath);
                    byte[] fileBytes = appFile.getBytes();
                    //保存在本地
                    Files.write(path, fileBytes);

                    com.example.school.common.mysql.entity.AppManage appManage = new com.example.school.common.mysql.entity.AppManage(systemType, version, versionCode, newName, tarPath, content);
                    return this.saveAppManager(appManage);

                }
            }
        }
        return null;
    }

    @Override
    public void downAppFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        com.example.school.common.mysql.entity.AppManage appManage = this.findAppManager(SysConst.AppSystemType.ANDROID.getType());
        String filePath = appManage.getFilePath();
        String fileName = appManage.getFileName();
        if (request != null && response != null
                && StringUtils.isNotEmpty(filePath) && StringUtils.isNotEmpty(fileName)) {
            final String browserType = request.getParameter("browserType");
            final String userAgent = request.getHeader("USER-AGENT");

            // filePath是指欲下载的文件的路径。
            filePath = URLDecoder.decode(filePath, "UTF-8");
            File file = new File(filePath);
            if (!file.exists()) {
                throw new Exception("文件不存在！");
            }
            if (!file.isFile()) {
                throw new Exception("非文件类型！");
            }

            if ("IE".equals(browserType)) {// IE浏览器,页面传过来的值，只用于判断是否为IE浏览器
                fileName = URLEncoder.encode(fileName, "UTF8");
            } else {
                if (userAgent.contains("Mozilla")) {// google,火狐浏览器
                    fileName = new String(fileName.getBytes(), "ISO8859-1");
                } else {
                    fileName = URLEncoder.encode(fileName, "UTF8");// 其他浏览器
                }
            }

            FileInputStream fileInputStream = new FileInputStream(file);
            FileChannel fileChannel = fileInputStream.getChannel();

            long fileLength = fileChannel.size();

            // 提示框设置
            response.reset(); // reset the response
            response.setCharacterEncoding("UTF-8");
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

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                buff.clear();
                fileChannel.close();
                fileInputStream.close();
            }
        }
    }
}
