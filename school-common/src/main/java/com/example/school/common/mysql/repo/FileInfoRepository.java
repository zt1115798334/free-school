package com.example.school.common.mysql.repo;

import com.example.school.common.mysql.entity.FileInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/2/21 10:20
 * description:
 */
public interface FileInfoRepository extends CrudRepository<FileInfo, Long> {

}
