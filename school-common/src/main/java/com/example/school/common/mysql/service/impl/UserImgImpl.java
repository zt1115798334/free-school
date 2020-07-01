package com.example.school.common.mysql.service.impl;

import com.example.school.common.mysql.repo.UserImgRepository;
import com.example.school.common.mysql.service.FileInfo;
import com.example.school.common.mysql.service.UserImg;
import com.example.school.common.utils.FileUtils;
import com.example.school.common.utils.UserUtils;
import com.example.school.common.utils.module.UploadFile;
import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/24 10:16
 * description:
 */
@AllArgsConstructor
@Service
public class UserImgImpl implements UserImg {

    private final UserImgRepository userImgRepository;

    private final FileInfo fileInfoService;


    @Override
    public com.example.school.common.mysql.entity.UserImg save(com.example.school.common.mysql.entity.UserImg userImg) {
        userImg.setState(ON);
        userImg.setDeleteState(UN_DELETED);
        userImg.setCreatedTime(currentDateTime());
        return userImgRepository.save(userImg);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public com.example.school.common.mysql.entity.UserImg saveUserImg(HttpServletRequest request, Long userId) {
        String folderPath = FileUtils.getFolderPath(FOLDER_HEAD_PORTRAIT);
        UploadFile uploadFile = FileUtils.uploadFile(request, folderPath);
        com.example.school.common.mysql.entity.FileInfo fileInfo = fileInfoService.saveFileInfo(uploadFile);
        userImgRepository.updateUserImgState(OFF, userId);
        return this.save(new com.example.school.common.mysql.entity.UserImg(userId, fileInfo.getId()));
    }

    @Override
    public com.example.school.common.mysql.entity.UserImg findUserImgUrlByOn(Long userId) {
        return userImgRepository.findByUserIdAndStateAndDeleteState(userId, ON, UN_DELETED).orElse(UserUtils.getDefaultUserImg());
    }

    @Override
    public Map<Long, com.example.school.common.mysql.entity.UserImg> findUserImgUrlByOn(List<Long> userIdList) {
        List<com.example.school.common.mysql.entity.UserImg> userImgList = userImgRepository.findByUserIdInAndStateAndDeleteState(userIdList, ON, UN_DELETED);
        return userImgList.stream().collect(toMap(com.example.school.common.mysql.entity.UserImg::getUserId, Function.identity()));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void modifyUserImg(Long userId, Long imgId) {
        List<com.example.school.common.mysql.entity.UserImg> userImgList = userImgRepository.findByUserId(userId);
        List<com.example.school.common.mysql.entity.UserImg> needSave = userImgList.stream().peek(userImg -> {
            if (Objects.equal(imgId, userImg.getImgId())) {
                userImg.setState(ON);
            } else {
                userImg.setState(OFF);
            }
        }).collect(toList());
        userImgRepository.saveAll(needSave);
    }

}
