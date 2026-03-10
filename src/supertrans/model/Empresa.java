package supertrans.model;

public abstract class Empresa {
    /* ====================================================================
     * ATRIBUTOS PRIVADOS A CLASSE
     * ==================================================================== */

    private String nomeFantasia;
    private PerfilEnum perfil;
    private boolean faturamentoDireto;
    private String documentoAnexo;
    private boolean aprovado;

    public Empresa(String nomeFantasia, PerfilEnum perfil, boolean faturamentoDireto, String documentoAnexo, boolean aprovado) {
        this.nomeFantasia = nomeFantasia;
        this.perfil = perfil;
        this.faturamentoDireto = faturamentoDireto;
        this.documentoAnexo = documentoAnexo;
        this.aprovado = aprovado;
    }

    /* ====================================================================
     * GETTERS E SETTERS (Encapsulamento)
     * ==================================================================== */

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