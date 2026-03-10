package supertrans.model;

public class EmpresaEstrangeira extends Empresa{
    private String razaoSocial;
    private String identificadorEstrangeiro;

    //Construtor
    public EmpresaEstrangeira (String nomeFantasia, PerfilEnum perfil, boolean faturamentoDireto, String documentoAnexo, boolean aprovado, String razaoSocial, String identificadorEstrangeiro) {
        super(nomeFantasia, perfil, faturamentoDireto, documentoAnexo, aprovado);

        this.razaoSocial = razaoSocial;
        this.identificadorEstrangeiro = identificadorEstrangeiro;
    }

    //Getters e setters


    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getIdentificadorEstrangeiro() {
        return identificadorEstrangeiro;
    }

    public void setIdentificadorEstrangeiro(String identificadorEstrangeiro) {
        this.identificadorEstrangeiro = identificadorEstrangeiro;
    }
}