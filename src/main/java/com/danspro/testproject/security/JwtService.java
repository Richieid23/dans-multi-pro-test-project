package com.danspro.testproject.security;

import com.danspro.testproject.dto.JwtResp;
import com.danspro.testproject.dto.Response;
import com.danspro.testproject.entities.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private String jwtExpirationMs;

    public String generate(HttpServletRequest httpRequest, User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());

        StringBuilder builder = new StringBuilder();
        builder.append("USER_");
        builder.append(user.getId().toString());

        String token = Jwts.builder().setClaims(claims)
                .setSubject(builder.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(jwtExpirationMs)))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();

        return token;
    }

    public Response filter(HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.builder()
                    .rc("999")
                    .message("error")
                    .build();
        }

        final String token = authHeader.substring(7);

        try {
            final Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
            String username = (String) claims.get("username");

            JwtResp data = JwtResp.builder()
                    .username(username)
                    .build();

            return Response.builder()
                    .rc("000")
                    .message("success")
                    .data(data)
                    .build();

        } catch (final SignatureException e) {
            return Response.builder()
                    .rc("999")
                    .message("error")
                    .build();
        } catch (ExpiredJwtException e) {
            return Response.builder()
                    .rc("999")
                    .message("error")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.builder()
                    .rc("999")
                    .message("error")
                    .build();
        }
    }
}
