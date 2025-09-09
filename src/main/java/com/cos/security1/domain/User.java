package com.cos.security1.domain;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

// ORM - Object Relation Mapping

@Data
@Entity
@NoArgsConstructor
public class User {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private int id;
    private String username;
    private String password;
    private String email;
    private String role; //ROLE_USER, ROLE_ADMIN

    private String provider;
    private String providerId;
    @CreationTimestamp //해당 엔티티가 처음 저장될 때 자동으로 현재 시간을 기록
    private Timestamp createDate;

    @Builder
    public User(String username, String password, String email, String role, String provider, String providerId,Timestamp createDate) {
        this.username=username;
        this.password=password;
        this.email=email;
        this.role=role;
        this.provider=provider;
        this.providerId=providerId;
        this.createDate=createDate;
    }
}
