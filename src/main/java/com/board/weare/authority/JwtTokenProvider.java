package com.board.weare.authority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * JWT : JSON Web Token
 * 쿠키와 달리 서명된 토큰이다. 공개/개인키 쌍으로 사용.
 * 토큰에 서명할 시 서명된 토큰은 개인키를 보유한 서버가 해당 토큰을 인증할 수 있다.
 *
 * 구조 : Header.Payload.Signature
 * Header   : 헤더 (토큰의 타입, 서명 알고리즘 등을 포함)
 * Payload  : 내용 (Claim 이라는 사용자/토큰의 속성을 KEY-VALUE 형태로 저장)   * KEY는 3글자
 *  * Header와 Payload에는 민감한 정보를 담지 않아야 한다. => 누구나 JWT를 가지고 디코딩 가능. 그래서 굳이 암호화를 할 필요가 없다.
 * Signature: 서명 (header디코딩값 + payload디코딩값 을 서버 개인키로 암호화)
 *
 * 1. 브라우저가 로그인 요청을 보냄
 * 2. 로그인에 성공한 경우 200 OK와 함께 토큰 전달
 * 3. 그 이후로 요청을 보낼 때 토큰을 껴서 보냄
 * 4. 인증이 필요한 도메인의 경우 서버에서 해당 토큰을 검증하여 권한이 있는지 확인한다.
 */
@RequiredArgsConstructor
@Component
@Log4j2
public class JwtTokenProvider {
    private  String privateKey = "i3e1security";

    private long tokenValidityTime = 60*60*1000L;   // 토큰 유효시간

    @Autowired
    private UserDetailsService userDetailsService;

    @PostConstruct
    protected void init(){
        privateKey = Base64.getEncoder().encodeToString(privateKey.getBytes(StandardCharsets.UTF_8));
        log.info("로그테스트트트트트");
    }

    // 토큰 생성하기
    public String createToken(String userPK, List<String> roles){
        log.info("### createToken");
        log.info("userPK : "+ userPK + "  roles : "+ roles.isEmpty());
        Claims claims= Jwts.claims().setSubject(userPK);    // JWT payload에 저장되는 정보단위
        claims.put("roles", roles);                         // key-value
        long begin = System.currentTimeMillis();
        return Jwts.builder().setClaims(claims)                     // 정보 저장
                .setIssuedAt(new Date(begin))                       // 토큰 발행 시간
                .setExpiration(new Date(begin+tokenValidityTime))   // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS256, privateKey) // 사용할 알고리즘, 서버 개인키
                .compact();
    }

    // token에 담긴 usePK를 가져옴         FIXME : 메소드를 아래 메소드와 분리한 이유 없으면 그냥 합치기
//    public String getUserPK(String token){
//        return getClaims(token).getSubject();
//    }

    // token에서 추출한 회원정보로 인증 정보를 조회함
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getClaims(token).getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getToken(HttpServletRequest request){
        return request.getHeader("X-AUTH-TOKEN");   // FIXME : token header key 확인
    }

    public boolean validateToken(String token){
        try{
            return getClaims(token).getExpiration().before(new Date());
        }catch (Throwable th){
            return false;
        }
    }

    public Claims getClaims(String token){
        return Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token).getBody();
    }
}
