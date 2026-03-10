package supertrans.model;

public abstract class Empresa {
    private String nomeFantasia;
    private PerfilEnum perfil;
    private boolean faturamentoDireto;
    private String documentoAnexo;
    private boolean aprovado;

    //construtor
    public Empresa(String nomeFantasia, PerfilEnum perfil, boolean faturamentoDireto, String documentoAnexo, boolean aprovado) {
        this.nomeFantasia = nomeFantasia;
        this.perfil = perfil;
        this.faturamentoDireto = faturamentoDireto;
        this.documentoAnexo = documentoAnexo;
        this.aprovado = aprovado;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public PerfilEnum getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilEnum perfil) {
        this.perfil = perfil;
    }

    public boolean isFaturamentoDireto() {
        return faturamentoDireto;
    }

    public void setFaturamentoDireto(boolean faturamentoDireto) {
        this.faturamentoDireto = faturamentoDireto;
    }

    public String getDocumentoAnexo() {
        return documentoAnexo;
    }

    public void setDocumentoAnexo(String documentoAnexo) {
        this.documentoAnexo = documentoAnexo;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }
}