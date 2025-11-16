package org.kzk.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.jetbrains.annotations.NotNull;
import org.kzk.data.AuthRepository;
import org.kzk.data.UserRepository;
import org.kzk.data.entity.UserAuthEntity;
import org.kzk.security.ex.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;

@Component
public class SecurityService {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.experation}")
    private Integer expirationSeconds;
    @Value("${jwt.issuer}")
    private String issuer;

    @Autowired
    public SecurityService(
            AuthRepository authRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<TokenDetails> authenticate(String username, String password) {
        return userRepository.findUserWithAuth(username)
                .flatMap(user -> {
                    if (!user.isEnabled()) {
                        return Mono.error(new AuthException("This user is disabled", "USER_ACCOUNT_DISABLED"));
                    }

                    if (!passwordEncoder.matches(password, user.passwordHash())) {
                        return Mono.error(new AuthException("Invalid password - try again", "INVALID_PASSWORD"));

                    }

                    return Mono.just(generateToken(user)
                            .toBuilder()
                            .userId(user.userId())
                            .build());
                })
                .switchIfEmpty(Mono.error(new AuthException("User is not exist!", "USER_NOT_FOUND")));
    }

    TokenDetails generateToken(@NotNull UserAuthEntity user) {
        Map<String, Object> claims = new HashMap<>() {{
            put("role", user.role().name());
            put("username", user.username());
        }};

        return generateToken(claims, user.userId().toString());
    }


    TokenDetails generateToken(
            Map<String, Object> claims,
            String subject
    ) {
        Instant instant = Instant.now().plusSeconds(expirationSeconds);
        Date expirationDt = Date.from(instant);
        return generateToken(expirationDt, claims, subject);
    }

    TokenDetails generateToken(
            Date expirationDt,
            Map<String, Object> claims,
            String subject
    ) {

        Date createdDt = new Date();
        String token = Jwts.builder()
                .claims().add(claims).and()
                .issuer(issuer)
                .subject(subject)
                .issuedAt(createdDt)
                .id(UUID.randomUUID().toString())
                .expiration(expirationDt)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        return TokenDetails.builder()
                .token(token)
                .issuedAt(createdDt)
                .expiresAt(expirationDt)
                .build();
    }


    @NotNull
    private SecretKey getSignInKey() {// org.kzk.security.JwtHandler.getSignInKey
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
