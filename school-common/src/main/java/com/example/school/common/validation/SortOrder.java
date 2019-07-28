package com.example.school.common.validation;

import com.example.school.common.constant.SysConst;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/28 17:42
 * description:
 */
@Documented
@Constraint(validatedBy = SortOrder.Validator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SortOrder {

    String message() default "排序类型错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<SortOrder, String> {
        @Override
        public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
            if (StringUtils.isBlank(s)) {
                return true;
            }
            return SysConst.getSortOrderByType(s).isPresent();
        }
    }
}



