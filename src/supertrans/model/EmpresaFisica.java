package supertrans.model;

public class EmpresaFisica extends Empresa{
    private String nome;
    private String cpf;

    //Construtor
    public EmpresaFisica (String nomeFantasia, PerfilEnum perfil, boolean faturamentoDireto, String documentoAnexo, boolean aprovado, String nome, String cpf) {
        super(nomeFantasia, perfil, faturamentoDireto, documentoAnexo, aprovado);
        this.nome = nome;
        setCpf(cpf);
    }

    //Setter e Getters

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
        if(cpf != null && cpf.length() == 11){
            this.cpf = cpf;
        } else {
            throw new IllegalArgumentException("CPF inválido");
        }
    }
}