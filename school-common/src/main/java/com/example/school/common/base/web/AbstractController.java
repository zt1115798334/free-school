package com.example.school.common.base.web;


import com.example.school.common.constant.SystemStatusCode;
import com.example.school.common.base.entity.ResultMessage;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 14:52
 * description:
 */
public abstract class AbstractController {

    private SystemStatusCode success = SystemStatusCode.SUCCESS;
    private SystemStatusCode failed = SystemStatusCode.FAILED;
    private final static String PAGE_NUMBER = "pageNumber";
    private final static String PAGE_SIZE = "pageSize";
    private final static String TOTAL = "total";
    private final static String ROWS = "rows";

    /**
     * 格式输出结果
     *
     * @return ResultMessage
     */
    protected ResultMessage success() {
        return new ResultMessage().ok(success.getCode(), success.getName());
    }

    /**
     * 格式输出结果
     *
     * @param msg 描述
     * @return ResultMessage
     */
    protected ResultMessage success(String msg) {
        return new ResultMessage().ok(success.getCode(), msg);
    }

    /**
     * 格式输出结果
     *
     * @param data 数据
     * @return ResultMessage
     */
    protected ResultMessage success(Object data) {
        return new ResultMessage().ok(success.getCode(), success.getName()).setData(data);
    }

    /**
     * 格式输出结果
     *
     * @param msg  描述
     * @param data 数据
     * @return ResultMessage
     */
    protected ResultMessage success(String msg, Object data) {
        return new ResultMessage().ok(success.getCode(), msg).setData(data);
    }

    /**
     * 格式输出结果
     *
     * @return ResultMessage
     */
    protected ResultMessage failure() {
        return new ResultMessage().error(failed.getCode(), failed.getName());
    }

    /**
     * 格式输出结果
     *
     * @param msg 描述
     * @return ResultMessage
     */
    protected ResultMessage failure(String msg) {
        return new ResultMessage().error(failed.getCode(), msg);
    }


    /**
     * 格式输出结果
     *
     * @param pageNumber 当前页
     * @param pageSize   页大小
     * @param total      总记录数
     * @param rows       当前页数据
     * @return ResultMessage
     */
    protected ResultMessage success(long pageNumber, long pageSize, long total, Object rows) {
        return new ResultMessage().ok(success.getCode())
                .addData(PAGE_NUMBER, pageNumber)
                .addData(PAGE_SIZE, pageSize)
                .addData(TOTAL, total)
                .addData(ROWS, rows);
    }

    /**
     * 格式输出结果
     *
     * @param pageNumber 当前页
     * @param pageSize   页大小
     * @param total      总记录数
     * @param rows       当前页数据
     * @return ResultMessage
     */
    protected ResultMessage failure(long pageNumber, long pageSize, long total, Object rows) {
        return new ResultMessage().ok(failed.getCode())
                .addData(PAGE_NUMBER, pageNumber)
                .addData(PAGE_SIZE, pageSize)
                .addData(TOTAL, total)
                .addData(ROWS, rows);
    }
}
