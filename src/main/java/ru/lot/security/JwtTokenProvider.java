package ru.lot.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.lot.entity.User;
import ru.lot.service.UserService;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
    }

    public String createJWT(Long userId, String username, String role) {
        Date now = new Date();

        Date expiration = new Date(now.getTime() + Long.parseLong(jwtProperties.getTokenDuration()));

        Claims claims = Jwts.claims()
                .subject(username)
                .id(userId.toString())
                .add("scope", role)
                .build();
        return jwtProperties.getTokenPrefix() + Jwts.builder().claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes()))
                .compact();
    }

    @Transactional
    public Authentication getUserAuthorizationFromJWT(String token) throws
            ExpiredJwtException, AuthorizationServiceException {
        token = token.replace(jwtProperties.getTokenPrefix(), "");

        Jws<Claims> claims = Jwts.parser().verifyWith((SecretKey) Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes()))
                .build().parseSignedClaims(token);
        User user = (User) userService.loadUserByUsername(claims.getPayload().getSubject());
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }
}
