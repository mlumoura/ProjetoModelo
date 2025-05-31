package com.exemplo.modelo.controller.clientes;

import com.exemplo.modelo.entity.Clientes;
import com.exemplo.modelo.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/clientes-view") // 🔹 Alteração para evitar conflito com API REST
public class ClienteViewController {

    @Autowired
    private ClienteService clienteService;

    private LocalDateTime converterData(String data, boolean isFim) {
        if (data == null || data.isEmpty()) return null;
        try {
            return isFim ? LocalDate.parse(data).atTime(23, 59, 59) : LocalDate.parse(data).atStartOfDay();
        } catch (Exception e) {
            return null; // 🔹 Evita erro de conversão inesperado
        }
    }

    @GetMapping("/listar-html")
    public String listarClientes(Model model) {
        List<Clientes> clientes = clienteService.listarTodos();
        model.addAttribute("clientes", clientes);
        return "clientes/listarClientes";
    }

    @GetMapping("/consulta")
    public String mostrarConsulta() {
        return "clientes/consulta";
    }

    @GetMapping("/buscar-id")
    public String buscarPorId(@RequestParam Long id, Model model) {
        try {
            Clientes cliente = clienteService.buscarPorId(id);
            model.addAttribute("clientes", cliente != null ? List.of(cliente) : List.of());
            model.addAttribute("erro", cliente == null ? "Cliente não encontrado!" : null);
        } catch (Exception e) {
            model.addAttribute("erro", "Ocorreu um erro inesperado.");
        }
        return "clientes/consulta";
    }

    @GetMapping("/buscar-nome")
    public String buscarPorNome(@RequestParam String nome, Model model) {
        try {
            List<Clientes> clientes = clienteService.buscarPorNome(nome);
            model.addAttribute("clientes", clientes);
            model.addAttribute("erro", clientes.isEmpty() ? "Nenhum cliente encontrado com esse nome." : null);
        } catch (Exception e) {
            model.addAttribute("erro", "Erro inesperado ao buscar clientes.");
        }
        return "clientes/consulta";
    }

    @GetMapping("/buscar-data-html")
    public String buscarPorData(@RequestParam(required = false) String inicio,
                                @RequestParam(required = false) String fim,
                                Model model) {
        try {
            LocalDateTime dataInicio = converterData(inicio, false);
            LocalDateTime dataFim = converterData(fim, true);
            if (dataFim == null && dataInicio != null) {
                dataFim = dataInicio.plusDays(1).minusSeconds(1);
            }

            List<Clientes> clientes = clienteService.buscarPorDataCriacao(dataInicio, dataFim);
            model.addAttribute("clientes", clientes);
            model.addAttribute("erro", clientes.isEmpty() ? "Nenhum cliente encontrado no intervalo de datas." : null);
        } catch (Exception e) {
            model.addAttribute("erro", "Erro inesperado ao buscar clientes.");
        }
        return "clientes/consulta";
    }

    @GetMapping("/relatorios-html")
    public String gerarRelatorioPorData(@RequestParam(required = false) String inicio,
                                        @RequestParam(required = false) String fim,
                                        Model model) {
        LocalDateTime dataInicio = converterData(inicio, false);
        LocalDateTime dataFim = converterData(fim, true);

        // 🔹 Se só a data inicial foi preenchida, assume que a final é hoje
        if (dataInicio != null && dataFim == null) {
            dataFim = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);
        }

        // 🔹 Se só a data final foi preenchida, assume que o início é o mais antigo possível
        if (dataInicio == null && dataFim != null) {
            dataInicio = LocalDateTime.MIN;
        }

        List<Clientes> clientes = clienteService.buscarPorDataCriacao(dataInicio, dataFim);
        long totalClientes = clienteService.contarClientesTotal();
        long clientesCriadosHoje = clienteService.contarClientesCriadosHoje();

        model.addAttribute("clientes", clientes);
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("clientesCriadosHoje", clientesCriadosHoje);

        return "clientes/relatorio";
    }


    @GetMapping("/relatorios-atualizados")
    public String gerarRelatorioAtualizados(@RequestParam(required = false) String inicio,
                                            @RequestParam(required = false) String fim,
                                            Model model) {
        LocalDateTime dataInicio = converterData(inicio, false);
        LocalDateTime dataFim = converterData(fim, true);

        List<Clientes> clientes;

        if (dataInicio == null && dataFim == null) {
            // 🔹 Se nenhuma data for preenchida, retorna SOMENTE registros que têm dataAtualizacao
            clientes = clienteService.buscarSomenteAtualizados();
        } else {
            // 🔹 Se só a data inicial foi preenchida, assume que a final é hoje
            if (dataInicio != null && dataFim == null) {
                dataFim = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);
            }
            // 🔹 Se só a data final foi preenchida, assume que o início é o mais antigo possível
            if (dataInicio == null && dataFim != null) {
                dataInicio = LocalDateTime.MIN;
            }
            clientes = clienteService.buscarPorAtualizacao(dataInicio, dataFim);
        }

        model.addAttribute("clientes", clientes);
        model.addAttribute("totalClientes", clienteService.contarClientesTotal());
        model.addAttribute("clientesCriadosHoje", clienteService.contarClientesCriadosHoje());
        model.addAttribute("clientesAtualizadosHoje", clienteService.contarClientesAtualizadosHoje());

        return "clientes/relatorioAtualizados";
    }


    @GetMapping("/buscar-nome-data")
    public String buscarPorNomeEData(@RequestParam String nome,
                                     @RequestParam(required = false) String inicio,
                                     @RequestParam(required = false) String fim,
                                     Model model) {
        try {
            LocalDateTime dataInicio = converterData(inicio, false);
            LocalDateTime dataFim = converterData(fim, true);
            if (dataFim == null && dataInicio != null) {
                dataFim = dataInicio.plusDays(1).minusSeconds(1);
            }

            List<Clientes> clientes = clienteService.buscarPorNomeEData(nome, dataInicio, dataFim);
            model.addAttribute("clientes", clientes);
            model.addAttribute("erro", clientes.isEmpty() ? "Nenhum cliente encontrado com esse nome e intervalo de datas." : null);
        } catch (Exception e) {
            model.addAttribute("erro", "Erro inesperado ao buscar clientes.");
        }
        return "clientes/consulta"; // 🔹 Garante que os resultados apareçam na página correta
    }

}
