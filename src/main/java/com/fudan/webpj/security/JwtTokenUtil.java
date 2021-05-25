package com.fudan.webpj.security;

import com.fudan.webpj.entity.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil implements Serializable {
    @Value("${token_Header}")
    private String tokenHeader;

    private static Key getKeyInstance() {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] bytes = DatatypeConverter.parseBase64Binary("mall-user");
        return new SecretKeySpec(bytes, signatureAlgorithm.getJcaName());
    }

    public String generateToken(User user) {
        JwtInfo jwtInfo = new JwtInfo(user.getId());
        return Jwts.builder()
                .claim(JwtConstants.JWT_KEY_USER_ID, jwtInfo.getUid())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //# 30 * 60 * 1000 = 0.5 hours
                .setExpiration(new Date(System.currentTimeMillis() + 1800000))
                .signWith(SignatureAlgorithm.HS256, getKeyInstance())
                .compact();
    }

    public String getUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getKeyInstance())
                .parseClaimsJws(token).getBody();
        return claims.get(JwtConstants.JWT_KEY_USER_ID).toString();
    }

    public boolean validateToken(String token) throws ExpiredJwtException {
        Claims claims = Jwts.parser()
                .setSigningKey(getKeyInstance())
                .parseClaimsJws(token).getBody();
        Date expiration = claims.getExpiration();
        System.out.println("" + expiration);
        return !expiration.before(new Date());
    }

    public String resolveToken(String authorization) {
        return authorization.substring(tokenHeader.length());
    }

}
