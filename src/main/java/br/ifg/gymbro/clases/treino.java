package clases;

import java.text.SimpleDateFormat;

public class treino{
    private float pesoTreino;
    private int repeticoes;
    private Data data;
    private int series;
    private boolean aqueciemnto;
    private String anotacao;

    public treino(float pesoTreino, int repeticoes, Data data, int series, boolean aqueciemnto, String anotacao){
        this.pesoTreino = pesoTreino;
        this.repeticoes = repeticoes;
        this.data = data;
        this.series = series;
        this.aqueciemnto = aqueciemnto;
        this.anotacao = anotacao;
    }

    public float getPesoTreino(){
        return pesoTreino;
    }

    public void setPesoTreino(float pesoTreino){
        this.pesoTreino = pesoTreino;
    }

    public int getRepeticao(){
        return repeticoes;
    }

    public void setRepeticao(int repeticoes){
        this.repeticoes;
    }

    public Data getData(){
        return data;
    }

    public void setData(Data dara){
        this.data = data;
    }

    public int getSeries(){
        return series;
    }

    public void setSeries(int series){
        this.series = series;
    }

    public boolean getAquecimento(){
        return aqueciemnto;
    }

    public void setAquecimento(boolean aqueciemnto){
        this.aqueciemnto = aqueciemnto;
    }

    public String getAnotacao(){
        return anotacao;
    }

    public void setAnotacao(String anotacao){
        this.anotacao = anotacao;
    }

}