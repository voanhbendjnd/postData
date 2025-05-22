package com.djnd.post_data.domain.entity;

import java.time.Instant;

import com.djnd.post_data.utils.SecurityUtils;
import com.djnd.post_data.utils.constant.GenderEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;
    // @Min(value = 6, message = "password must great or than 6")
    @Size(min = 6, message = ">>> Password must be at least 6 characters <<<")
    private String password;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private UserOTP userOTP;

    private String address;
    private String createdBy;
    private String updatedBy;
    private Instant createdAt;
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonIgnore
    private Role role;

    @PrePersist
    public void handleBeforeCreateAt() {
        this.createdBy = SecurityUtils.getCurrentUserLogin().isPresent() == true
                ? SecurityUtils.getCurrentUserLogin().get()
                : "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdateBy() {
        this.updatedBy = SecurityUtils.getCurrentUserLogin().isPresent() == true
                ? SecurityUtils.getCurrentUserLogin().get()
                : "";
        this.updatedAt = Instant.now();
    }

}
