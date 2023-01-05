package com.example.instagram.controller;

import com.example.instagram.entity.Info;
import com.example.instagram.entity.User;
import com.example.instagram.repository.UserRepository;
import com.example.instagram.security.JwtProvider;
import com.example.instagram.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;


/**
 * @author "ISMOIL NIGMATOV"
 * @created 10:42 PM on 12/2/2022
 * @project instagram
 */

@RestController
@RequestMapping("/ismoil")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String chatId,@RequestParam String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(chatId, password));

        String token = jwtProvider.generateToken(chatId);
        return ResponseEntity.ok(token);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/quota")
    public ResponseEntity<?> addQuota(@RequestParam String chatId,@RequestParam Long quota){
        try {
            User user = userRepository.findById(chatId).orElseThrow(RuntimeException::new);
            Long lastQuota = user.getQuota();
            user.setQuota(lastQuota + quota);
            userRepository.save(user);
            return ResponseEntity.ok("Added");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.toString());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/info")
    public ResponseEntity<?> getInfos(@RequestParam String chatId){
        User user = userRepository.findById(chatId).orElseThrow(null);
        List<Info> infos = user.getInfos();
        return ResponseEntity.ok(infos);
    }
}
