package cn.plutowu.validator;

import cn.plutowu.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 手机验证器
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
public class IsMobileValidator implements ConstraintValidator<cn.plutowu.validator.IsMobile,String> {

    private boolean required = false;


    @Override
    public void initialize(cn.plutowu.validator.IsMobile isMobile) {
        required = isMobile.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (required) {
            return ValidatorUtil.isMobile(value);
        } else {
            if (StringUtils.isEmpty(value)) {
                return true;
            } else {
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
