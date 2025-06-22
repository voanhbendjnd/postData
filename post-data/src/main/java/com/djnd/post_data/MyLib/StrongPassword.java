package com.djnd.post_data.MyLib;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented

@NotBlank(message = ">>> Password cannot be empty! <<<")
@Size(min = 6, message = ">>> Minimum password 6 character! <<<")
@Size(max = 8, message = ">>> Maximum password is 8 character! <<<")
// .* any character, zero or many charater
@Pattern(regexp = ".*[a-zA-Z].*", message = ">>> Password must contain digit and character! <<<")
@Pattern(regexp = ".*[0-9].*", message = ">>> Password must contain  digit and character! <<<")
@ReportAsSingleViolation // only allow 1 error
public @interface StrongPassword {
    String message()

    default "Mật khẩu không đáp ứng các yêu cầu bảo mật.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}