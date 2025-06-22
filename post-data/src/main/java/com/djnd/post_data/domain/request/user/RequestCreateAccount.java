package com.djnd.post_data.domain.request.user;

import com.djnd.post_data.MyLib.StrongPassword;

import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateAccount {
    private String userName;
    @NotNull(message = ">>> Password cannot be null! <<<")
    @NotBlank(message = ">>> Password cannot be empty! <<<")
    @Size(min = 6, message = ">>> Minimum password is 6 character or digit! <<<")
    @Pattern(regexp = ".*[a-zA-Z].*", message = ">>> Password must be contain character! <<<")
    @Pattern(regexp = ".*[0-9].*", message = ">>> Password must be contain digit! <<<")
    private String password;
    @NotNull(message = ">>> Confirm password cannot be null! <<<")
    @Size(min = 6, message = ">>> Minimum confirm password is 6 character or digit! <<<")
    @NotBlank(message = ">>> Password cannot be empty! <<<")
    @Pattern(regexp = ".*[a-zA-Z].*", message = ">>> Password must be contain character! <<<")
    @Pattern(regexp = ".*[0-9].*", message = ">>> Password must be contain digit! <<<")
    private String confirmPassword;
}
