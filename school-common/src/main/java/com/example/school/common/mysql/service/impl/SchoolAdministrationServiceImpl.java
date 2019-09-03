package com.example.school.common.mysql.service.impl;

import com.example.school.common.constant.SysConst;
import com.example.school.common.exception.custom.OperationException;
import com.example.school.common.mysql.entity.SchoolAdministration;
import com.example.school.common.mysql.repo.SchoolAdministrationRepository;
import com.example.school.common.mysql.service.SchoolAdministrationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/08/03 12:30
 * description:
 */
@AllArgsConstructor
@Service
public class SchoolAdministrationServiceImpl implements SchoolAdministrationService {

    private final SchoolAdministrationRepository schoolAdministrationRepository;

    @Override
    @Transactional(rollbackFor = RuntimeException.class, isolation = Isolation.READ_UNCOMMITTED)
    public SchoolAdministration save(SchoolAdministration schoolAdministration) {
        Long userId = schoolAdministration.getUserId();
        Optional<SchoolAdministration> administrationOptional = this.findOptByUserId(userId);
        if (administrationOptional.isPresent()) {
            SchoolAdministration dbAdministration = administrationOptional.get();
            dbAdministration.setStudentId(schoolAdministration.getStudentId());
            dbAdministration.setStudentPwd(schoolAdministration.getStudentPwd());
            dbAdministration.setUpdatedTime(currentDateTime());
            return schoolAdministrationRepository.save(dbAdministration);
        } else {
            schoolAdministration.setUsableState(SysConst.UsableState.AVAILABLE.getCode());
            schoolAdministration.setFreshState(SysConst.FreshState.FRESH.getCode());
            schoolAdministration.setCreatedTime(currentDateTime());
            return schoolAdministrationRepository.save(schoolAdministration);
        }
    }

    @Override
    public SchoolAdministration saveSchoolAdministration(Long userId, Short schoolCode, String studentId, String studentPwd) {
//        String studentPwd = schoolAdministration.getStudentPwd();
//        String studentPwdRsa = RSAUtils.encryptBASE64(RSAUtils.encryptByPrivateKey(studentPwd.getBytes(), schoolProperties.getRsa().getPrivateKey()));
//        schoolAdministration.setStudentPwd(studentPwdRsa);
        SchoolAdministration schoolAdministration = new SchoolAdministration(userId, schoolCode, studentId, studentPwd);
        return this.save(schoolAdministration);
    }

    @Override
    public void modifySchoolAdministrationUsableStateToNotAvailable(Long userId) {
        Optional<SchoolAdministration> administrationOptional = this.findOptByUserId(userId);
        administrationOptional.ifPresent(schoolAdministration -> {
            schoolAdministration.setUsableState(SysConst.UsableState.NOT_AVAILABLE.getCode());
            schoolAdministrationRepository.save(schoolAdministration);
        });
    }

    @Override
    public Optional<SchoolAdministration> findOptByUserId(Long userId) {
        return schoolAdministrationRepository.findByUserId(userId);
    }

    @Override
    public SchoolAdministration findSchoolAdministration(Long userId) {
        return this.findOptByUserId(userId).orElseThrow(() -> new OperationException("你还没有绑定呢。。"));
    }
}
