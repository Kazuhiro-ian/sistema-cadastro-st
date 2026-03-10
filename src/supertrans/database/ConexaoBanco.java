package supertrans.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitária responsável por gerenciar a comunicação com o banco de dados PostgreSQL.
 * Fornece instâncias ativas de conexão (Connection) para a camada de serviço (Service)
 * poder realizar as operações de leitura e escrita (CRUD).
 */
public class ConexaoBanco {

    /**
     * Estabelece e retorna uma nova conexão com o banco de dados relacional.
     * * NOTA: Em um ambiente corporativo avançado, as credenciais abaixo não ficam fixas
     * no código, sendo extraídas de variáveis de ambiente (.env) ou arquivos de
     * configuração para maior segurança.
     *
     * @return Objeto java.sql.Connection ativo, ou null caso a conexão falhe.
     */
    public static Connection conectar() {

        // URL de conexão JDBC: protocolo:banco://servidor:porta/nome_do_banco
        String url = "jdbc:postgresql://localhost:5432/super_trans_db";

        // Credenciais de acesso ao servidor local
        String usuario = "postgres";
        String senha = "SUA_SENHA_AQUI"; // Senha omitida para segurança no versionamento

        try {
            // Tenta estabelecer a ponte de comunicação usando o Driver do PostgreSQL
            Connection conexao = DriverManager.getConnection(url, usuario, senha);
            // System.out.println("[LOG] Conexão com o banco estabelecida."); // Opcional
            return conexao;

        } catch (SQLException e) {
            // Captura e detalha o erro caso o servidor esteja offline ou as credenciais inválidas
            System.err.println("[ERRO CRÍTICO] Falha ao conectar com o banco de dados.");
            System.err.println("Detalhes técnicos: " + e.getMessage());
            return null;
        }
    }
}