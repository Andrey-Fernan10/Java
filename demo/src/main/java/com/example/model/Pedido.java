package com.example.model;

import java.util.ArrayList;

public class Pedido {

    private Cliente cliente;
    private ArrayList<ItemPedido> itens;

    public Pedido(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("O pedido precisa de um cliente.");
        }

        this.cliente = cliente; // VARIAVEL / CONSTRUTOR
        this.itens = new ArrayList<>();
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void adicionarItem(Produto produto, int quantidade) {
        itens.add(new ItemPedido(produto, quantidade));
    }

    public ArrayList<ItemPedido> getItens() {
        return itens;
    }

    public double calcularTotal() {
        double total = 0;

        for (ItemPedido item : itens) {
            total += item.calcularSubtotal();
        }

        return total;
    }
}