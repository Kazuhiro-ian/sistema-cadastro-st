package supertrans.model;

public class EmpresaJuridica extends Empresa{
    private String razaoSocial;
    private String cnpj;

    //construtor
    public EmpresaJuridica (String nomeFantasia, PerfilEnum perfil, boolean faturamentoDireto, String documentoAnexo, boolean aprovado, String razaoSocial, String cnpj) {
        super(nomeFantasia, perfil, faturamentoDireto, documentoAnexo, aprovado);
        this.razaoSocial = razaoSocial;
        setCnpj(cnpj);
    }
    //GETTERS E SETTERS
    //razão social-----------------------------------------------------------------
    public String getRazaoSocial() {
        return razaoSocial;
    }
    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }
    //cnpj--------------------------------------------------------------------------
    public String getCnpj() {
        return cnpj;
    }
    //validação do cnpj
    public void setCnpj(String cnpj) {
        if (cnpj != null && cnpj.length() == 14) {
            this.cnpj = cnpj;
        } else {
            throw new IllegalArgumentException("CNPJ inválido");
        }
    }
}