package com.gallego.ms_common.service;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    
    /**
     * Clave secreta para firmar los tokens, se inyecta desde el application.properties
     */
    @Value("${security.jwt.secret-key}") 
    String secretKey;
    
    /**
     * Tiempo de expiracion del token en milisegundos, se inyecta desde el application.properties
     */
    @Value("${security.jwt.token-expiration}") 
    Long tokenExpiration;

    /**
     * Transforma la clave secreta de String (base64) a un objeto SecretKey utilizable por la libreria
     * @return firma secreta
     */
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Long userId, Long rolId, String name) {
        return Jwts.builder()
                .claims(Map.of("userId", userId))
                .claims(Map.of("rolId", rolId))
                .subject(name)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSignKey())
                .compact();
    }

    /**
     * Verifica si el token es valido, comprobando su firma y su fecha de expiracion
     * @param token
     * @return
     */
    public Boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Comprueba si el token pertenece a un usuario con rol de administrador (rolId = 1)
     * @param token
     * @return
     */
    public boolean isAdmin(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            Long rolId = claims.get("rolId", Long.class);
            return rolId != null && rolId == 1L;
        } catch (JwtException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrae el nombre de usuario del token
     * @param token
     * @param <T>
     * @param token
     * @param resolver
     * @return
     */
    public <T> T extractClaims(String token, Function<Claims, T> resolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }

    /**
     * Extrae el nombre de usuario del token
     * @param token
     * @return
     */
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    /**
     * Extrae el id del usuario del token
     * @param token
     * @return
     */
    public Long extractUserId(String token) {
        return extractClaims(token, claims -> claims.get("userId", Long.class));
    }

    /**
     * Extrae el rol del usuario del token
     * @param token
     * @return
     */
    public Long extractRolId(String token) {
        return extractClaims(token, claims -> claims.get("rolId", Long.class));
    }

    /**
     * Refrescar el token de seguridad, generando uno nuevo con la misma informacion pero con una nueva fecha de expiracion
     * @param token
     * @return
     * @throws Exception
     */
    public String refreshToken(String token) throws Exception {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new Exception("Token is expired");
        } catch (JwtException e) {
            throw new Exception("Token is invalid");
        }
        return generateToken(claims.get("userId", Long.class), claims.get("rolId", Long.class), claims.getSubject());
    }
}