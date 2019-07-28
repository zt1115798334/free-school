package com.example.school.task.task.page;

import com.example.school.common.constant.SysConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/24 14:58
 * description:
 */
@Slf4j
public abstract class PageHandler<T> {


    protected final int DEFAULT_BATCH_SIZE = SysConst.DEFAULT_BATCH_SIZE;


    public void handle() {
        try {
            log.info("处理数据开始");

            int total = 0;
            int count = handleData();
            total += count;

            log.info("共处理了{}条数据", total);
            log.info("处理数据结束");
        } catch (Exception e) {
            log.error("处理数据出错，异常信息：{} ", e.toString());
        }
    }

    private int handleData() {
        int total = 0;
        int pageNumber = 1;

        Page<T> page = getPageList(pageNumber);
        long totalPages = page.getTotalPages();
        List<T> list = page.getContent();
        log.info("本表格总共{}页", totalPages);
        log.info("第1页数据处理开始");
        int count = handleDataOfPerPage(list, pageNumber);
        log.info("第1页数据处理结束，处理了{}条数据", count);
        total += count;

        for (int i = 2; i <= totalPages; i++) {
            page = getPageList(i);
            list = page.getContent();
            log.info("第{}页数据处理开始", i);
            count = handleDataOfPerPage(list, i);
            log.info("第{}页数据处理结束，处理了{}条数据", i, count);
            total += count;
        }
        return total;
    }

    protected abstract int handleDataOfPerPage(List<T> list, int pageNumber);

    protected abstract Page<T> getPageList(int pageNumber);
}
