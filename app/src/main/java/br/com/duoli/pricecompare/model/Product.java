package br.com.duoli.pricecompare.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Product implements Serializable {

    private String nome;
    private BigDecimal preco;
    private int peso;
    private String tipo;
    private boolean menorValor;

    public Product(String nome, BigDecimal preco, int peso, String tipo) {
        this.nome = nome;
        this.preco = preco;
        this.peso = peso;
        this.tipo = tipo;
        this.menorValor = false;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public int getPeso() {
        return peso;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getValorPorPeso() {
        if(getTipo().equalsIgnoreCase("kg") || getTipo().equalsIgnoreCase("l") ){
            return this.preco.divide(new BigDecimal(this.peso*1000), 5, BigDecimal.ROUND_HALF_UP);
        }
        return this.preco.divide(new BigDecimal(this.peso), 5, BigDecimal.ROUND_HALF_UP);
    }

    public boolean isMenorValor() {
        return menorValor;
    }

    public void setMenorValor(boolean menorValor) {
        this.menorValor = menorValor;
    }

    public String getTipoConvertido(){
        if ("kg".equalsIgnoreCase(tipo)){
            return "g";
        } else if ("l".equalsIgnoreCase(tipo)){
            return "mL";
        } else {
            return tipo;
        }
    }
}
