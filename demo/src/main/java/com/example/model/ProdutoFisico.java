package com.example.model;

public class ProdutoFisico extends Produto {

    private double frete;

    public ProdutoFisico(String nome, double preco, int quantidade, double frete) {
        super(nome, preco, quantidade);

        if (frete < 0) {
            throw new IllegalArgumentException("Frete não pode ser negativo");
        }

        this.frete = frete;
    }

    public double getFrete() {
        return frete;
    }

    public void setFrete(double frete) {
        if (frete < 0) {
            throw new IllegalArgumentException("Frete não pode ser negativo");
        }

        this.frete = frete;
    }

    @Override
    public double calcularValorEstoque() {
        return getPreco() * getQuantidade() + frete;
    }
}