package com.example.demo.entity;


import com.example.demo.enums.UserGender;
import com.example.demo.enums.UserRole;
import com.example.demo.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true, name = "mail")
    String mail;

    @Column(nullable = false, name = "password")
    String password;

    @Column(nullable = false, name = "fullname")
    String fullName;

    @Column(nullable = false, name = "birthday")
    LocalDate dateOfBirth;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    UserGender gender;

    @Column(name = "address")
    String address;

    @Column(nullable = false, name = "status_account")
    @Enumerated(EnumType.STRING)
    UserStatus statusAccount;

    @Column(name = "avatar_url")
    String avatar;

    @Column(nullable = false, name = "role")
    @Enumerated(EnumType.STRING)
    UserRole role;

    @Column(name = "phone")
    String phone;
}
