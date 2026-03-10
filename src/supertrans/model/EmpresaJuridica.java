package supertrans.model;

public class EmpresaJuridica extends Empresa {

    // Atributos corporativos específicos
    private String razaoSocial;
    private String cnpj;

    public EmpresaJuridica(String nomeFantasia, PerfilEnum perfil, boolean faturamentoDireto, String documentoAnexo, boolean aprovado, String razaoSocial, String cnpj) {

        // Chamada ao construtor da superclasse
        super(nomeFantasia, perfil, faturamentoDireto, documentoAnexo, aprovado);

        this.razaoSocial = razaoSocial;

        // Chama o setter garantindo o Fail-Fast (falha rápida) se o dado for inválido
        setCnpj(cnpj);
    }

    /* ====================================================================
     * GETTERS E SETTERS (Com regras de negócio)
     * ==================================================================== */

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        // Regra de validação: O CNPJ brasileiro padrão possui exatamente 14 dígitos.
        if (cnpj != null && cnpj.length() == 14) {
            this.cnpj = cnpj;
        } else {
            // Exceção amigável que será capturada e exibida na Main
            throw new IllegalArgumentException("CNPJ inválido! O documento deve conter exatamente 14 números.");
        }
    }
}