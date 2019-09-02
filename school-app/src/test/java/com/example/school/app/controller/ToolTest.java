package com.example.school.app.controller;

import com.example.school.app.SchoolAppApplicationTests;
import com.example.school.common.constant.SysConst;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.externalService.verification.VerificationService;
import com.example.school.common.mysql.service.UserRegistrationService;
import com.example.school.common.tools.ShortMessageTool;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/21 17:47
 * description:
 */
public class ToolTest extends SchoolAppApplicationTests {

    @Autowired
    private ShortMessageTool shortMessageTool;

    @Test
    public void sendShortMessageFromLoginCode() throws OperationException {
        shortMessageTool.sendShortMessageFromCode("15600663638", "8752", SysConst.VerificationCodeType.LOGIN.getType());
    }

    @Autowired
    private VerificationService verificationService;

    @Test
    public void ddd() {
        verificationService.verificationSchoolOfFZKJXY("175041141","Zyx0731..");
    }

}
