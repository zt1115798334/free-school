package com.example.school.app.controller;

import com.example.school.common.mysql.entity.FileInfo;
import com.example.school.common.mysql.service.FileInfoService;
import com.example.school.common.utils.FileUtils;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/23 16:05
 * description:
 */
@Controller
@RequestMapping(value = "app/file")
@AllArgsConstructor
public class FileInfoController {

    private final FileInfoService fileInfoService;

    @ApiOperation(value = "展示原始图片信息")
    @GetMapping(value = "findOriginalImg")
    public void findOriginalImg(HttpServletRequest request, HttpServletResponse response,
                                @NotNull(message = "id不能为空") @RequestParam Long topicImgId) throws Exception {
        FileInfo fileInfo = fileInfoService.findFileInfo(topicImgId);
        FileUtils.showFile(request, response, fileInfo.getFilePath(), fileInfo.getFileName());
    }

    @ApiOperation(value = "展示压缩图片信息")
    @GetMapping(value = "findCompressImg")
    public void findCompressImg(HttpServletRequest request, HttpServletResponse response,
                                @NotNull(message = "id不能为空") @RequestParam Long topicImgId) throws Exception {
        FileInfo fileInfo = fileInfoService.findFileInfo(topicImgId);
        FileUtils.showFile(request, response, fileInfo.getCompressFilePath(), fileInfo.getFileName());
    }
}
