package com.example.model;

public class Produto {

    private final int codigo;
    private String nome;
    private double preco;
    private int quantidade;

    private static final double DESCONTO_MAXIMO = 50;

    private static int totalProdutos;

    public Produto(String nome, double preco, int quantidade) {
        if (preco < 0 || quantidade < 0) {
            throw new IllegalArgumentException("Argumento invalido");
        }

        codigo = ++totalProdutos;

        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }

        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }

        this.quantidade = quantidade;
    }

    public boolean temEstoque() {
        return quantidade > 0;
    }

    public void exibirResumo() {
        System.out.printf(
            "Cód: %d | %s | Preço: R$ %.2f | Estoque: %d\n",
            codigo, nome, preco, quantidade
        );
    }

    public boolean adicionarEstoque(int qtd) {
        if (qtd <= 0) {
            return false;
        }

        this.quantidade += qtd;
        return true;
    }

    public boolean removerEstoque(int qtd) {
        if (qtd <= 0 || qtd > quantidade) {
            return false;
        }

        this.quantidade -= qtd;
        return true;
    }

    public boolean aplicarDesconto(double percentual) {
        if (percentual <= 0 || percentual > DESCONTO_MAXIMO) {
            return false;
        }

        preco -= preco * percentual / 100;
        return true;
    }

    public boolean aplicarDesconto(double valor, boolean fixo) {
        if (!fixo || valor <= 0 || valor > preco) {
            return false;
        }

        preco -= valor;
        return true;
    }

    public double calcularValorEstoque() {
        return preco * quantidade;
    }
}