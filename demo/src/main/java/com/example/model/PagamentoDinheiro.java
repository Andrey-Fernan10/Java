package com.example.model;

public class PagamentoDinheiro extends Pagamento{
    
    private double percentualTaxa;

    public PagamentoDinheiro(double valor, double percentualTaxa) {
        super(valor);
        this.percentualTaxa = percentualTaxa;
    }

    @Override
    public double calcularTaxa() {
        return getValor() * percentualTaxa / 100;
    }

    @Override
    public void processar() {
        System.out.println("----------------------------------------");
        System.out.println("Pagamento no Dinheiro " + calcularTotal());
       
        
    }
}
