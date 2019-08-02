package pl.sda.nutflex.domain;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {MyValidator.class})
@Documented
public @interface ReleaseDateValidation {
    String message() default "Too old movie";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
