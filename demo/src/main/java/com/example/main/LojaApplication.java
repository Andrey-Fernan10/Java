package com.example.main;

import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.example.controller.LojaController;
import com.example.model.Cliente;
import com.example.model.Funcionario;
import com.example.model.Produto;
import com.example.service.LojaService;
import com.example.view.LojaView;

/** Ponto de entrada da aplicação Swing de demonstração. */
public class LojaApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LojaApplication::iniciarAplicacao);
    }

    private static void iniciarAplicacao() {
        configurarAparencia();

        LojaService lojaService = new LojaService();
        LojaController controller = new LojaController(lojaService);
        carregarDadosDemonstrativos(controller);
        List<Funcionario> funcionarios = criarFuncionariosDemonstrativos();

        new LojaView(controller, funcionarios).setVisible(true);
    }

    private static void carregarDadosDemonstrativos(LojaController controller) {
        controller.cadastrarCliente(new Cliente("Ana Souza", "111.222.333-44", 35));
        controller.cadastrarCliente(new Cliente("Bruno Lima", "222.333.444-55", 10));
        controller.cadastrarCliente(new Cliente("Carla Mendes", "333.444.555-66", 0));

        controller.cadastrarProduto(new Produto("Livro Java", 59.90, 12));
        controller.cadastrarProduto(new Produto("Caderno de programação", 24.50, 30));
        controller.cadastrarProduto(new Produto("Mouse sem fio", 79.90, 8));
        controller.cadastrarProduto(new Produto("Teclado USB", 99.90, 5));
    }

    private static List<Funcionario> criarFuncionariosDemonstrativos() {
        Funcionario funcionario1 = new Funcionario();
        funcionario1.setNome("Marcos Oliveira");
        funcionario1.setDocumento("123.456.789-00");
        funcionario1.setMatricula("SEN-1042");

        Funcionario funcionario2 = new Funcionario();
        funcionario2.setNome("Juliana Costa");
        funcionario2.setDocumento("987.654.321-00");
        funcionario2.setMatricula("SEN-2087");

        return List.of(funcionario1, funcionario2);
    }

    private static void configurarAparencia() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignorada) {
            // O Swing usa o tema padrão caso o tema nativo não esteja disponível.
        }
    }
}
