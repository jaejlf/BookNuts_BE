package team.nine.booknutsbackend.config;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import team.nine.booknutsbackend.exception.user.InvalidTokenException;
import team.nine.booknutsbackend.service.RedisService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private String secretKey = "booknutssecret";

    private long accessTokenValidTime = 30 * 60 * 1000L; //30분
    private long refreshTokenValidTime = 30 * 24 * 60 * 60 * 1000L; //30일 (한 달)

    private final UserDetailsService userDetailsService;
    private final RedisService redisService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(String email) {
        return createToken(email, accessTokenValidTime);
    }

    public String createRefreshToken(String email) {
        String refreshToken = createToken(email, refreshTokenValidTime);
        redisService.setValues("token-" + email, refreshToken, Duration.ofMillis(refreshTokenValidTime));
        return refreshToken;
    }

    public String createToken(String email, Long tokenValidTime) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    //JWT 토큰에서 인증 정보 조회
    public Authentication getAuthetication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    //Request의 Header에서 token 값을 가져온다 => "X-AUTH-TOKEN" : "TOKEN값"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    //토큰의 유효성 체크
    public boolean validateToken(String jwtToken) throws InvalidTokenException {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return false;
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    //토큰의 만료 일자 확인
    public Long getValidTime(String jwtToken) {
        Date now = new Date();
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
        return claims.getBody().getExpiration().getTime() - now.getTime();
    }

}