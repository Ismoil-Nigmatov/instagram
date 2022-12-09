package com.example.instagram.repository;

import com.example.instagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author "ISMOIL NIGMATOV"
 * @created 8:27 PM on 11/30/2022
 * @project instagram
 */
public interface UserRepository extends JpaRepository<User,String> {
}
