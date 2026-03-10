package supertrans;

import supertrans.model.EmpresaEstrangeira;
import supertrans.model.EmpresaFisica;
import supertrans.model.EmpresaJuridica;
import supertrans.model.PerfilEnum;
import supertrans.service.EmpresaService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        EmpresaService empresaService = new EmpresaService();

        /* ====================================================================
         * 1. CONFIGURAÇÃO INICIAL E PERFIL DE ACESSO
         * ==================================================================== */
        System.out.println("--- BEM-VINDO AO PORTAL SUPER TRANS ---");
        System.out.println("Qual seu perfil de acesso atual?");
        System.out.println("1 - Usuário interno (Aprovação automática)");
        System.out.println("2 - Usuário externo (Requer aprovação)");
        System.out.print("Digite sua opção: ");

        // Default seguro (Usuário externo)
        int opcaoPerfil = 2;
        try {
            opcaoPerfil = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("[AVISO] Entrada inválida! Assumindo usuário externo por padrão.");
        }

        boolean statusAprovacao = (opcaoPerfil == 1);
        int opcaoMenu = -1;

        /* ====================================================================
         * 2. LOOP PRINCIPAL DO SISTEMA (MENU)
         * ==================================================================== */
        do {
            System.out.println("\n=== MENU DE CADASTRO ===");
            System.out.println("1 - Cadastrar Empresa Jurídica");
            System.out.println("2 - Cadastrar Empresa Física");
            System.out.println("3 - Cadastrar Empresa Estrangeira");
            System.out.println("4 - Listar todas as empresas cadastradas");
            System.out.println("0 - Sair do sistema");
            System.out.print("Escolha uma opção: ");

            try {
                opcaoMenu = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("[ERRO] Entrada inválida! Por favor, digite um número correspondente ao menu.");
                continue; // Reinicia o loop imediatamente
            }

            // Verifica se a opção escolhida refere-se a um fluxo de cadastro (1 a 3)
            if (opcaoMenu >= 1 && opcaoMenu <= 3) {

                /* ============================================================
                 * 3. CAPTURA DE DADOS COMUNS A TODAS AS EMPRESAS
                 * ============================================================ */
                System.out.println("\n--- Preenchendo Dados Comuns ---");

                System.out.print("Nome Fantasia: ");
                String nomeFantasia = sc.nextLine();

                System.out.println("Perfil da Empresa:");
                System.out.println("1 - Fornecedor");
                System.out.println("2 - Revendedor");
                System.out.println("3 - Cliente Final");
                System.out.print("Escolha uma opção de perfil: ");

                PerfilEnum perfilSelecionado = PerfilEnum.CLIENTE_FINAL; // Enumeração padrão
                try {
                    int opPerfil = Integer.parseInt(sc.nextLine());
                    switch (opPerfil) {
                        case 1: perfilSelecionado = PerfilEnum.FORNECEDOR; break;
                        case 2: perfilSelecionado = PerfilEnum.REVENDEDOR; break;
                        case 3: perfilSelecionado = PerfilEnum.CLIENTE_FINAL; break;
                        default: System.out.println("[AVISO] Opção inválida! Assumindo Cliente Final."); break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[AVISO] Entrada inválida! Assumindo Cliente Final.");
                }

                System.out.print("Possui faturamento direto? (S/N): ");
                boolean faturamento = sc.nextLine().equalsIgnoreCase("S");

                System.out.print("Documento Anexo (ex: arquivo.pdf): ");
                String documentoAnexo = sc.nextLine();

                /* ============================================================
                 * 4. CAPTURA DE DADOS ESPECÍFICOS E INSTANCIAÇÃO
                 * ============================================================ */
                switch (opcaoMenu) {
                    case 1:
                        // Fluxo: Pessoa Jurídica
                        System.out.println("\n--- Dados Específicos: Pessoa Jurídica ---");
                        System.out.print("Razão Social: ");
                        String razaoSocialPJ = sc.nextLine();

                        System.out.print("CNPJ (exatamente 14 números): ");
                        String cnpjPJ = sc.nextLine();

                        try {
                            EmpresaJuridica novaPJ = new EmpresaJuridica(nomeFantasia, perfilSelecionado, faturamento, documentoAnexo, statusAprovacao, razaoSocialPJ, cnpjPJ);
                            empresaService.salvarEmpresa(novaPJ);
                        } catch (IllegalArgumentException e) {
                            System.out.println("[ERRO DE VALIDAÇÃO] " + e.getMessage());
                        }
                        break;

                    case 2:
                        // Fluxo: Pessoa Física
                        System.out.println("\n--- Dados Específicos: Pessoa Física ---");
                        System.out.print("Nome: ");
                        String nomePF = sc.nextLine();

                        System.out.print("CPF (Exatamente 11 números): ");
                        String cpfPF = sc.nextLine();

                        try {
                            EmpresaFisica novaPF = new EmpresaFisica(nomeFantasia, perfilSelecionado, faturamento, documentoAnexo, statusAprovacao, nomePF, cpfPF);
                            empresaService.salvarEmpresa(novaPF);
                        } catch (IllegalArgumentException e) {
                            System.out.println("[ERRO DE VALIDAÇÃO] " + e.getMessage());
                        }
                        break;

                    case 3:
                        // Fluxo: Empresa Estrangeira
                        System.out.println("\n--- Dados Específicos: Empresa Estrangeira ---");
                        System.out.print("Razão Social: ");
                        String razaoSocialEE = sc.nextLine();

                        System.out.print("Identificador Estrangeiro: ");
                        String identificadorEE = sc.nextLine();

                        EmpresaEstrangeira novaEE = new EmpresaEstrangeira(nomeFantasia, perfilSelecionado, faturamento, documentoAnexo, statusAprovacao, razaoSocialEE, identificadorEE);
                        empresaService.salvarEmpresa(novaEE);
                        break;
                }
            } else {
                /* ============================================================
                 * 5. FLUXOS ALTERNATIVOS (LISTAGEM E SAÍDA)
                 * ============================================================ */
                switch (opcaoMenu) {
                    case 4:
                        empresaService.listarEmpresas();
                        break;
                    case 0:
                        System.out.println("Encerrando o sistema Super Trans. Até logo!");
                        break;
                    default:
                        System.out.println("[ERRO] Opção inválida do menu principal!");
                        break;
                }
            }
        } while (opcaoMenu != 0);

        sc.close();
    }
}