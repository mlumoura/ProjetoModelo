package com.exemplo.modelo.service;

import com.exemplo.modelo.entity.Clientes;
import com.exemplo.modelo.repository.ClienteRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.exemplo.modelo.exception.EmailJaExisteException;
import com.exemplo.modelo.exception.EmailInvalidoException;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Clientes> buscarPorNomeEData(String nome, LocalDateTime inicio, LocalDateTime fim) {
        return clienteRepository.buscarPorNomeEData(nome, inicio, fim);
    }

    public List<Clientes> listarTodos() {
        return clienteRepository.findAll();
    }

    public Clientes buscarPorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public Clientes salvarCliente(Clientes cliente) {
        // 🔹 Validações antes de salvar no banco
        if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
            throw new IllegalArgumentException("O nome é obrigatório!");
        }
        if (cliente.getEmail() == null || cliente.getEmail().isEmpty()) {
            throw new IllegalArgumentException("O email é obrigatório!");
        }

        // 🔹 Valida formato do email
        validarEmail(cliente.getEmail());

        // 🔹 Verifica se o email já existe no banco
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new EmailJaExisteException("O email já está cadastrado.");
        }

        return clienteRepository.save(cliente);
    }

    private void validarEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(emailRegex)) {
            throw new EmailInvalidoException("Email inválido.");
        }
    }

    public void deletarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public List<Clientes> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Clientes> buscarPorDataCriacao(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return clienteRepository.buscarPorDataCriacao(dataInicio, dataFim); // 🔹 Agora usa o método existente!
    }

    public long contarClientesTotal() {
        return clienteRepository.count();
    }

    public long contarClientesCriadosHoje() {
        LocalDateTime hoje = LocalDateTime.now().toLocalDate().atStartOfDay();
        return clienteRepository.countByDataCriacaoBetween(hoje, hoje.plusDays(1).minusSeconds(1));
    }

    public long contarClientesAtualizadosHoje() {
        LocalDateTime hoje = LocalDateTime.now().toLocalDate().atStartOfDay();
        return clienteRepository.countByDataAtualizacaoBetween(hoje, hoje.plusDays(1).minusSeconds(1));
    }

    public List<Clientes> buscarCriadosEAtualizados(LocalDateTime inicio) {
        return clienteRepository.buscarCriadosEAtualizados(inicio);
    }

    public List<Clientes> buscarPorAtualizacao(LocalDateTime inicio, LocalDateTime fim) {
        return clienteRepository.buscarPorAtualizacao(inicio, fim);
    }

    public List<Clientes> buscarSomenteAtualizados() {
        return clienteRepository.buscarSomenteAtualizados();
    }




}
