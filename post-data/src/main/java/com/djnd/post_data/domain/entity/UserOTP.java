package com.djnd.post_data.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "userOTP")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserOTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 4, message = ">>> OTP code must be contain 4 number digits! <<<")
    private Integer code;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
