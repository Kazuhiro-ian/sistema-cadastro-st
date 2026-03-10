package supertrans.service;

import supertrans.database.ConexaoBanco;
import supertrans.model.Empresa;
import supertrans.model.EmpresaEstrangeira;
import supertrans.model.EmpresaFisica;
import supertrans.model.EmpresaJuridica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe de serviço responsável pelas operações de persistência e regras de negócio
 * relacionadas às entidades corporativas.
 * Atua como a camada intermediária entre a interface do usuário (Main) e a
 * camada de acesso a dados (PostgreSQL).
 */
public class EmpresaService {

    /**
     * Gerencia o salvamento completo de uma empresa no banco de dados.
     * Utiliza a estratégia de "Tabelas Separadas" (Joined Strategy), realizando
     * o INSERT primeiro na tabela principal (empresa) e, utilizando o ID gerado,
     * realiza um segundo INSERT na tabela específica da entidade filha.
     *
     * @param empresa Objeto polimórfico representando a empresa (Física, Jurídica ou Estrangeira).
     */
    public void salvarEmpresa(Empresa empresa) {
        // Comando SQL para a tabela principal
        String sqlEmpresa = "INSERT INTO empresa (nome_fantasia, perfil, faturamento_direto, documento_anexo, aprovado, tipo_empresa) VALUES (?, ?, ?, ?, ?, ?)";

        // try-with-resources: Garante que a conexão e o PreparedStatement sejam fechados automaticamente
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement pstmtEmpresa = conn.prepareStatement(sqlEmpresa, Statement.RETURN_GENERATED_KEYS)) {

            /* ============================================================
             * ETAPA 1: SALVANDO OS DADOS COMUNS NA TABELA MÃE
             * ============================================================ */
            pstmtEmpresa.setString(1, empresa.getNomeFantasia());
            pstmtEmpresa.setString(2, empresa.getPerfil().name()); // Salva a representação String do Enum
            pstmtEmpresa.setBoolean(3, empresa.isFaturamentoDireto());
            pstmtEmpresa.setString(4, empresa.getDocumentoAnexo());
            pstmtEmpresa.setBoolean(5, empresa.isAprovado());

            // Identifica dinamicamente o tipo da classe para gravar a sigla no banco
            String tipo = "";
            if (empresa instanceof EmpresaJuridica) tipo = "PJ";
            else if (empresa instanceof EmpresaFisica) tipo = "PF";
            else if (empresa instanceof EmpresaEstrangeira) tipo = "EE";

            pstmtEmpresa.setString(6, tipo);

            // Executa a inserção na tabela principal
            pstmtEmpresa.executeUpdate();

            /* ============================================================
             * ETAPA 2: RECUPERANDO O ID E SALVANDO NA TABELA FILHA
             * ============================================================ */
            ResultSet rs = pstmtEmpresa.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1); // Captura o ID (Primary Key) gerado pelo PostgreSQL

                // Direciona para o método auxiliar correspondente ao tipo de empresa
                if (empresa instanceof EmpresaJuridica) {
                    salvarPJ(conn, idGerado, (EmpresaJuridica) empresa);
                } else if (empresa instanceof EmpresaFisica) {
                    salvarPF(conn, idGerado, (EmpresaFisica) empresa);
                } else if (empresa instanceof EmpresaEstrangeira) {
                    salvarEE(conn, idGerado, (EmpresaEstrangeira) empresa);
                }
            }

            // Feedback visual limpo e profissional
            System.out.println("Empresa '" + empresa.getNomeFantasia() + "' salva com sucesso no Banco de Dados!");

        } catch (SQLException e) {
            System.out.println("Erro ao salvar no banco de dados: " + e.getMessage());
        }
    }

    /* ====================================================================
     * MÉTODOS AUXILIARES DE PERSISTÊNCIA (Privados)
     * ==================================================================== */

    /**
     * Salva os dados específicos de uma Pessoa Jurídica usando o ID da tabela mãe.
     */
    private void salvarPJ(Connection conn, int idEmpresa, EmpresaJuridica pj) throws SQLException {
        String sql = "INSERT INTO empresa_juridica (id_empresa, razao_social, cnpj) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEmpresa);
            pstmt.setString(2, pj.getRazaoSocial());
            pstmt.setString(3, pj.getCnpj());
            pstmt.executeUpdate();
        }
    }

    /**
     * Salva os dados específicos de uma Pessoa Física usando o ID da tabela mãe.
     */
    private void salvarPF(Connection conn, int idEmpresa, EmpresaFisica pf) throws SQLException {
        String sql = "INSERT INTO empresa_fisica (id_empresa, nome, cpf) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEmpresa);
            pstmt.setString(2, pf.getNome());
            pstmt.setString(3, pf.getCpf());
            pstmt.executeUpdate();
        }
    }

    /**
     * Salva os dados específicos de uma Empresa Estrangeira usando o ID da tabela mãe.
     */
    private void salvarEE(Connection conn, int idEmpresa, EmpresaEstrangeira ee) throws SQLException {
        String sql = "INSERT INTO empresa_estrangeira (id_empresa, razao_social, identificador_estrangeiro) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEmpresa);
            pstmt.setString(2, ee.getRazaoSocial());
            pstmt.setString(3, ee.getIdentificadorEstrangeiro());
            pstmt.executeUpdate();
        }
    }

    /* ====================================================================
     * MÉTODOS DE LEITURA (Listagem)
     * ==================================================================== */

    /**
     * Consulta o banco de dados e exibe no console uma lista formatada
     * de todas as empresas cadastradas na tabela principal.
     */
    public void listarEmpresas() {
        String sql = "SELECT nome_fantasia, perfil, aprovado, tipo_empresa FROM empresa";

        System.out.println("\n--- Lista de Empresas Cadastradas no Banco ---");

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            boolean temDados = false;

            // Percorre todas as linhas retornadas pelo banco de dados
            while (rs.next()) {
                temDados = true;

                // Extração dos dados da linha atual
                String nomeFantasia = rs.getString("nome_fantasia");
                String perfil = rs.getString("perfil");
                boolean aprovado = rs.getBoolean("aprovado");
                String tipoSigla = rs.getString("tipo_empresa");

                // Tradução da sigla salva no banco para um formato amigável ao usuário
                String tipoNome = "";
                switch (tipoSigla) {
                    case "PJ": tipoNome = "Empresa Jurídica"; break;
                    case "PF": tipoNome = "Empresa Física"; break;
                    case "EE": tipoNome = "Empresa Estrangeira"; break;
                    default: tipoNome = "Desconhecido"; break;
                }

                // Impressão formatada
                System.out.println("Tipo: " + tipoNome);
                System.out.println("Nome Fantasia: " + nomeFantasia);
                System.out.println("Perfil: " + perfil);
                String statusTela = aprovado ? "Aprovada" : "Pendente (Requer aprovação)";
                System.out.println("Status: " + statusTela);
                System.out.println("----------------------------------------------");
            }

            if (!temDados) {
                System.out.println("Nenhuma empresa cadastrada no momento.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar dados no banco: " + e.getMessage());
        }
    }
}