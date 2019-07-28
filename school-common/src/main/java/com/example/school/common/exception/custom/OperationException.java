package com.example.school.common.exception.custom;

import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 1/7/2019 2:02 PM
 * description: 错误异常
 */
@NoArgsConstructor
public class OperationException extends Exception {
    private static final long serialVersionUID = 1L;

    public OperationException(String message) {
        super(message);
    }
}
