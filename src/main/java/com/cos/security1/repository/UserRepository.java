package com.cos.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cos.security1.domain.User;
import org.springframework.stereotype.Repository;

// JpaRepository 를 상속하면 자동 컴포넌트 스캔됨
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    //findBy까지는 규칙, + Username
    //select * from user where username=?
    User findByUsername(String username);
}
