package com.exemplo.modelo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Clientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    private LocalDateTime dataCriacao;


    @Column(nullable = true) // 🔹 Aceita valores nulos
    private LocalDateTime dataAtualizacao = LocalDateTime.now(); // 🔹 Define um valor padrão automático

    // Construtor padrão
    public Clientes() {
        this.dataCriacao = LocalDateTime.now(); // Atribui a data e hora atuais por padrão
    }

    // Construtor com todos os atributos (exceto o ID que é gerado automaticamente)
    public Clientes(String nome, String email, String telefone, String senha) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.dataCriacao = LocalDateTime.now(); // 🔹 Garante que a data de criação seja sempre registrada
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; } // 🔹 Agora retorna corretamente

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao; // 🔹 Agora permite modificar a data de atualização
    }

    // Método toString para exibir informações
    @Override
    public String toString() {
        return "Clientes{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                '}';
    }
}
