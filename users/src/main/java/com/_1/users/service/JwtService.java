package com._1.users.service;

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
     * Clave secreta
     */
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    /**
     * expiracion del token
     */
    @Value("${security.jwt.token-expiration}")
    private Long tokenExpiration;

    /**
     * Trasforma la clave secreta de string (BASE64) a un onjeto SecretKey
     * utilizable por la libreria
     * 
     * @return SecretKey
     */
    private SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Metodo para la creacion del token (jwt)
     * 
     * @param userId id del usuario
     * @param userName nombre del usuario(dueño del token)
     * @param rolId id del rol del usuario
     * @return String JWT
     */
    public String generateToken(Long userId, String userName, Long rolId){

        return Jwts.builder()  // Se empieza a construir el token
        .claims(Map.of("userId", userId)) // Se agrega datos personalisados (payload)
        .claims(Map.of("rolId", rolId)) // Se agrega datos personalisados (payload)
        .subject(userName) // identificamos el dueño del token
        .issuedAt(new Date()) // fecha de creacion
        .expiration(new Date(System.currentTimeMillis() + tokenExpiration)) // fecha de expiracion expresada en ms(actual) + expiracion de ms(10 min en ms)
        .signWith(getSignKey()) // firma digital para evitar alteracion del token
        .compact(); // se une todo para acabar de cunstruir

    }

    /**
     * verifica si el token es valido y no ha sido manipulado
     * 
     * @param token el jwt a validar
     * @return true si la firma es valida, falso en caso contraio
     */
    public Boolean istokenValid (String token) {
        try {
            Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * metodo generico para extraer todos los claims del token(payload)
     * 
     * @param <T> cualquier tipado
     * @param token  actual
     * @param resolver claim especifico
     * @return
     */    
    private <T> T extractClaims(String token, Function<Claims, T> resolver){
        final Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        
        return resolver.apply(claims);
    }

    /**
     * metodo para extraer el propietario del token (jwt)
     * 
     * @param token actual
     * @return nombre de usuario
     */
    public String extracUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }


    /**
     * metodo para extraer el id del usuario del token (jwt)
     * 
     * @param token 
     * @return
     */
    public Long extractUserId(String token) {
        return extractClaims(token, claims -> claims.get("userId", Long.class));
    }


    /**
     * metodo para extraer el rol del usuario del token (jwt)
     * @param token
     * @return
     */
    public Long extractRolId(String token) {
        return extractClaims(token, claims -> claims.get("rolId", Long.class));
    }

    
    /**
     * metodo para refrescar el token
     * 
     * @param token
     * @return token nuevo
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
            e.printStackTrace();
            throw new Exception("token is expired " + e.getMessage());
        } catch (JwtException e){
            e.printStackTrace();
            throw new Exception("token is invalid " + e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception("Hubo un error al validar el token " + e.getMessage());

        }

        return generateToken(claims.get("userId", Long.class), claims.getSubject(), claims.get("rolId", Long.class));
    }
}
