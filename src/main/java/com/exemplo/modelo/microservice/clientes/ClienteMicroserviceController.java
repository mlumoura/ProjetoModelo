package com.exemplo.modelo.microservice.clientes;

import com.exemplo.modelo.entity.Clientes;
import com.exemplo.modelo.model.ClienteDTO;
import com.exemplo.modelo.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
public class ClienteMicroserviceController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<ClienteDTO> listarClientes() {
        return clienteService.listarTodos().stream()
                .map(cliente -> new ClienteDTO(cliente))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ClienteDTO buscarPorId(@PathVariable Long id) {
        Clientes cliente = clienteService.buscarPorId(id);
        return cliente != null ? new ClienteDTO(cliente) : null;
    }

    @PutMapping("/{id}")
    public ClienteDTO atualizarCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO) {
        Clientes clienteExistente = clienteService.buscarPorId(id);
        if (clienteExistente == null) {
            return null; // Cliente não encontrado
        }

        clienteExistente.setNome(clienteDTO.getNome());
        clienteExistente.setEmail(clienteDTO.getEmail());
        clienteExistente.setDataAtualizacao(LocalDateTime.now()); // 🔹 Atualiza a data de alteração

        Clientes clienteAtualizado = clienteService.salvarCliente(clienteExistente);
        return new ClienteDTO(clienteAtualizado);
    }

    @DeleteMapping("/{id}")
    public String deletarCliente(@PathVariable Long id) {
        Clientes cliente = clienteService.buscarPorId(id);
        if (cliente == null) {
            return "Cliente não encontrado.";
        }

        clienteService.deletarCliente(id);
        return "Cliente excluído com sucesso.";
    }
}
