package com.board.weare.config.jwt;

import com.board.weare.entity.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    @Value("spring.jwt.secret")
    private String secretKey;
    private final AccountServiceImpl accountService;

    // 토큰 유효시간 30분
    private long tokenValidTime = 30 * 60 * 1000L;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String createToken(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }

    // Jwt 토큰에서 회원 구별 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public JwtInfo.Payload getPayloadIsNotExpired(String accessToken, String refreshToken){
        if(accessToken == null){
            throw new TokenIsNotGiven("authorization");
        }
        if(refreshToken == null){
            throw new TokenIsNotGiven("refresh_token");
        }

        if(!validateToken(accessToken) && !validateToken(refreshToken)){ // 둘 다 만료되었을 경우
            throw new TokenExpiredException();
        }

        //TODO accessToken, refreshToken 중 만료된 거 변경해주는거 해야 함..

        return getPayload(accessToken, refreshToken);
    }

    public JwtInfo.Payload getPayload(String accessToken, String refreshToken){
        JwtInfo.Payload payload = new JwtInfo.Payload(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody());
        System.out.println("login user name = " + payload.getName() + ", role = " + payload.getRole());
        return payload;
    }

    // Request의 Header에서 token 파싱 : "Authorization: jwt토큰"
    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("Authorization");
    }

    // Request의 Header에서 token 파싱 : "refresh_token: jwt refresh token"
    public String resolveRFToken(HttpServletRequest req) {
        return req.getHeader("refresh_token");
    }

    // Jwt 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token, String rfToken) {
        JwtInfo.Payload payload = getPayloadIsNotExpired(token, rfToken);
        String accountId = payload.getSub();
        Account account = accountService.get(accountId);
        return new UsernamePasswordAuthenticationToken(account, "", account.getAuthorities());
    }
}