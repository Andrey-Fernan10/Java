package com.example.model;

public class PagamentoCartao extends Pagamento implements Pagavel{

    private double percentualTaxa;

    public PagamentoCartao(double valor, double percentualTaxa) {
        super(valor);
        this.percentualTaxa = percentualTaxa;
    }

    @Override
    public double calcularTaxa() {
        return getValor() * percentualTaxa / 100;
    }

    @Override
    public void processar() {
        System.out.println("-----------------------------------------------");
        System.out.println("Pagamento via Cratão  " + String.format("%.2f", calcularTotal()));
        
    }
}