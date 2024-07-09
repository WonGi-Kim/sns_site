package com.sparta.easyspring.auth.util;

import com.sparta.easyspring.auth.config.JwtConfig;
import com.sparta.easyspring.auth.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final long tokenExpiration;
    private final long refreshTokenExpiration;
    private final SecretKey secretKey;
    private final RedisUtil redisUtil;

    public JwtUtil(JwtConfig jwtConfig, RedisUtil redisUtil) {
        this.tokenExpiration = jwtConfig.getTokenExpiration();
        this.refreshTokenExpiration = jwtConfig.getRefreshTokenExpiration();
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
        this.redisUtil = redisUtil;
    }

    /**
     * 사용자 이름으로 토큰 발급
     *
     * @param username
     * @return
     */
    public String createAccessToken(String username) {
        return generateToken(username, tokenExpiration);
    }

    public String createRefreshToken(String username) {
        return generateToken((username), refreshTokenExpiration);
    }

    public String createAccessTokenFromRefresh(String refreshToken) {

        if(validateToken(refreshToken)){
            String username = getUsernameFromToken(refreshToken);
            return createAccessToken(username);
        }
        throw new IllegalArgumentException("Refresh토큰이 유효하지 않음");
    }


    /**
     * 토큰 생성
     *
     * @param username
     * @param expiration
     * @return
     */
    public String generateToken(String username, long expiration) {
        return Jwts.builder()
            .setSubject(username) // 토큰 발행 주체
            .setIssuedAt(new Date()) // 토큰 발행 시간
            .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 토큰 만료 시간
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * 토큰 유효성 검사 : claims와 userDetails비교
     * @param token
     * @return
     */
    public boolean validateToken(String token,UserDetailsImpl userDetails) {
        try {
            String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 토큰 유효성 검사
     * @param token
     * @return
     */
    public boolean validateToken(String token){
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * JWT 토큰에서 Claims추출 (사용자 정보)
     *
     * @param token
     * @return
     */
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    /**
     * Bearer 제거하고 token 보내기
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * 토큰 만료일 cliam추출
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        Date expiration = extractClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    // 블랙리스트에 토큰 추가
    public void blacklistToken(String token) {
        long expirationMillis = calculateExpirationMillis();
        System.out.println("Calculated expiration in milliseconds: " + expirationMillis);
        String username = getUsernameFromToken(token);
        redisUtil.setBlackList(token, username, expirationMillis);
    }

    // 블랙리스트에서 토큰 검증
    public boolean isTokenBlacklisted(String token) {
        return redisUtil.hasKeyBlackList(token);
    }

    // 블랙리스트에서 토큰 제거
    public void removeTokenFromBlacklist(String token) {
        redisUtil.deleteBlackList(token);
    }

    // 유효하지 않은 토큰 예외 처리
    private void handleInvalidTokenException(Exception e) {
        throw new IllegalArgumentException("Invalid token: " + e.getMessage());
    }

    // 만료 시간 계산
    private long calculateExpirationMillis() {
        System.out.println("Refresh token expiration (seconds): " + refreshTokenExpiration);
        if (refreshTokenExpiration <= 0) {
            throw new IllegalArgumentException("Refresh token expiration must be greater than 0");
        }
        return refreshTokenExpiration * 1000; // Convert seconds to milliseconds
    }

}
