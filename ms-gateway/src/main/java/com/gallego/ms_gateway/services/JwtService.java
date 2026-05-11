package com.gallego.ms_gateway.services;

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
    
    @Value("${security.jwt.secret-key}") 
    String secretKey;
    
    @Value("${security.jwt.token-expiration}") 
    Long tokenExpiration;


    /**
     * Transformamos la clave secreta de String (base64) a un objeto SecretKey
     * utilizable por la libreria
     * 
     * @return firma secreta
     */
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generar el token de seguridad al inicar sesion
     * 
     * @param userId
     * @param rolId
     * @param name
     * @return
     */
    public String generateToken (Long userId, Long rolId, String name){
        return Jwts.builder()
                .claims(Map.of("userId", userId)) // claims personalizados (informacion adicional)
                .claims(Map.of("rolId", rolId))
                .subject(name) // claim por defecto (a quien pertenece este token)
                .issuedAt(new Date()) // fecha de creacion
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration)) // fecha de expiracion
                .signWith(getSignKey())
                .compact();
    }

    /**
     * Verificar si el token es valido
     * 
     * @param token
     * @return booleano
     */
    public Boolean isTokenValid(String token){
        try {
            Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Extraer todos los claims del token
     * 
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
     * Extraer el nombre de usuario del token
     * 
     * @param token
     * @return nombre de usuario
     */
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    /**
     * Extrae el id del usuario
     * 
     * @param token
     * @return id del usuario
     */
    public Long extractUserId(String token) {
        return extractClaims(token, claims -> claims.get("userId", Long.class));
    }

    /**
     * Extrae el rol del usuario
     * 
     * @param token
     * @return rol del usuario
     */
    public Long extractRolId(String token) {
        return extractClaims(token, claims -> claims.get("rolId", Long.class));
    }

    public String refreshToken(String token) throws Exception {
        Claims claims;

        try {
            claims = Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new Exception("Token is expired" + e.getMessage());
        } catch (JwtException e) {
            throw new Exception("Token is invalid" + e.getMessage());
        }

        return generateToken(claims.get("userId", Long.class), claims.get("rolId", Long.class), claims.getSubject());
    }
}  
