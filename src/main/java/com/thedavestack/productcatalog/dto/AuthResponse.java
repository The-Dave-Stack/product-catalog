package com.thedavestack.productcatalog.dto;

public record AuthResponse(
        String token, String type, String username, String role, long expiresIn) {
    public static AuthResponse of(String token, String username, String role, long expiresIn) {
        return new AuthResponse(token, "Bearer", username, role, expiresIn);
    }
}
