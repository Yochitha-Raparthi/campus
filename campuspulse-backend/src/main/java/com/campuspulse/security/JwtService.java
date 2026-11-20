package com.campuspulse.security;
import io.jsonwebtoken.*;import io.jsonwebtoken.security.Keys;import javax.crypto.SecretKey;import java.util.*;import org.springframework.beans.factory.annotation.Value;import org.springframework.stereotype.Service;
@Service public class JwtService{
private final SecretKey key;
public JwtService(@Value("${app.jwt.secret:}") String secret){if(secret.isBlank())throw new IllegalStateException("JWT_SECRET must be configured");byte[] bytes;try{bytes=Base64.getDecoder().decode(secret);}catch(IllegalArgumentException e){throw new IllegalStateException("JWT_SECRET must be Base64",e);}if(bytes.length<32)throw new IllegalStateException("JWT_SECRET must decode to at least 32 bytes");key=Keys.hmacShaKeyFor(bytes);}
public String create(String email){return Jwts.builder().subject(email).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis()+8*60*60*1000L)).signWith(key).compact();}
public String subject(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();}
}
