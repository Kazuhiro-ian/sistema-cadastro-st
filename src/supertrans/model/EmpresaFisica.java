package supertrans.model;

public class EmpresaFisica extends Empresa {

    // Atributos específicos da Pessoa Física
    private String nome;
    private String cpf;

    public EmpresaFisica(String nomeFantasia, PerfilEnum perfil, boolean faturamentoDireto, String documentoAnexo, boolean aprovado, String nome, String cpf) {

        // Repassa os atributos base para a classe abstrata Empresa
        super(nomeFantasia, perfil, faturamentoDireto, documentoAnexo, aprovado);

        this.nome = nome;

        // Chama o setter para garantir que a validação de 11 dígitos ocorra já na criação do objeto
        setCpf(cpf);
    }

    /* ====================================================================
     * GETTERS E SETTERS COM REGRAS DE NEGÓCIO
     * ==================================================================== */

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        // Regra de negócio corporativa: O CPF não pode ser nulo e deve ter exatamente 11 dígitos.
        if (cpf != null && cpf.length() == 11) {
            this.cpf = cpf;
        } else {
            // Lança uma exceção clara explicando o motivo da recusa
            throw new IllegalArgumentException("CPF inválido! O documento deve conter exatamente 11 números.");
        }
    }
}