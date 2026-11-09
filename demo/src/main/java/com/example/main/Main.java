package com.example.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.model.Cliente;
import com.example.model.Pagamento;
import com.example.model.PagamentoPix;
import com.example.model.Pedido;
import com.example.model.Produto;
import com.example.service.LojaService;

public class Main {
    public static void main(String[] args) {

        Set<String> documentosCadastrados = new HashSet<>();
        List<Cliente> listaClientes = new ArrayList<>();

        String[][] dadosTestes = {
            {"Ana", "111.222.333-00", "10"},
            {"Carlos", "444.555.666-11", "20"}
        };

        System.out.println("=== SISTEMA DE CADASTRO DE CLIENTES ===");

        for (String[] dado : dadosTestes) {
            String nome = dado[0];
            String documento = dado[1];
            int pontos = Integer.parseInt(dado[2]);

            if (documentosCadastrados.add(documento)) {
                Cliente novoCliente = new Cliente(nome, documento, pontos);
                listaClientes.add(novoCliente);
                System.out.println("[SUCESSO] Cliente cadastrado com sucesso: " + nome);
            } else {
                System.out.println("[ERRO] Cadastro recusado! O documento " + documento + " já existe no sistema.");
            }
        }

        System.out.println("\n=== TESTANDO O LOJA SERVICE ===");

        LojaService loja = new LojaService();

        Cliente ana = listaClientes.get(0);
        Produto livro = new Produto("Livro Java", 50.0, 5);

        loja.cadastrarCliente(ana);
        loja.cadastrarProduto(livro);

        Pedido pedido = loja.abrirPedido(ana);
        pedido.adicionarItem(livro, 6);

        double totalPedido = pedido.calcularTotal();

        Pagamento pagamento = new PagamentoPix(totalPedido);

        try {
            loja.finalizarCompra(pedido, pagamento);
            System.out.println("Compra finalizada com sucesso! Total do pedido: R$ " + totalPedido);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro na validação do valor: " + e.getMessage());
        }
    }
}