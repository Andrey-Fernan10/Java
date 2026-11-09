package com.example.service;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Cliente;
import com.example.model.Pagamento;
import com.example.model.Pedido;
import com.example.model.Produto;
import com.example.model.ItemPedido;

public class LojaService {
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Produto> produtos = new ArrayList<>();
    private final List<Pedido> pedidos = new ArrayList<>();

    public void cadastrarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public void cadastrarProduto(Produto produto) {
        produtos.add(produto);
    }

    public Produto buscarProdutoPorCodigo(int codigo) {
        for (Produto produto : produtos) {
            if (produto.getCodigo() == codigo) {
                return produto;
            }
        }
        return null;
    }

    public Pedido abrirPedido(Cliente cliente) {
        if (!clienteCadastrado(cliente)) {
            throw new IllegalArgumentException("Cliente não cadastrado na loja.");
        }
        return new Pedido(cliente);
    }

    public void finalizarCompra(Pedido pedido, Pagamento pagamento) {
        if (pedido == null || pagamento == null) {
            throw new IllegalArgumentException("Pedido e pagamento são obrigatórios.");
        }

        if (!clienteCadastrado(pedido.getCliente())) {
            throw new IllegalArgumentException("Cliente do pedido não está cadastrado na loja.");
        }

        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new IllegalArgumentException("O pedido precisa ter pelo menos um item.");
        }

        for (ItemPedido item : pedido.getItens()) {
            Produto prod = item.getProduto();
            
            if (prod == null || buscarProdutoPorCodigo(prod.getCodigo()) == null) {
                throw new IllegalArgumentException("Produto não cadastrado na loja.");
            }

            if (item.getQuantidade() > prod.getQuantidade()) {
                throw new IllegalArgumentException(
                    "Estoque insuficiente para o produto: " + prod.getNome() + 
                    " (Solicitado: " + item.getQuantidade() + ", Disponível: " + prod.getQuantidade() + ")"
                );
            }
        }

        if (Double.compare(pagamento.getValor(), pedido.calcularTotal()) != 0) {
            throw new IllegalArgumentException("Pagamento diferente do pedido.");
        }

        pagamento.processar();

        for (ItemPedido item : pedido.getItens()) {
            item.getProduto().removerEstoque(item.getQuantidade());
        }

        pedidos.add(pedido);
    }

    public List<Pedido> listarPedidos() {
        return new ArrayList<>(pedidos);
    }

    private boolean clienteCadastrado(Cliente cliente) {
        if (cliente == null) {
            return false;
        }
        return clientes.contains(cliente);
    }

    // Mudamos de 'void' para 'List<Cliente>' para a tela receber os dados
    public List<Cliente> listarClientes() {
        return clientes;
    }

    // Mudamos de 'void' para 'List<Produto>' para a tela receber os dados
    public List<Produto> listarProdutos() {
        return produtos;
    }

    public void reporEstoque() {
        // Pode continuar totalmente vazio!
    }
}