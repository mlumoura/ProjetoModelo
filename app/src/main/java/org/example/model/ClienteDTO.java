package org.example.model;

import org.example.entity.Clientes;
import java.time.LocalDateTime;

public class ClienteDTO {
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao; // 🔹 Novo campo para acompanhar modificações
    private Long id;
    private String nome;
    private String email;

    public ClienteDTO(Clientes cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
        this.dataCriacao = cliente.getDataCriacao();
        this.dataAtualizacao = cliente.getDataAtualizacao(); // 🔹 Agora inclui a data de atualização
    }

    public ClienteDTO(Long id, String nome, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.nome = nome;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}
