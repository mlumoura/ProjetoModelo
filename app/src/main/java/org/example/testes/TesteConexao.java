package org.exemplo.modelo.testes;

import java.sql.Connection;
import java.sql.DriverManager;

public class TesteConexao {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/ProjetoModelo?useSSL=false&serverTimezone=UTC";
        String usuario = "root";
        String senha = "sakamoto";

        try (Connection conn = DriverManager.getConnection(url, usuario, senha)) {
            System.out.println("Conexão bem-sucedida!");
        } catch (Exception e) {
            System.out.println("Erro na conexão: " + e.getMessage());
        }
    }
}
