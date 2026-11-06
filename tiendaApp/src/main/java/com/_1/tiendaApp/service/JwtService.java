package com._1.tiendaApp.service;

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
     * clave secreta
     */
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    /**
     * expiracion del token en milisegundos
     */
    @Value ("${security.jwt.token-expiration}")
    private Long tokenExpiration;

    /**
     * trasforma la clave secreta a un objeto SecretKey
     * @return
     */
    private SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * metodo para la creacion del token (jwt)
     * @param userName nombre del usuario
     * @param rolId el rol del usuario
     * @param userId el id del usuario
     * @return String jwt
     */
    public String generateToken (Long userId, String userName , Long rolId){
        return Jwts.builder().claims(Map.of("userId", userId))
        .claims(Map.of("rolId", rolId))
        .subject(userName)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
        .signWith(getSignKey())
        .compact();
    }

    /**
     * verifica si el token es valido y no hasido manipulado
     * @param token el jwt a validar
     * @return true si la firma es valida, false en caso contrario
     */
    public Boolean isTokenValid(String token){
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
     * @param token actual
     * @param resolver claim especifico
     * @return
     */    
    private  <T> T extractClaims(String token, Function<Claims, T> resolver){
        final Claims claims = Jwts.parser()
        .verifyWith(getSignKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

        return  resolver.apply(claims);
    }

    /**
     * metoo para extraer el propietario del token
     * 
     * @param token token actual
     * @return nombre del usuario
     */
    public  String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    /**
     * metodo para extraer el id del usuario
     * @param token
     * @return Long id del usuario
     */
    public Long extractUserId(String token) {
        return extractClaims(token, claims -> claims.get("userId", Long.class));
    }

    /**
     * metodo para extraer el rol del usuario
     * @param token
     * @return Long rol del usuario
     */
    public Long extractRolId (String token) {
        return extractClaims(token, claims -> claims.get("rolId", Long.class));
    }

    /**
     * verificamos que no hubo ningun error en el token para crear uno nuevo
     * 
     * @param token
     * @return el nuevo token
     * @throws Exception en caso que ocurra un error
     */
    public  String refreshToken (String token) throws Exception{
        Claims claims;
        try {
            claims = Jwts.parser()
            .verifyWith(getSignKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();

        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            throw new Exception("token is expired" + e.getMessage());
        } catch(JwtException e){
            e.printStackTrace();
            throw new Exception("token is invalid" + e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            throw new Exception("hubo un error al validar el token" + e.getMessage());
        }

        return generateToken(claims.get("userId", Long.class), claims.getSubject(), claims.get("rolId", Long.class));
    }




}
