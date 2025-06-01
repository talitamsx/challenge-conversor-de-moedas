package br.com.challenge.conversormoedas.modelo;

//Classe que representa a conversão de moeda, armazenando os detalhes da operação.
public class Conversao {
    private double valorUsuario;
    private String moedaOrigem;
    private double valorConvertido;
    private String moedaDestino;

    public double getValorUsuario() {
        return valorUsuario;
    }

    public String getMoedaOrigem() {
        return moedaOrigem;
    }

    public double getValorConvertido() {
        return valorConvertido;
    }

    public String getMoedaDestino() {
        return moedaDestino;
    }

    //Construtor que cria uma nova conversão com todos os parametros
    public Conversao(double valorUsuario, String moedaUsuario, double valorConvertido, String novaMoeda) {
        this.valorUsuario = valorUsuario;
        this.moedaOrigem = moedaUsuario;
        this.valorConvertido = valorConvertido;
        this.moedaDestino = novaMoeda;
    }
}

