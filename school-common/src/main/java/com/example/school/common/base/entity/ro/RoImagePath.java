package com.example.school.common.base.entity.ro;

import com.example.school.common.constant.SysConst;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2019/6/27 11:50
 * description:
 */
@NoArgsConstructor
@Getter
@Setter
public class RoImagePath {

    private String originalImageFileUrl;
    private String compressImageFileUrl;

    public RoImagePath(Long topicImgId) {
        this.originalImageFileUrl = SysConst.ORIGINAL_IMAGE_FILE_URL + topicImgId;
        this.compressImageFileUrl = SysConst.COMPRESS_IMAGE_FILE_URL + topicImgId;
    }
}
