package com.proyect.pensamiento_comp.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil();

    @Test
    void generateAndValidateToken_ok() {
        String token = jwtUtil.generateToken("user@example.com", List.of("ADMIN"));
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));

        Claims claims = jwtUtil.getClaims(token);
        assertEquals("user@example.com", claims.getSubject());
        assertEquals(List.of("ADMIN"), claims.get("roles", List.class));
    }

    @Test
    void validateToken_invalid() {

        String invalid = "invalid.token.value";
        assertFalse(jwtUtil.validateToken(invalid));
    }
}

