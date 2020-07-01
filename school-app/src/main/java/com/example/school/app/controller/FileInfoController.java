package com.example.school.app.controller;

import com.example.school.common.mysql.service.FileInfo;
import com.example.school.common.utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@Api(tags = "文件")
@Controller
@RequestMapping(value = "app/file")
@AllArgsConstructor
public class FileInfoController {

    private final FileInfo fileInfoService;

    @ApiOperation(value = "展示原始图片信息")
    @GetMapping(value = "findOriginalImg")
    public void findOriginalImg(HttpServletRequest request, HttpServletResponse response,
                                @NotNull(message = "id不能为空") @RequestParam Long topicImgId) throws Exception {
        com.example.school.common.mysql.entity.FileInfo fileInfo = fileInfoService.findFileInfo(topicImgId);
        FileUtils.showFile(request, response, fileInfo.getFilePath(), fileInfo.getOriginalFileName());
    }

    @ApiOperation(value = "展示压缩图片信息")
    @GetMapping(value = "findCompressImg")
    public void findCompressImg(HttpServletRequest request, HttpServletResponse response,
                                @NotNull(message = "id不能为空") @RequestParam Long topicImgId) throws Exception {
        com.example.school.common.mysql.entity.FileInfo fileInfo = fileInfoService.findFileInfo(topicImgId);
        FileUtils.showFile(request, response, fileInfo.getCompressFilePath(), fileInfo.getOriginalFileName());
    }

    @ApiOperation(value = "展示题库pdf信息")
    @GetMapping(value = "findQuestionBankPdf")
    public void findQuestionBankPdf(HttpServletRequest request, HttpServletResponse response,
                                    @NotNull(message = "id不能为空") @RequestParam Long topicFileId) throws Exception {
        com.example.school.common.mysql.entity.FileInfo fileInfo = fileInfoService.findFileInfo(topicFileId);
        FileUtils.showFile(request, response, fileInfo.getFilePath(), fileInfo.getOriginalFileName());
    }

    @ApiOperation(value = "展示题库pdf信息Html")
    @GetMapping(value = "findQuestionBankPdfHtml")
    public String findQuestionBankPdfHtml(Model model, @NotNull(message = "id不能为空") @RequestParam Long topicFileId) {
        model.addAttribute("topicFileId", topicFileId);
        return "showPdf";
    }

}
