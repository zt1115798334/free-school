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
 * date: 2018/12/28 17:53
 * description:
 */
@Documented
@Constraint(validatedBy = TimeType.Validator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeType {

    String message() default "时间类型错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<TimeType, String> {
        @Override
        public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
            return SysConst.getTimeTypeByType(s).isPresent();
        }
    }
}
