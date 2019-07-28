package com.example.school.shiro.aop;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.User;
import com.example.school.common.mysql.entity.UserLog;
import com.example.school.common.mysql.service.UserLogService;
import com.example.school.common.utils.DateUtils;
import com.example.school.common.utils.MStringUtils;
import com.example.school.common.utils.NetworkUtil;
import com.example.school.shiro.base.CurrentUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/26 9:49
 * description:
 */
@Slf4j
@AllArgsConstructor
public abstract class AbsHttpAspectSave implements CurrentUser {
    private static String className;      // 方法名
    private static String methodName;      // 方法名
    private static String paramVal;        //参数信息
    private static String ip;              //ip地址

    private final UserLogService userLogService;

    protected abstract void aopPointCut(SaveLog logs);

    @Before(value = "aopPointCut(logs)", argNames = "joinPoint,logs")
    private void doBefore(JoinPoint joinPoint, SaveLog logs) {
        Signature signature = joinPoint.getSignature();
        className = signature.getDeclaringTypeName();
        methodName = signature.getName();
        MethodSignature methodSignature = (MethodSignature) signature;

        Object[] parameterValues = joinPoint.getArgs();  //目标方法参数
        String[] parameterNames = methodSignature.getParameterNames();

        paramVal = MStringUtils.parseParams(parameterNames, parameterValues);
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        ip = NetworkUtil.getLocalIp(request);
    }

    @AfterReturning(returning = "object", pointcut = "aopPointCut(logs)", argNames = "object,logs")
    private void doAfterReturning(Object object, SaveLog logs) throws OperationException {
        //返回值
        String response = JSONObject.toJSONString(object);
        String desc = logs.desc();
        User currentUser = getCurrentUser();
        Long userId = null;
        String username = null;
        if (currentUser != null) {
            userId = currentUser.getId();
            username = currentUser.getUserName();
        }
        UserLog userLog = new UserLog(userId, username, desc, paramVal, ip, DateUtils.currentDateTime(), className, methodName, response);
        userLogService.save(userLog);
    }
}
