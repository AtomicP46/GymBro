package clases;

public class exercicios(){
    private String nomeExer;
    private String regiao;
    private String tipo;
    private boolean unilateral;

    public exercicios(String nomeExer, String regiao, String tipo, boolean unilateral){
        this.nomeExer = nomeExer;
        this.regiao = regiao;
        this.tipo = tipo;
        this.unilateral = unilateral;
    }

    public String GetNomeExer(){
        return nomeExer;
    }

     public void setNomeExer(String nomeExer){
        this.nomeExer = nomeExer;
    }

    public String getRegiao(){
        return regiao;
    }

     public void setRegiao(String regiao){
        this.pesoTreino = regiao;
    }
}