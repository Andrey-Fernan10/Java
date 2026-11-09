package com.example.model;

public class ProdutoDigital extends Produto {

    public double licenca;
    
    public ProdutoDigital(String nome, double preco, int quantidade, double licenca) {
        super(nome, preco, quantidade);

        if (licenca < 0) {
            throw new IllegalArgumentException("A licença não pode ser negativo");
        }

        this.licenca = licenca;
    }

    public double getFrete() {
        return licenca;
    }

    public void setFrete(double licenca) {
        if (licenca < 0) {
            throw new IllegalArgumentException("Frete não pode ser negativo");
        }

        this.licenca = licenca;
    }

    @Override
    public double calcularValorEstoque() {
        return getPreco() * getQuantidade() + licenca;
    }
}