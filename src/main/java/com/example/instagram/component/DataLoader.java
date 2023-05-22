package com.example.instagram.component;

import com.example.instagram.entity.Role;
import com.example.instagram.entity.User;
import com.example.instagram.repository.RoleRepository;
import com.example.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author "ISMOIL NIGMATOV"
 * @created 12:15 PM on 10/8/2022
 * @project Project
 */

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Value("${spring.sql.init.mode}")
    String mode;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args){
        if (mode.equals("always")){
            Role admin = roleRepository.save(new Role(1L,"ADMIN"));

            userRepository.save(new User("688008330",null,null,null,null,null,100L,passwordEncoder.encode("mdyiloveyou"),true,true,true,true,admin));
        }
    }
}
