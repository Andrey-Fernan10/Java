package com.example.model;

public class PagamentoPix extends Pagamento {

    public PagamentoPix(double valor) {
        super(valor);
    }

    @Override
    public double calcularTaxa() {
        return 0.0;
    }

    @Override
    public void processar() {
        System.out.println("Pagamento via PIX no valor de R$ " + getValor() + " processado com sucesso!");
    }
}