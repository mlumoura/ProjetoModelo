package org.example.controller.clientes;

import org.example.entity.Clientes;
import org.example.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/clientes-view") // 🔹 Define um prefixo para evitar conflitos com outras rotas da aplicação
public class ClienteViewController {

    @Autowired
    private ClienteService clienteService; // 🔹 Injeta o serviço que manipula os dados dos clientes

    // 🔹 Método auxiliar para converter uma String de data em LocalDateTime
    private LocalDateTime converterData(String data, boolean isFim) {
        if (data == null || data.isEmpty()) return null;
        try {
            // 🔹 Se for data final, ajusta para o último segundo do dia; senão, começa no início do dia
            return isFim ? LocalDate.parse(data).atTime(23, 59, 59) : LocalDate.parse(data).atStartOfDay();
        } catch (Exception e) {
            return null; // 🔹 Evita erros de conversão
        }
    }

    // 🔹 Rota para listar todos os clientes na interface HTML
    @GetMapping("/listar-html")
    public String listarClientes(Model model, @RequestParam(required = false) String tema) {
        String temaFinal = tema != null ? tema : "claro"; // 🔹 Define o tema padrão caso não seja passado
        model.addAttribute("tema", temaFinal);

        List<Clientes> clientes = clienteService.listarTodos(); // 🔹 Busca todos os clientes no banco
        model.addAttribute("clientes", clientes);

        return "clientes/listarClientes"; // 🔹 Retorna a página HTML correta
    }

    @GetMapping("/consulta")
    public String mostrarConsulta(Model model, @RequestParam(required = false) String tema) {
        String temaFinal = tema != null ? tema : "claro";
        model.addAttribute("tema", temaFinal);
        return "clientes/consulta";
    }

    @GetMapping("/buscar-id")
    public String buscarPorId(@RequestParam Long id, Model model, @RequestParam(required = false) String tema) {
        try {
            Clientes cliente = clienteService.buscarPorId(id);
            model.addAttribute("clientes", cliente != null ? List.of(cliente) : List.of());
            model.addAttribute("erro", cliente == null ? "Cliente não encontrado!" : null);
        } catch (Exception e) {
            model.addAttribute("erro", "Ocorreu um erro inesperado.");
        }
        model.addAttribute("tema", tema != null ? tema : "claro");
        return "clientes/consulta";
    }

    @GetMapping("/buscar-nome")
    public String buscarPorNome(@RequestParam String nome, Model model, @RequestParam(required = false) String tema) {
        try {
            List<Clientes> clientes = clienteService.buscarPorNome(nome);
            model.addAttribute("clientes", clientes);
            model.addAttribute("erro", clientes.isEmpty() ? "Nenhum cliente encontrado com esse nome." : null);
        } catch (Exception e) {
            model.addAttribute("erro", "Erro inesperado ao buscar clientes.");
        }
        model.addAttribute("tema", tema != null ? tema : "claro");
        return "clientes/consulta";
    }

    // 🔹 Busca clientes pelo nome e intervalo de datas
    @GetMapping("/buscar-nome-data")
    public String buscarPorNomeEData(@RequestParam String nome,
                                     @RequestParam(required = false) String inicio,
                                     @RequestParam(required = false) String fim,
                                     Model model, @RequestParam(required = false) String tema) {
        try {
            LocalDateTime dataInicio = converterData(inicio, false);
            LocalDateTime dataFim = converterData(fim, true);

            // 🔹 Se nenhuma data for informada, assume um intervalo completo
            if (dataInicio == null && dataFim == null) {
                dataInicio = LocalDateTime.MIN;
                dataFim = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);
            }
            if (dataInicio != null && dataFim == null) {
                dataFim = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);
            }
            if (dataInicio == null && dataFim != null) {
                dataInicio = LocalDateTime.MIN;
            }

            List<Clientes> clientes = clienteService.buscarPorNomeEData(nome, dataInicio, dataFim);
            model.addAttribute("clientes", clientes);
            model.addAttribute("erro", clientes.isEmpty() ? "Nenhum cliente encontrado com esse nome e intervalo de datas." : null);
        } catch (Exception e) {
            model.addAttribute("erro", "Erro inesperado ao buscar clientes.");
        }
        model.addAttribute("tema", tema != null ? tema : "claro");
        return "clientes/consulta";
    }

    @GetMapping("/buscar-data-html")
    public String buscarPorData(@RequestParam(required = false) String inicio,
                                @RequestParam(required = false) String fim,
                                Model model) {
        try {
            LocalDateTime dataInicio = converterData(inicio, false);
            LocalDateTime dataFim = converterData(fim, true);
            if (dataFim == null && dataInicio == null) {
                dataFim = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);
                dataInicio = LocalDateTime.MIN;
            }

            if (dataFim == null && dataInicio != null) {
                dataFim = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);
            }

            if (dataFim != null && dataInicio == null) {
                dataInicio = LocalDateTime.MIN;
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

        if (dataFim == null && dataInicio == null) {
            dataFim = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);
            dataInicio = LocalDateTime.MIN;
        }

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


    // 🔹 Gera relatório de clientes atualizados recentemente
    @GetMapping("/relatorios-atualizados")
    public String gerarRelatorioAtualizados(@RequestParam(required = false) String inicio,
                                            @RequestParam(required = false) String fim,
                                            Model model, @RequestParam(required = false) String tema) {
        LocalDateTime dataInicio = converterData(inicio, false);
        LocalDateTime dataFim = converterData(fim, true);

        List<Clientes> clientes;
        if (dataInicio == null && dataFim == null) {
            clientes = clienteService.buscarSomenteAtualizados();
        } else {
            if (dataInicio != null && dataFim == null) {
                dataFim = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);
            }
            if (dataInicio == null && dataFim != null) {
                dataInicio = LocalDateTime.MIN;
            }
            clientes = clienteService.buscarPorAtualizacao(dataInicio, dataFim);
        }

        model.addAttribute("clientes", clientes);
        model.addAttribute("totalClientes", clienteService.contarClientesTotal());
        model.addAttribute("clientesCriadosHoje", clienteService.contarClientesCriadosHoje());
        model.addAttribute("clientesAtualizadosHoje", clienteService.contarClientesAtualizadosHoje());
        model.addAttribute("tema", tema != null ? tema : "claro");

        return "clientes/relatorioAtualizados";
    }
}
