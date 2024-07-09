package com.sparta.easyspring.auth.controller;

import com.sparta.easyspring.auth.dto.AuthRequestDto;
import com.sparta.easyspring.auth.dto.AuthResponseDto;
import com.sparta.easyspring.auth.dto.RefreshTokenRequestDto;
import com.sparta.easyspring.auth.dto.UpdatePasswordRequestDto;
import com.sparta.easyspring.auth.security.UserDetailsImpl;
import com.sparta.easyspring.auth.service.KakaoService;
import com.sparta.easyspring.auth.service.NaverService;
import com.sparta.easyspring.auth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private static final Logger log = LoggerFactory.getLogger(AuthRestController.class);
    private final UserService userService;
    private final KakaoService kakaoService;
    private final NaverService naverService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(@RequestBody AuthRequestDto requestDto) {
        return userService.signup(requestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto requestDto) {
        return userService.login(requestDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        if (token == null) {
            log.info("sb");
        }
        log.info(token);
        return ResponseEntity.ok(userService.logout(token));
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<AuthResponseDto> withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.withdraw(userDetails);
    }

    @PutMapping("/update/password")
    public ResponseEntity<AuthResponseDto> updatePassword(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody UpdatePasswordRequestDto requestDto) {
        return userService.updatePassword(requestDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestBody RefreshTokenRequestDto requestDto) {
        return userService.refresh(requestDto);
    }

    @GetMapping("/login/kakao")
    public ResponseEntity<AuthResponseDto> kakaoLogin(@RequestParam String code) throws Exception {
        ResponseEntity<AuthResponseDto> response = kakaoService.login(code);
        log.info("userid :"+response.getBody().getId());
        log.info("username :" + response.getBody().getUsername());
        return response;
    }

    @GetMapping("/login/naver")
    public ResponseEntity<AuthResponseDto> naverLogin(@RequestParam String code) throws Exception{
        ResponseEntity<AuthResponseDto> response = naverService.login(code);
        log.info("userid :"+response.getBody().getId());
        log.info("username :" + response.getBody().getUsername());
        return response;
    }
}
