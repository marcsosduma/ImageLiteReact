package br.duma.demo.application.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;
import br.duma.demo.domain.AccessToken;
import br.duma.demo.domain.entity.User;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

/**
 * JwtService
 */
@Service
@RequiredArgsConstructor
public class JwtService  {
    private final SecretKeyGenerator keyGenerator;

    public AccessToken generateToken(User user){
        var key = keyGenerator.getKey();
        var expirationDate = generateExpirationDate();
        var claims = generateTokenClaims(user);

        String token = Jwts.builder()
                            .signWith(key)
                            .subject(user.getEmail())
                            .expiration(expirationDate)
                            .claims(claims)
                            .compact();
        return new AccessToken(token);
    }    

    private Date generateExpirationDate(){
        var expirationMinutes = 60;
        LocalDateTime now = LocalDateTime.now().plusMinutes(expirationMinutes);
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Map<String, Object> generateTokenClaims(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName()) ;
        return claims;
    }

    public String getEmailFromToken(String tokenJwt){
        try{
         return Jwts.parser()
             .verifyWith(keyGenerator.getKey())
             .build()
             .parseSignedClaims(tokenJwt)
             .getPayload()
             .getSubject(); //email
        }catch(Exception e){
            throw new InvalidTokenException(e.getMessage());
        }
    }

}