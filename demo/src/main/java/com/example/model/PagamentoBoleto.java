package com.example.model;

public class PagamentoBoleto extends Pagamento {
    
    private double taxaFixa;

    public PagamentoBoleto(double valor, double taxaFixa) {
        super(valor);
        this.taxaFixa = taxaFixa;
    }

    @Override
    public double calcularTaxa() {
        return taxaFixa;
    }

    @Override
    public void processar() {
        System.out.println("---------------------------------------");
        System.out.println("Pagamendo via Boleto " + calcularTotal());
        
    }
}