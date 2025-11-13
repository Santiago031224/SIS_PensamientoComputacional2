package com.proyect.pensamiento_comp.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordEncoder that supports both legacy plain-text stored passwords and BCrypt hashes.
 *
 * Matching strategy:
 * - If the stored (encoded) password starts with a BCrypt prefix ($2a$, $2b$, $2y$) -> delegate to BCrypt.
 * - Otherwise perform a plain text equality check.
 *
 * Encoding strategy:
 * - Always returns a BCrypt hash for new raw passwords.
 *
 * This allows gradual migration: existing plain-text rows will continue to work until they are
 * re-saved (at which time they will become BCrypt hashes).
 */
public class PlainOrBcryptPasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder delegate = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        // Always store BCrypt going forward
        return delegate.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null) return false;
        String ep = encodedPassword.trim();
        if (isBcrypt(ep)) {
            return delegate.matches(rawPassword, ep);
        }
        // Legacy plain text comparison
        return rawPassword.toString().equals(ep);
    }

    @Override
    public boolean upgradeEncoding(String encodedPassword) {
        // If legacy plain text, we should upgrade (i.e., return true so higher layers can re-encode)
        return !isBcrypt(encodedPassword);
    }

    private boolean isBcrypt(String value) {
        return value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$");
    }
}
