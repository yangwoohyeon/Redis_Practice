package com.example.RedisPractice.api.members.entity;

import com.example.RedisPractice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 20)
    @Email
    private String email;

    @Column(name = "password", nullable = false, unique = true, length = 100)
    private String password;

    @Column(name = "nickname", nullable = false, unique = true, length = 15)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    public void setRole(Role role){
        this.role = role;
    }
}
