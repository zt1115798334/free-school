package com.example.school.shiro.aop;

import com.alibaba.fastjson.JSONObject;
import com.example.school.common.utils.MStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/26 9:41
 * description:
 */
@Slf4j
public abstract class AbsHttpAspect {

    private static String methodName;      // 方法名
    private static String paramVal;        //参数信息
    private static long startTime;         // 开始时间

    protected abstract void aopPointCut();

    @Before("aopPointCut()")
    private void doBefore(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        methodName = signature.getDeclaringTypeName() + "." + signature.getName();

        Object[] parameterValues = joinPoint.getArgs();  //目标方法参数
        String[] parameterNames = methodSignature.getParameterNames();

        paramVal = MStringUtils.parseParams(parameterNames, parameterValues);
        startTime = System.currentTimeMillis();
    }

    @After("aopPointCut()")
    private void doAfter() {
        long E_time = System.currentTimeMillis() - startTime;
        log.info("执行 " + methodName + " 耗时为：" + E_time + "ms");
        if (paramVal.length() < 1000) {
            log.info("参数信息：" + paramVal);
        }
    }

    @AfterReturning(returning = "object", pointcut = "aopPointCut()")
    private void doAfterReturning(Object object) {
        if (object != null) {
            JSONObject result = JSONObject.parseObject(JSONObject.toJSONString(object));
            result.remove("data");
            log.info("response：{}", result.toJSONString());
        }
    }

}
