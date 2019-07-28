package com.example.school.common.validation;

import com.example.school.common.constant.SysConst;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 1/2/2019 1:57 PM
 * description:
 */
@Documented
@Constraint(validatedBy = NoticeType.Validator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoticeType {

    String message() default "通知类型类型错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<NoticeType, String> {
        @Override
        public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
            return SysConst.getNoticeTypeByType(string).isPresent();
        }
    }
}
