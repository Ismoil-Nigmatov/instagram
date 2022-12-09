package com.example.instagram.repository;

import com.example.instagram.entity.Info;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author "ISMOIL NIGMATOV"
 * @created 8:28 PM on 11/30/2022
 * @project instagram
 */
public interface InfoRepository extends JpaRepository<Info,Long> {
}
