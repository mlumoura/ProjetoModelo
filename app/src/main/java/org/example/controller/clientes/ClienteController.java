package org.example.controller.clientes;

import org.example.model.ClienteDTO;
import org.example.entity.Clientes;
import org.example.response.RespostaBatch;
import org.example.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Clientes> adicionarCliente(@RequestBody Clientes cliente) {
        Clientes novoCliente = clienteService.salvarCliente(cliente);
        return ResponseEntity.status(201).body(novoCliente);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ClienteDTO>> listarTodosClientes() {
        List<Clientes> clientes = clienteService.listarTodos();
        List<ClienteDTO> clienteDTOs = clientes.stream()
                .map(cliente -> new ClienteDTO(cliente.getId(), cliente.getNome(), cliente.getDataCriacao(), cliente.getDataAtualizacao()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(clienteDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Clientes cliente = clienteService.buscarPorId(id);
        return cliente != null
                ? ResponseEntity.ok(new ClienteDTO(cliente.getId(), cliente.getNome(), cliente.getDataCriacao(), cliente.getDataAtualizacao()))
                : ResponseEntity.status(404).body("Cliente não encontrado.");
    }

    @GetMapping("/busca")
    public ResponseEntity<?> buscarPorNome(@RequestParam String nome) {
        List<Clientes> clientes = clienteService.buscarPorNome(nome);
        return clientes.isEmpty()
                ? ResponseEntity.status(404).body("Nenhum cliente encontrado.")
                : ResponseEntity.ok(clientes.stream()
                .map(cliente -> new ClienteDTO(cliente.getId(), cliente.getNome(), cliente.getDataCriacao(), cliente.getDataAtualizacao()))
                .collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCliente(@PathVariable Long id, @RequestBody Clientes clienteAtualizado) {
        Clientes clienteExistente = clienteService.buscarPorId(id);
        if (clienteExistente == null) {
            return ResponseEntity.status(404).body("Cliente não encontrado.");
        }

        clienteAtualizado.setId(id);
        clienteAtualizado.setDataAtualizacao(LocalDateTime.now());
        Clientes clienteAtualizadoFinal = clienteService.salvarCliente(clienteAtualizado);
        return ResponseEntity.ok(clienteAtualizadoFinal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarCliente(@PathVariable Long id) {
        return clienteService.buscarPorId(id) == null
                ? ResponseEntity.status(404).body("Cliente não encontrado.")
                : ResponseEntity.ok("Cliente excluído com sucesso.");
    }

    @PostMapping("/batch")
    public ResponseEntity<RespostaBatch> cadastrarClientesEmLote(@RequestBody List<Clientes> clientes) {
        List<String> erros = new ArrayList<>();

        for (Clientes cliente : clientes) {
            try {
                clienteService.salvarCliente(cliente);
            } catch (Exception e) {
                erros.add("Erro no cliente " + cliente.getNome() + ": " + e.getMessage());
            }
        }

        RespostaBatch resposta = erros.isEmpty()
                ? new RespostaBatch("Todos os clientes foram cadastrados com sucesso!", null)
                : new RespostaBatch("Alguns clientes tiveram erro.", erros);

        return ResponseEntity.status(erros.isEmpty() ? 200 : 400).body(resposta);
    }
}
