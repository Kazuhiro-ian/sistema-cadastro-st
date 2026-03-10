package supertrans.service;

import supertrans.model.Empresa;
import supertrans.model.EmpresaEstrangeira;
import supertrans.model.EmpresaFisica;
import supertrans.model.EmpresaJuridica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmpresaService {

    public void salvarEmpresa(Empresa empresa) {
        // Comando SQL para a tabela principal
        String sqlEmpresa = "INSERT INTO empresa (nome_fantasia, perfil, faturamento_direto, documento_anexo, aprovado, tipo_empresa) VALUES (?, ?, ?, ?, ?, ?)";

        // O try-with-resources já abre e fecha a conexão automaticamente de forma segura
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement pstmtEmpresa = conn.prepareStatement(sqlEmpresa, Statement.RETURN_GENERATED_KEYS)) {

            // 1. Preenchendo os dados da tabela mãe
            pstmtEmpresa.setString(1, empresa.getNomeFantasia());
            pstmtEmpresa.setString(2, empresa.getPerfil().name()); // Salva o nome do Enum (ex: FORNECEDOR)
            pstmtEmpresa.setBoolean(3, empresa.isFaturamentoDireto());
            pstmtEmpresa.setString(4, empresa.getDocumentoAnexo());
            pstmtEmpresa.setBoolean(5, empresa.isAprovado());

            // Descobrindo qual é o tipo da empresa para salvar no banco
            String tipo = "";
            if (empresa instanceof EmpresaJuridica) tipo = "PJ";
            else if (empresa instanceof EmpresaFisica) tipo = "PF";
            else if (empresa instanceof EmpresaEstrangeira) tipo = "EE";

            pstmtEmpresa.setString(6, tipo);

            // Executa o insert na tabela mãe
            pstmtEmpresa.executeUpdate();

            // 2. Pegando o ID que o PostgreSQL acabou de gerar
            ResultSet rs = pstmtEmpresa.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);

                // 3. Salvando os dados específicos na tabela filha correta
                if (empresa instanceof EmpresaJuridica) {
                    salvarPJ(conn, idGerado, (EmpresaJuridica) empresa);
                } else if (empresa instanceof EmpresaFisica) {
                    salvarPF(conn, idGerado, (EmpresaFisica) empresa);
                } else if (empresa instanceof EmpresaEstrangeira) {
                    salvarEE(conn, idGerado, (EmpresaEstrangeira) empresa);
                }
            }

            System.out.println("✅ supertrans.model.Empresa '" + empresa.getNomeFantasia() + "' salva com sucesso no Banco de Dados!");

        } catch (SQLException e) {
            System.out.println("❌ Erro ao salvar no banco: " + e.getMessage());
        }
    }

    // --- MÉTODOS AUXILIARES PARA AS TABELAS FILHAS ---

    private void salvarPJ(Connection conn, int idEmpresa, EmpresaJuridica pj) throws SQLException {
        String sql = "INSERT INTO empresa_juridica (id_empresa, razao_social, cnpj) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEmpresa);
            pstmt.setString(2, pj.getRazaoSocial());
            pstmt.setString(3, pj.getCnpj());
            pstmt.executeUpdate();
        }
    }

    private void salvarPF(Connection conn, int idEmpresa, EmpresaFisica pf) throws SQLException {
        String sql = "INSERT INTO empresa_fisica (id_empresa, nome, cpf) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEmpresa);
            pstmt.setString(2, pf.getNome());
            pstmt.setString(3, pf.getCpf());
            pstmt.executeUpdate();
        }
    }

    private void salvarEE(Connection conn, int idEmpresa, EmpresaEstrangeira ee) throws SQLException {
        String sql = "INSERT INTO empresa_estrangeira (id_empresa, razao_social, identificador_estrangeiro) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEmpresa);
            pstmt.setString(2, ee.getRazaoSocial());
            pstmt.setString(3, ee.getIdentificadorEstrangeiro());
            pstmt.executeUpdate();
        }
    }

    // Método para listar as empresas buscando direto do PostgreSQL
    public void listarEmpresas() {
        // Comando SQL para buscar os dados na tabela mãe
        String sql = "SELECT nome_fantasia, perfil, aprovado, tipo_empresa FROM empresa";

        System.out.println("\n--- Lista de Empresas Cadastradas no Banco ---");

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            boolean temDados = false;

            // O rs.next() vai passando linha por linha no resultado do banco
            while (rs.next()) {
                temDados = true; // Achamos pelo menos uma empresa

                // Pegando as colunas daquela linha
                String nomeFantasia = rs.getString("nome_fantasia");
                String perfil = rs.getString("perfil");
                boolean aprovado = rs.getBoolean("aprovado");
                String tipoSigla = rs.getString("tipo_empresa");

                // Traduzindo a sigla que salvamos no banco para o nome bonitinho
                String tipoNome = "";
                switch (tipoSigla) {
                    case "PJ": tipoNome = "supertrans.model.EmpresaJuridica"; break;
                    case "PF": tipoNome = "supertrans.model.EmpresaFisica"; break;
                    case "EE": tipoNome = "supertrans.model.EmpresaEstrangeira"; break;
                    default: tipoNome = "Desconhecido"; break;
                }

                // Imprimindo exatamente como você fazia antes
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
            System.out.println("❌ Erro ao buscar dados no banco: " + e.getMessage());
        }
    }
}