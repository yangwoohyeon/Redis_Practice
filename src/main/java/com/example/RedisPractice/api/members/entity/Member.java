package com.example.RedisPractice.api.members.entity;

import com.example.RedisPractice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 20)
    @Email
    private String email;

    @Column(name = "password", nullable = false, unique = true, length = 20)
    private String password;

    @Column(name = "nickname", nullable = false, unique = true, length = 15)
    private String name;

}
