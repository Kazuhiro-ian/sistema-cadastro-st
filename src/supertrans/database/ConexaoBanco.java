package supertrans.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {

    // Método que estabelece a conexão
    public static Connection conectar() {
        // O endereço do seu banco de dados local
        String url = "jdbc:postgresql://localhost:5432/super_trans_db";
        String usuario = "postgres"; // Usuário padrão do PostgreSQL
        String senha = "Senha do banco"; // Substitua pela senha que você usa no pgAdmin

        try {
            Connection conexao = DriverManager.getConnection(url, usuario, senha);
            System.out.println("✅ Conexão com o banco de dados realizada com sucesso!");
            return conexao;
        } catch (SQLException e) {
            System.out.println("❌ Erro ao conectar com o banco de dados: " + e.getMessage());
            return null;
        }
    }

    // Um método main temporário só para testarmos se a ponte está funcionando!
    public static void main(String[] args) {
        conectar();
    }
}