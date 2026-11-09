package com.example.model;

public class Cliente extends Usuario {

    private int codigo;
    private int pontos;
    private static int totalClientes = 0;

    public Cliente(String nome, String documento, int pontos) {
        super(nome, documento);
        this.pontos = pontos;
        this.codigo = ++totalClientes;
    }

    public int getPontos() { return pontos; }

    public int getCodigo() {
        return codigo;
    }

    public void setPontos(int pontos) {
        if (pontos < 0) {
            throw new IllegalArgumentException("Pontos não podem ser negativos");
        }

        this.pontos = pontos;
    }

    public void adicionarPontos(int qtd) {
        if (qtd > 0) {
            this.pontos += qtd;
        }
    }

    @Override
    public void exibirDados() {
        System.out.println("--- Dados do Cliente ---");
        System.out.println("Código: " + this.codigo);
        super.exibirDados();
        System.out.println("Pontos: " + pontos);
    }

    
}