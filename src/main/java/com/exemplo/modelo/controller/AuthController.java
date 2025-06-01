package com.exemplo.modelo.controller;

import com.exemplo.modelo.dto.LoginRequest;
import com.exemplo.modelo.dto.AuthResponse;
import com.exemplo.modelo.entity.Usuario;
import com.exemplo.modelo.repository.UsuarioRepository;
import com.exemplo.modelo.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService; // Serviço de geração de token JWT

    public AuthController(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@RequestBody LoginRequest loginRequest) {
        Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername());
        if (usuario != null && passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
            String token = jwtService.generateToken(usuario); // Agora geramos um JWT válido!
            return ResponseEntity.ok(new AuthResponse(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }
}


