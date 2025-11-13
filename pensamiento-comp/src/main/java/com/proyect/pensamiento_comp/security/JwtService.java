package com.proyect.pensamiento_comp.security;

import com.proyect.pensamiento_comp.model.User;
import com.proyect.pensamiento_comp.repository.IUserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    
    private final JwtUtil jwtUtil;
    private final IUserRepository userRepository;

    public JwtService(JwtUtil jwtUtil, IUserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    // Extrae el token del header Authorization
    public String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    // Valida la firma y expiración
    public boolean validateToken(String token) {
        return token != null && jwtUtil.validateToken(token);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtUtil.getClaims(token);
        String username = claims.getSubject();

        log.info("🔐 Getting authentication for user: {}", username);

        // Avoid Oracle DISTINCT over CLOBs by fetching in two steps (email -> id -> roles)
        User base = userRepository.findByEmail(username).orElse(null);
        if (base == null) {
            log.warn("❌ User not found: {}", username);
            return null;
        }
        User user = userRepository.findByIdWithRoles(base.getId()).orElse(base);

        if (user == null) {
            log.warn("❌ User with roles not found for ID: {}", base.getId());
            return null;
        }
        
        log.info("👤 User {} has {} roles", username, user.getRoles().size());
        user.getRoles().forEach(role -> 
            log.info("   - Role: {} (permissions count: {})", role.getName(), role.getPermissions().size())
        );
        
        // Build authorities from both permissions and roles
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .flatMap(role -> {
                    // Initialize permissions collection to avoid LazyInitializationException
                    role.getPermissions().size();
                    return role.getPermissions().stream();
                })
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList());
        
        log.info("🔑 Loaded {} permission-based authorities", authorities.size());
        authorities.forEach(auth -> log.info("   - Permission: {}", auth.getAuthority()));
        
        // Add role-based authorities (ROLE_ADMIN, ROLE_PROFESSOR, etc.)
        user.getRoles().forEach(role ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()))
        );

        log.info("✅ Total authorities for {}: {}", username, authorities.size());
        authorities.forEach(auth -> log.info("   - Authority: {}", auth.getAuthority()));

        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }

    public JwtUtil getJwtUtil() {
        return jwtUtil;
    }
}
