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

        // Instanciamos o Service que agora vai gerenciar a lista e as regras

        EmpresaService empresaService = new EmpresaService();

        System.out.println("--- BEM-VINDO AO PORTAL SUPER TRANS ---");
        System.out.println("Qual seu perfil de acesso atual?");
        System.out.println("1 - Usuário interno (Aprovação automática)");
        System.out.println("2 - Usuário externo (Requer aprovação)");
        System.out.print("Digite sua opção: ");
        int opcaoPerfil = 2;
        try {
            opcaoPerfil = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida! Assumindo usuário externo por padrão");
        }

        boolean statusAprovacao = (opcaoPerfil == 1);
        int opcaoMenu = -1;

        do {
            System.out.println("\n=== MENU DE CADASTRO ===");
            System.out.println("1 - Cadastrar supertrans.model.Empresa Jurídica");
            System.out.println("2 - Cadastrar supertrans.model.Empresa Física");
            System.out.println("3 - Cadastrar supertrans.model.Empresa Estrangeira");
            System.out.println("4 - Listar todas as empresas cadastradas");
            System.out.println("0 - Sair do sistema");
            System.out.print("Escolha uma opção: ");
            try {
                opcaoMenu = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Por favor, digite um número correspondente!");
                continue;
            }

            // Se a opção for de cadastro (1, 2 ou 3), pedimos os dados comuns primeiro
            if (opcaoMenu >= 1 && opcaoMenu <= 3) {
                System.out.println("\n--- Preenchendo Dados Comuns ---");

                System.out.print("Nome Fantasia: ");
                String nomeFantasia = sc.nextLine();

                System.out.println("Perfil da supertrans.model.Empresa:");
                System.out.println("1 - Fornecedor");
                System.out.println("2 - Revendedor");
                System.out.println("3 - Cliente Final");
                System.out.print("Escolha uma opção de perfil: ");

                PerfilEnum perfilSelecionado = PerfilEnum.CLIENTE_FINAL;

                try {
                    int opPerfil = Integer.parseInt(sc.nextLine());
                    switch (opPerfil) {
                        case 1: perfilSelecionado = PerfilEnum.FORNECEDOR; break;
                        case 2: perfilSelecionado = PerfilEnum.REVENDEDOR; break;
                        case 3: perfilSelecionado = PerfilEnum.CLIENTE_FINAL; break;
                        default: System.out.println("Opção inválida! Assumindo Cliente Final."); break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida! Assumindo Cliente Final.");
                }

                // ... (leitura do faturamentoDireto) ...

                System.out.print("Possui faturamento direto? (S/N): ");
                boolean faturamento = sc.nextLine().equalsIgnoreCase("S");

                System.out.print("Documento Anexo (ex: arquivo.pdf): ");
                String documentoAnexo = sc.nextLine();

                // O switch agora foca apenas nos dados específicos de cada tipo
                switch (opcaoMenu) {
                    case 1:
                        System.out.println("\n--- Dados Específicos: Pessoa Jurídica ---");
                        System.out.print("Razão Social: ");
                        String razaoSocialPJ = sc.nextLine();

                        System.out.print("CNPJ (exatamente 14 números): ");
                        String cnpjPJ = sc.nextLine();

                        try {
                            EmpresaJuridica novaPJ = new EmpresaJuridica(nomeFantasia, perfilSelecionado, faturamento, documentoAnexo, statusAprovacao, razaoSocialPJ, cnpjPJ);
                            // Chamamos o service para salvar
                            empresaService.salvarEmpresa(novaPJ);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Erro ao cadastrar: " + e.getMessage());
                        }
                        break;

                    case 2:
                        System.out.println("\n--- Dados Específicos: Pessoa Física ---");
                        System.out.print("Nome: ");
                        String nomePF = sc.nextLine();

                        System.out.print("CPF (Exatamente 11 números): ");
                        String cpfPF = sc.nextLine();

                        try {
                            EmpresaFisica novaPF = new EmpresaFisica(nomeFantasia, perfilSelecionado, faturamento, documentoAnexo, statusAprovacao, nomePF, cpfPF);
                            // Chamamos o service para salvar
                            empresaService.salvarEmpresa(novaPF);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Erro ao cadastrar: " + e.getMessage());
                        }
                        break;

                    case 3:
                        System.out.println("\n--- Dados Específicos: supertrans.model.Empresa Estrangeira ---");
                        System.out.print("Razão Social: ");
                        String razaoSocialEE = sc.nextLine();

                        System.out.print("Identificador Estrangeiro: ");
                        String identificadorEE = sc.nextLine();

                        EmpresaEstrangeira novaEE = new EmpresaEstrangeira(nomeFantasia, perfilSelecionado, faturamento, documentoAnexo, statusAprovacao, razaoSocialEE, identificadorEE);
                        // Chamamos o service para salvar
                        empresaService.salvarEmpresa(novaEE);
                        break;
                }
            } else {
                // Tratando as opções que não são de cadastro (Listar e Sair)
                switch (opcaoMenu) {
                    case 4:
                        // O service cuida de toda a listagem agora
                        empresaService.listarEmpresas();
                        break;
                    case 0:
                        System.out.println("Encerrando o sistema Super Trans...");
                        break;
                    default:
                        System.out.println("ERRO: Opção inválida!");
                        break;
                }
            }
        } while (opcaoMenu != 0);

        sc.close();
    }
}