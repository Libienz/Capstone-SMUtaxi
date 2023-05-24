package com.capstone.smutaxi.config.jwt;

import com.capstone.smutaxi.exception.auth.InvalidLoginException;
import com.capstone.smutaxi.exception.auth.TokenInvalidExpiredException;
import com.capstone.smutaxi.exception.auth.TokenInvalidFormException;
import com.capstone.smutaxi.exception.auth.TokenInvalidSecretKeyException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * JWT(Json Web Token)는 세가지 부분으로 구성
 *
 * @Header 헤더는 토큰의 유형과 암호화 알고리즘 등의 메타데이터를 포함
 * @PayLoad 토큰에 담길 정보, 즉 클레임(Claim)이라고 불리는 키-값 쌍들을 포함 다음은 payload 예시
 * {
 * "iss": "example.com", // 발급자 (issuer)
 * "sub": "user123", //토큰의 주체 (subject)
 * "aud": "api.example.com", // 토큰의 대상(audience) 접근 권한이 있는 API 서버
 * "exp": 1621512000, //만료시간
 * "iat": 1621425600 //발급시간
 * }
 * @Signature 헤더와 페이로드를 합친 후 비밀 키를 사용하여 생성되는 서명 토큰
 * 토큰의 변조 여부를 체크한다. (서버 만이 비밀 키를 알고 있음)
 * 현재 비밀키는 yml파일에 존재, 후에 비밀키 바꾸고 yml파일 숨겨야 한다!
 */
@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long validityInMilliseconds;
    private final UserDetailsService userDetailsService;

    //secretKey와 validityInMilliseconds는 생성자의 매개변수이지만 @Value어노테이션을 이용해 application.yml에서 값을 주입받고 있음
    public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") final String key,
                            @Value("${security.jwt.token.expire-length}") final long validityInMilliseconds, UserDetailsService userDetailsService) {
        this.key = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        this.validityInMilliseconds = validityInMilliseconds;
        this.userDetailsService = userDetailsService;
    }

    // Request의 Header에서 token 값 가져오기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후의 토큰 부분만 반환
        }
        return null;
    }

    //주어진 payload를 기반으로 토큰 생성
    // 토큰 생성
    public String createToken(String userPk, List<String> roles) {  // userPK = email
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + validityInMilliseconds)) // 토큰 유효시각 설정
                .signWith(SignatureAlgorithm.HS256, key)  // 암호화 알고리즘과, secret 값
                .compact();
    }

    //토큰에서 Subject가 누구인지 추출하는 메서드
    public String getUserPk(final String token) {
        return tokenToJws(token) //token -> JWS (JWT Signature)
                .getBody() //Json 형식으로 인코딩된 클레임 정보를 가져온다.
                .getSubject(); //subject get
    }

    //토큰의 유효성 검증 메서드
    public void validateAbleToken(final String token) {
        try {
            final Jws<Claims> claims = tokenToJws(token);

            validateExpiredToken(claims); //토큰의 만료 여부 검증
        } catch (final JwtException | InvalidLoginException e) {
            throw new TokenInvalidSecretKeyException(token);
        }
    }

    // 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //토큰을 JWS로 변환 JWS는 클레임단위로 정보를 가지고 있다.
    private Jws<Claims> tokenToJws(final String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (final IllegalArgumentException | MalformedJwtException e) {
            throw new TokenInvalidFormException();
        } catch (final SignatureException e) {
            throw new TokenInvalidSecretKeyException(token);
        } catch (final ExpiredJwtException e) {
            throw new TokenInvalidExpiredException();
        }
    }

//    토큰 만료 여부 검증
    private void validateExpiredToken(final Jws<Claims> claims) {
        if (claims.getBody().getExpiration().before(new Date())) {
            throw new TokenInvalidExpiredException();
        }
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
