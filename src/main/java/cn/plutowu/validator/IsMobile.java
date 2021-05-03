package cn.plutowu.validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 电话验证
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {IsMobileValidator.class}) // 引入校验器
public @interface IsMobile {
    boolean required() default true; // 默认不能为空

    String message() default "手机号码格式错误";  // 校验不通过输出信息

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
