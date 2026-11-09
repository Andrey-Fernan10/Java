package com.example.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import com.example.model.Cliente;
import com.example.model.Funcionario;
import com.example.model.ItemPedido;
import com.example.model.Pagamento;
import com.example.model.PagamentoBoleto;
import com.example.model.PagamentoCartao;
import com.example.model.PagamentoDinheiro;
import com.example.model.PagamentoPix;
import com.example.model.Pedido;
import com.example.model.Produto;
import com.example.controller.LojaController;

/**
 * Camada visual da demonstração. A interface encaminha as ações ao controller
 * e apresenta os resultados, sem concentrar o fluxo da loja aqui.
 */
public class LojaView extends JFrame {
    private static final Color BACKGROUND = new Color(242, 245, 249);
    private static final Color NAVY = new Color(28, 43, 64);

    private final LojaController controller;
    private final List<Funcionario> funcionarios;
    private final List<ItemCarrinho> carrinho = new ArrayList<>();

    private final JComboBox<Funcionario> funcionarioCombo = new JComboBox<>();
    private final JComboBox<Cliente> clienteCombo = new JComboBox<>();
    private final JComboBox<Produto> produtoCombo = new JComboBox<>();
    private final JComboBox<String> pagamentoCombo = new JComboBox<>(new String[] {
            "Pix", "Cartão (4,5%)", "Boleto (R$ 3,50)", "Dinheiro"
    });
    private final JSpinner quantidadeSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
    private final JLabel statusLabel = new JLabel("Comece identificando o funcionário e o cliente.");
    private final JLabel subtotalLabel = new JLabel("Subtotal: R$ 0,00");
    private final JLabel taxaLabel = new JLabel("Taxa: R$ 0,00");
    private final JLabel totalLabel = new JLabel("Total: R$ 0,00");

    private final DefaultTableModel produtosModel = modeloSomenteLeitura(
            "Código", "Produto", "Preço", "Estoque");
    private final DefaultTableModel carrinhoModel = modeloSomenteLeitura(
            "Produto", "Quantidade", "Subtotal");
    private final DefaultTableModel historicoModel = modeloSomenteLeitura(
            "Pedido", "Data", "Funcionário", "Cliente", "Total", "Pagamento");

    private final JTable produtosVendaTable = novaTabela(produtosModel);
    private final JTable produtosEstoqueTable = novaTabela(produtosModel);
    private final JTable carrinhoTable = novaTabela(carrinhoModel);
    private final JTable historicoTable = novaTabela(historicoModel);

    private String ultimoComprovante = "Nenhuma venda foi finalizada nesta sessão.";
    private int numeroPedido;

    public LojaView(LojaController controller, List<Funcionario> funcionarios) {
        this.controller = controller;
        this.funcionarios = List.copyOf(funcionarios);
        configurarJanela();
        configurarCombos();
        atualizarTabelas();
        pagamentoCombo.addActionListener(event -> atualizarTotais());
    }

    private void configurarJanela() {
        setTitle("Loja SENAC | Terminal de atendimento");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 680));
        setSize(1160, 770);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND);
        setLayout(new BorderLayout());
        add(criarCabecalho(), BorderLayout.NORTH);
        add(criarAbas(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);
    }

    private JPanel criarCabecalho() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));
        JLabel title = new JLabel("LOJA SENAC");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Terminal de vendas • atendimento, estoque e pagamentos");
        subtitle.setForeground(new Color(207, 219, 232));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JPanel labels = new JPanel(new GridBagLayout());
        labels.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        labels.add(title, c);
        c.gridy = 1;
        labels.add(subtitle, c);
        header.add(labels, BorderLayout.WEST);
        return header;
    }

    private JTabbedPane criarAbas() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        tabs.addTab("Atendimento e venda", criarAbaVenda());
        tabs.addTab("Estoque", criarAbaEstoque());
        tabs.addTab("Histórico", criarAbaHistorico());
        return tabs;
    }

    private JPanel criarAbaVenda() {
        JPanel page = new JPanel(new BorderLayout(12, 12));
        page.setBackground(BACKGROUND);
        page.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel people = card("1. Identificação do atendimento", new GridBagLayout());
        GridBagConstraints p = gridConstraints();
        adicionarCampo(people, p, 0, "Funcionário", funcionarioCombo);
        adicionarCampo(people, p, 1, "Cliente", clienteCombo);
        JButton addClient = secondaryButton("Cadastrar cliente");
        p.gridx = 2;
        p.gridy = 1;
        p.weightx = 0;
        people.add(addClient, p);
        addClient.addActionListener(event -> cadastrarCliente());

        JPanel chooseProduct = card("2. Seleção do produto", new GridBagLayout());
        GridBagConstraints choose = gridConstraints();
        adicionarCampo(chooseProduct, choose, 0, "Produto (com saldo atual)", produtoCombo);
        adicionarCampo(chooseProduct, choose, 1, "Quantidade", quantidadeSpinner);
        JButton addItem = primaryButton("Adicionar ao pedido");
        choose.gridx = 2;
        choose.gridy = 1;
        choose.weightx = 0;
        chooseProduct.add(addItem, choose);
        addItem.addActionListener(event -> adicionarItem());

        JPanel upper = new JPanel(new BorderLayout(0, 10));
        upper.setOpaque(false);
        upper.add(people, BorderLayout.NORTH);
        upper.add(chooseProduct, BorderLayout.SOUTH);

        JPanel catalog = tableCard("Catálogo e estoque", produtosVendaTable);
        JPanel currentOrder = tableCard("Pedido em andamento", carrinhoTable);
        JPanel tables = new JPanel(new GridBagLayout());
        tables.setOpaque(false);
        GridBagConstraints t = gridConstraints();
        t.gridy = 0;
        t.weightx = 1;
        t.weighty = 1;
        t.fill = GridBagConstraints.BOTH;
        tables.add(catalog, t);
        t.gridx = 1;
        tables.add(currentOrder, t);

        JPanel bottom = card("3. Pagamento", new GridBagLayout());
        GridBagConstraints b = gridConstraints();
        adicionarCampo(bottom, b, 0, "Forma de pagamento", pagamentoCombo);
        JButton removeItem = secondaryButton("Remover item selecionado");
        b.gridx = 1;
        b.gridy = 1;
        b.weightx = 0;
        bottom.add(removeItem, b);
        removeItem.addActionListener(event -> removerItem());

        JPanel totals = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 2));
        totals.setOpaque(false);
        totaisLabelStyle();
        totals.add(subtotalLabel);
        totals.add(taxaLabel);
        totals.add(totalLabel);
        b.gridx = 2;
        b.weightx = 1;
        b.anchor = GridBagConstraints.EAST;
        bottom.add(totals, b);

        JButton finish = primaryButton("Finalizar venda");
        b.gridx = 3;
        b.weightx = 0;
        bottom.add(finish, b);
        finish.addActionListener(event -> finalizarVenda());

        page.add(upper, BorderLayout.NORTH);
        page.add(tables, BorderLayout.CENTER);
        page.add(bottom, BorderLayout.SOUTH);
        return page;
    }

    private JPanel criarAbaEstoque() {
        JPanel page = new JPanel(new BorderLayout(12, 12));
        page.setBackground(BACKGROUND);
        page.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        page.add(tableCard("Saldo atual — atualizado depois de cada venda", produtosEstoqueTable),
                BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        actions.setBackground(Color.WHITE);
        JButton register = primaryButton("Cadastrar produto");
        JButton restock = primaryButton("Repor produto selecionado");
        JButton refresh = secondaryButton("Atualizar");
        actions.add(register);
        actions.add(restock);
        actions.add(refresh);
        register.addActionListener(event -> cadastrarProduto());
        restock.addActionListener(event -> reporEstoque());
        refresh.addActionListener(event -> atualizarTabelas());
        page.add(actions, BorderLayout.SOUTH);
        return page;
    }

    private JPanel criarAbaHistorico() {
        JPanel page = new JPanel(new BorderLayout(12, 12));
        page.setBackground(BACKGROUND);
        page.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        page.add(tableCard("Pedidos finalizados nesta sessão", historicoTable), BorderLayout.CENTER);
        JButton receipt = primaryButton("Abrir último comprovante");
        receipt.addActionListener(event -> JOptionPane.showMessageDialog(this, ultimoComprovante,
                "Comprovante", JOptionPane.INFORMATION_MESSAGE));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);
        actions.add(receipt);
        page.add(actions, BorderLayout.SOUTH);
        return page;
    }

    private JPanel criarRodape() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(230, 235, 241));
        footer.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        statusLabel.setForeground(new Color(59, 73, 91));
        footer.add(statusLabel, BorderLayout.WEST);
        JLabel note = new JLabel("Protótipo didático • dados em memória");
        note.setForeground(new Color(101, 113, 128));
        footer.add(note, BorderLayout.EAST);
        return footer;
    }

    private void configurarCombos() {
        funcionarios.forEach(funcionarioCombo::addItem);
        controller.listarClientes().forEach(clienteCombo::addItem);
        controller.listarProdutos().forEach(produtoCombo::addItem);
        funcionarioCombo.setSelectedIndex(-1);
        clienteCombo.setSelectedIndex(-1);

        funcionarioCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean selected, boolean focus) {
                super.getListCellRendererComponent(list, value, index, selected, focus);
                if (value instanceof Funcionario f) {
                    setText(f.getNome() + " • matrícula " + f.getMatricula());
                }
                return this;
            }
        });
        clienteCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean selected, boolean focus) {
                super.getListCellRendererComponent(list, value, index, selected, focus);
                if (value instanceof Cliente c) {
                    setText(c.getNome() + " • " + c.getDocumento() + " • " + c.getPontos() + " pts");
                }
                return this;
            }
        });
        produtoCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean selected, boolean focus) {
                super.getListCellRendererComponent(list, value, index, selected, focus);
                if (value instanceof Produto produto) {
                    setText(produto.getNome() + " • " + moeda(produto.calcularValorEstoque())
                            + " • estoque " + produto.getQuantidade());
                }
                return this;
            }
        });
    }

    private void adicionarItem() {
        Produto produto = (Produto) produtoCombo.getSelectedItem();
        if (produto == null) {
            erro("Selecione um produto.");
            return;
        }
        int quantidade = (Integer) quantidadeSpinner.getValue();
        int noCarrinho = carrinho.stream()
                .filter(item -> item.produto.getCodigo() == produto.getCodigo())
                .mapToInt(item -> item.quantidade).sum();
        if (noCarrinho + quantidade > produto.getQuantidade()) {
            erro("Estoque insuficiente. Disponível: " + produto.getQuantidade()
                    + "; já solicitado: " + noCarrinho + ".");
            return;
        }

        ItemCarrinho atual = carrinho.stream()
                .filter(item -> item.produto.getCodigo() == produto.getCodigo())
                .findFirst().orElse(null);
        if (atual == null) {
            carrinho.add(new ItemCarrinho(produto, quantidade));
        } else {
            atual.quantidade += quantidade;
        }
        atualizarCarrinho();
        statusLabel.setText("Produto incluído no pedido.");
    }

    private void removerItem() {
        int selected = carrinhoTable.getSelectedRow();
        if (selected < 0) {
            erro("Selecione um item do pedido para remover.");
            return;
        }
        carrinho.remove(carrinhoTable.convertRowIndexToModel(selected));
        atualizarCarrinho();
    }

    private void finalizarVenda() {
        Funcionario funcionario = (Funcionario) funcionarioCombo.getSelectedItem();
        Cliente cliente = (Cliente) clienteCombo.getSelectedItem();
        if (funcionario == null || cliente == null) {
            erro("Selecione o funcionário responsável e o cliente.");
            return;
        }
        if (carrinho.isEmpty()) {
            erro("Adicione pelo menos um produto ao pedido.");
            return;
        }

        try {
            Pedido pedido = controller.abrirPedido(cliente);
            for (ItemCarrinho item : carrinho) {
                pedido.adicionarItem(item.produto, item.quantidade);
            }
            Pagamento pagamento = criarPagamento(pedido.calcularTotal());
            controller.finalizarCompra(pedido, pagamento);
            registrarNaInterface(funcionario, cliente, pedido, pagamento);
        } catch (IllegalArgumentException erro) {
            erro(erro.getMessage());
        }
    }

    private Pagamento criarPagamento(double valorBase) {
        return switch ((String) pagamentoCombo.getSelectedItem()) {
            case "Cartão (4,5%)" -> new PagamentoCartao(valorBase, 4.5);
            case "Boleto (R$ 3,50)" -> new PagamentoBoleto(valorBase, 3.50);
            case "Dinheiro" -> new PagamentoDinheiro(valorBase, 0);
            default -> new PagamentoPix(valorBase);
        };
    }

    private void registrarNaInterface(Funcionario funcionario, Cliente cliente, Pedido pedido,
            Pagamento pagamento) {
        numeroPedido++;
        String tipo = (String) pagamentoCombo.getSelectedItem();
        historicoModel.addRow(new Object[] {
                String.format("#%04d", numeroPedido),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")),
                funcionario.getNome(), cliente.getNome(), moeda(pagamento.calcularTotal()), tipo
        });
        ultimoComprovante = criarComprovante(funcionario, cliente, pedido, pagamento, tipo);
        carrinho.clear();
        atualizarCarrinho();
        atualizarTabelas();
        statusLabel.setText("Venda concluída por " + funcionario.getNome() + ".");
        JOptionPane.showMessageDialog(this, ultimoComprovante,
                "Venda concluída", JOptionPane.INFORMATION_MESSAGE);
    }

    private String criarComprovante(Funcionario funcionario, Cliente cliente, Pedido pedido,
            Pagamento pagamento, String tipo) {
        StringBuilder out = new StringBuilder("LOJA SENAC — COMPROVANTE\n");
        out.append("Pedido: #").append(String.format("%04d", numeroPedido)).append('\n');
        out.append("Atendente: ").append(funcionario.getNome())
                .append(" (matrícula ").append(funcionario.getMatricula()).append(")\n");
        out.append("Cliente: ").append(cliente.getNome()).append('\n');
        out.append("----------------------------------------\n");
        for (ItemPedido item : pedido.getItens()) {
            out.append(String.format("%s | %d x %s = %s%n", item.getProduto().getNome(),
                    item.getQuantidade(), moeda(item.getProduto().calcularValorEstoque()),
                    moeda(item.calcularSubtotal())));
        }
        out.append("----------------------------------------\n");
        out.append("Subtotal: ").append(moeda(pedido.calcularTotal())).append('\n');
        out.append("Pagamento: ").append(tipo).append('\n');
        out.append("Taxa: ").append(moeda(pagamento.calcularTaxa())).append('\n');
        out.append("Total: ").append(moeda(pagamento.calcularTotal())).append('\n');
        out.append("Estoque baixado e pedido registrado.\n\n");
        out.append("Simulação local: nenhuma cobrança real foi realizada.");
        return out.toString();
    }

    private void cadastrarCliente() {
        JTextField nome = new JTextField(18);
        JTextField documento = new JTextField(18);
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = gridConstraints();
        adicionarCampo(form, c, 0, "Nome", nome);
        adicionarCampo(form, c, 1, "Documento", documento);
        int answer = JOptionPane.showConfirmDialog(this, form, "Cadastrar cliente",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (answer != JOptionPane.OK_OPTION) {
            return;
        }
        if (nome.getText().isBlank() || documento.getText().isBlank()) {
            erro("Informe o nome e o documento.");
            return;
        }
        try {
            Cliente cliente = new Cliente(nome.getText().trim(), documento.getText().trim(), 0);
            controller.cadastrarCliente(cliente);
            clienteCombo.addItem(cliente);
            clienteCombo.setSelectedItem(cliente);
            statusLabel.setText("Cliente cadastrado: " + cliente.getNome() + ".");
        } catch (IllegalArgumentException erro) {
            erro(erro.getMessage());
        }
    }

    private void cadastrarProduto() {
        JTextField nome = new JTextField(18);
        JTextField valor = new JTextField(18);
        JSpinner quantidade = new JSpinner(new SpinnerNumberModel(1, 0, 9999, 1));
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = gridConstraints();
        adicionarCampo(form, c, 0, "Nome do produto", nome);
        adicionarCampo(form, c, 1, "Preço (ex.: 49,90)", valor);
        adicionarCampo(form, c, 2, "Estoque inicial", quantidade);

        int answer = JOptionPane.showConfirmDialog(this, form, "Cadastrar produto",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (answer != JOptionPane.OK_OPTION) {
            return;
        }
        if (nome.getText().isBlank()) {
            erro("Informe o nome do produto.");
            return;
        }

        try {
            double preco = Double.parseDouble(valor.getText().trim().replace(',', '.'));
            if (preco <= 0) {
                erro("O preço deve ser maior que zero.");
                return;
            }
            Produto produto = new Produto(nome.getText().trim(), preco,
                    (Integer) quantidade.getValue());
            controller.cadastrarProduto(produto);
            atualizarTabelas();
            produtoCombo.setSelectedItem(produto);
            statusLabel.setText("Produto cadastrado: " + produto.getNome() + ".");
        } catch (NumberFormatException erro) {
            erro("Digite um preço válido, como 49,90.");
        } catch (IllegalArgumentException erro) {
            erro(erro.getMessage());
        }
    }

    private void reporEstoque() {
        int selected = produtosEstoqueTable.getSelectedRow();
        if (selected < 0) {
            erro("Selecione um produto na tabela.");
            return;
        }
        int row = produtosEstoqueTable.convertRowIndexToModel(selected);
        int codigo = (Integer) produtosModel.getValueAt(row, 0);
        String input = JOptionPane.showInputDialog(this,
                "Quantidade para adicionar ao estoque:", "Reposição de estoque",
                JOptionPane.QUESTION_MESSAGE);
        if (input == null) {
            return;
        }
        try {
            controller.reporEstoque(codigo, Integer.parseInt(input.trim()));
            atualizarTabelas();
            statusLabel.setText("Estoque reposto para o produto " + codigo + ".");
        } catch (NumberFormatException erro) {
            erro("Digite uma quantidade inteira.");
        } catch (IllegalArgumentException erro) {
            erro(erro.getMessage());
        }
    }

    private void atualizarCarrinho() {
        carrinhoModel.setRowCount(0);
        for (ItemCarrinho item : carrinho) {
            carrinhoModel.addRow(new Object[] { item.produto.getNome(), item.quantidade,
                    moeda(item.produto.calcularValorEstoque() * item.quantidade) });
        }
        atualizarTotais();
    }

    private void atualizarTotais() {
        double subtotal = carrinho.stream()
                .mapToDouble(item -> item.produto.calcularValorEstoque() * item.quantidade).sum();
        double taxa = taxaEstimativa(subtotal);
        subtotalLabel.setText("Subtotal: " + moeda(subtotal));
        taxaLabel.setText("Taxa: " + moeda(taxa));
        totalLabel.setText("Total: " + moeda(subtotal + taxa));
    }

    private double taxaEstimativa(double valor) {
        return switch ((String) pagamentoCombo.getSelectedItem()) {
            case "Cartão (4,5%)" -> valor * 0.045;
            case "Boleto (R$ 3,50)" -> valor == 0 ? 0 : 3.50;
            default -> 0;
        };
    }

    private void atualizarTabelas() {
        produtosModel.setRowCount(0);
        Produto selecionado = (Produto) produtoCombo.getSelectedItem();
        produtoCombo.removeAllItems();
        for (Produto produto : controller.listarProdutos()) {
            produtosModel.addRow(new Object[] { produto.getCodigo(), produto.getNome(),
                    moeda(produto.calcularValorEstoque()), produto.getQuantidade() });
            produtoCombo.addItem(produto);
        }
        if (selecionado != null) {
            produtoCombo.setSelectedItem(selecionado);
        }
        produtoCombo.repaint();
    }

    private void erro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Ação não concluída",
                JOptionPane.WARNING_MESSAGE);
        statusLabel.setText("Ação não concluída: " + mensagem);
    }

    private void totaisLabelStyle() {
        subtotalLabel.setForeground(new Color(83, 96, 111));
        taxaLabel.setForeground(new Color(83, 96, 111));
        totalLabel.setForeground(NAVY);
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
    }

    private static JTable novaTabela(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        return table;
    }

    private static DefaultTableModel modeloSomenteLeitura(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static JPanel card(String title, java.awt.LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(214, 221, 230)), title),
                BorderFactory.createEmptyBorder(4, 6, 6, 6)));
        return panel;
    }

    private static JPanel tableCard(String title, JTable table) {
        JPanel panel = card(title, new BorderLayout(6, 8));
        panel.setPreferredSize(new Dimension(430, 300));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private static GridBagConstraints gridConstraints() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 7, 5, 7);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        return c;
    }

    private static void adicionarCampo(JPanel panel, GridBagConstraints c, int column,
            String label, Component field) {
        c.gridx = column;
        c.gridy = 0;
        c.weightx = 0;
        panel.add(new JLabel(label), c);
        c.gridy = 1;
        c.weightx = 1;
        panel.add(field, c);
    }

    private static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(221, 234, 248));
        // O Look and Feel nativo do macOS pode manter o fundo claro do botão.
        // Texto escuro garante contraste também quando o fundo personalizado é ignorado.
        button.setForeground(NAVY);
        button.setFocusPainted(false);
        return button;
    }

    private static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        return button;
    }

    private static String moeda(double amount) {
        return String.format("R$ %.2f", amount);
    }

    private static final class ItemCarrinho {
        private final Produto produto;
        private int quantidade;

        private ItemCarrinho(Produto produto, int quantidade) {
            this.produto = produto;
            this.quantidade = quantidade;
        }
    }
}
