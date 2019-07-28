package com.example.school.common.exception;

import com.example.school.common.constant.SystemStatusCode;
import com.example.school.common.base.entity.ResultMessage;
import com.example.school.common.exception.custom.OperationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 15:07
 * description:
 */
@Slf4j
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public ResultMessage exceptionHandler(HttpServletResponse response, Exception ex) {
        log.error(ex.getMessage());
        ex.printStackTrace();
        return new ResultMessage().error(SystemStatusCode.SC_INTERNAL_SERVER_ERROR.getCode(), "系统出现异常，请联系管理员");
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResultMessage handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage());
        return new ResultMessage().error(SystemStatusCode.SC_BAD_REQUEST.getCode(), SystemStatusCode.SC_BAD_REQUEST.getMsg());
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultMessage handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error(ex.getMessage());
        return new ResultMessage().error(SystemStatusCode.SC_METHOD_NOT_ALLOWED.getCode(), SystemStatusCode.SC_METHOD_NOT_ALLOWED.getMsg());
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResultMessage handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        log.error(ex.getMessage());
        return new ResultMessage().error(SystemStatusCode.SC_UNSUPPORTED_MEDIA_TYPE.getCode(), SystemStatusCode.SC_UNSUPPORTED_MEDIA_TYPE.getMsg());
    }

    /**
     * 用来处理bean validation异常
     *
     * @param ex 异常
     * @return ResultMessage
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultMessage resolveConstraintViolationException(ConstraintViolationException ex) {
        log.error(ex.getMessage());
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        String result = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        return new ResultMessage().error(SystemStatusCode.PARAMS_VALIDATION_FAILED.getCode(), result);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultMessage resolveMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        String result = objectErrors.stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(","));
        return new ResultMessage().error(SystemStatusCode.PARAMS_VALIDATION_FAILED.getCode(), result);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResultMessage resolveMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error(ex.getMessage());
        return new ResultMessage().error(SystemStatusCode.PARAMS_VALIDATION_FAILED.getCode(), "参数类型错误，请联系管理员");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResultMessage resolveMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error(ex.getMessage());
        return new ResultMessage().error(SystemStatusCode.PARAMS_VALIDATION_FAILED.getCode(), "参数错误，请联系管理员");
    }

    /**
     * 操作异常
     *
     * @param ex ex
     * @return ResultMessage
     */
    @ExceptionHandler(OperationException.class)
    public ResultMessage operationException(OperationException ex) {
        log.error(ex.getMessage());
        return new ResultMessage().error(SystemStatusCode.FAILED.getCode(), ex.getMessage());
    }

}
