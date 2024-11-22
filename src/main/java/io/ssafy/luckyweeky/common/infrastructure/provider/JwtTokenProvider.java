package io.ssafy.luckyweeky.common.infrastructure.provider;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.ssafy.luckyweeky.common.DispatcherServlet;

import java.io.File;
import java.security.Key;
import java.util.Date;

public class JwtTokenProvider {
    private final static JwtTokenProvider instance = new JwtTokenProvider();
    private final Key SECREATKEY; // JWT 서명을 위한 Secret Key

    private JwtTokenProvider() {
        Dotenv dotenv = Dotenv.configure()
                .directory(DispatcherServlet.getWebInfPath()+ File.separatorChar)
                .filename(".env")
                .load();
        this.SECREATKEY = Keys.hmacShaKeyFor(dotenv.get("SECREATKEY").getBytes()); // Secret Key 생성
    }

    public static JwtTokenProvider getInstance() {
        return instance;
    }

    /**
     * JWT 토큰 생성
     * @param subject 토큰의 Subject (예: 사용자 ID)
     * @param claims  추가 클레임 (예: 권한 정보)
     * @return 생성된 JWT 토큰
     */
    public String createToken(String subject, Claims claims,long validityInMilliseconds) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims) // 클레임 설정
                .setSubject(subject) // Subject 설정 (예: 사용자 ID)
                .setIssuedAt(now) // 생성 시간
                .setExpiration(validity) // 만료 시간
                .signWith(SECREATKEY, SignatureAlgorithm.HS256) // 서명 알고리즘
                .compact(); // 토큰 생성
    }

    /**
     * JWT 토큰에서 클레임 추출
     * @param token JWT 토큰
     * @return 토큰에 포함된 클레임
     */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECREATKEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 토큰 유효성 검사
     * @param token JWT 토큰
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date()); // 만료 시간 확인
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 토큰에서 Subject 추출
     * @param token JWT 토큰
     * @return Subject (예: 사용자 ID)
     */
    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }
}
