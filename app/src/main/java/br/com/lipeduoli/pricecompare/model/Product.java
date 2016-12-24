package br.com.lipeduoli.pricecompare.model;

import java.math.BigDecimal;

/**
 * Created by liped on 05/07/2016.
 */
public class Product {

    private BigDecimal valorPeso;
    private String nome;
    private BigDecimal preco;
    private int peso;
    private String tipo;
    private boolean menorValor;

    public Product(String nome, BigDecimal preco, int peso, String tipo) {
        this.nome = nome;
        this.preco = preco;
        this.peso = peso;
        setTipo(tipo);
        this.menorValor = false;
        this.valorPeso = this.preco.divide(new BigDecimal(this.peso), 5, BigDecimal.ROUND_HALF_UP);
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
        return valorPeso;
    }

    public boolean isMenorValor() {
        return menorValor;
    }

    public void setMenorValor(boolean menorValor) {
        this.menorValor = menorValor;
    }

    private void setTipo(String tipo) {
        if ("kg".equalsIgnoreCase(tipo)){
            peso = peso*1000;
            this.tipo = "g";
        } else if ("l".equalsIgnoreCase(tipo)){
            peso = peso*1000;
            this.tipo = "mL";
        } else {
            this.tipo = tipo;
        }
    }
}
