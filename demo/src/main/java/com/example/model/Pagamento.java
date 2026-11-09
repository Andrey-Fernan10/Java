package com.example.model;

public abstract class Pagamento {
    
    public double valor; 

    public Pagamento(double valor) {
this.valor = valor;
    }
public double getValor() { return valor;}

public abstract double calcularTaxa();

public abstract void processar();

public double calcularTotal(){
    return valor + calcularTaxa();
    }    
}
