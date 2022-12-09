package com.example.instagram.service;

import com.example.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author "ISMOIL NIGMATOV"
 * @created 10:43 PM on 12/2/2022
 * @project instagram
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String chatId) throws UsernameNotFoundException {
        log.info(String.valueOf(userRepository.findById(chatId)));
        return userRepository.findById(chatId).orElse(null);
    }

}
