package org.example.response;

import java.util.List;

public class RespostaBatch {
    private String mensagem;
    private List<String> erros;

    public RespostaBatch(String mensagem, List<String> erros) {
        this.mensagem = mensagem;
        this.erros = (erros == null || erros.isEmpty()) ? List.of("Sem erros") : erros; // 🔹 Substitui null por uma mensagem
    }

    public String getMensagem() {
        return mensagem;
    }

    public List<String> getErros() {
        return erros;
    }
}
