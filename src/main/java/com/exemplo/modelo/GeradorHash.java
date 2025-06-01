package com.exemplo.modelo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaHash = encoder.encode("admin123"); // Gerando hash da senha
        System.out.println("Hash gerado: " + senhaHash);
    }
}

