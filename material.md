# Missão: fluxo de atendimento da loja

## O problema

A loja já possui clientes, produtos e formas de pagamento. Falta organizar o que acontece durante um atendimento: clientes aguardam, um pedido reúne os produtos escolhidos, o pagamento é processado e a loja registra as ações realizadas.

Sua dupla deve criar um fluxo simples de compra usando as classes já estudadas.

## Classes fornecidas pelo docente

Copie as duas classes abaixo para o pacote `br.com.senac.model`. Elas já estão prontas. Não é necessário alterá-las nesta aula.

### `ItemPedido.java`

```java
package br.com.senac.model;

public class ItemPedido {
    private Produto produto;
    private int quantidade;

    public ItemPedido(Produto produto, int quantidade) {
        if (produto == null || quantidade <= 0) {
            throw new IllegalArgumentException("Produto e quantidade devem ser válidos.");
        }

        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double calcularSubtotal() {
        return produto.calcularPrecoFinal() * quantidade;
    }
}
```

### `Pedido.java`

```java
package br.com.senac.model;

import java.util.ArrayList;

public class Pedido {
    private Cliente cliente;
    private ArrayList<ItemPedido> itens;

    public Pedido(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("O pedido precisa de um cliente.");
        }

        this.cliente = cliente;
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
```

## Sua missão

Crie uma classe `Main` no pacote `br.com.senac.main`. O programa deve simular um atendimento completo.

### Parte 1 — Fila de clientes

Crie uma `ArrayList<Cliente>` chamada `filaAtendimento`.

- Crie pelo menos três clientes.
- Adicione-os à lista na ordem em que chegam.
- Atenda o primeiro cliente usando `remove(0)`.
- Mostre o nome de quem foi atendido e quem continua aguardando.

### Parte 2 — Pedido do cliente atendido

- Crie ao menos um `ProdutoFisico` e um `ProdutoDigital`.
- Crie um `Pedido` para o cliente que saiu da fila.
- Adicione os dois produtos ao pedido, com quantidades válidas.
- Mostre o total calculado pelo pedido.

### Parte 3 — Pagamento

- Escolha uma classe concreta de pagamento já existente, como `PagamentoPix`, `PagamentoCartao`, `PagamentoBoleto` ou `PagamentoDinheiro`.
- Crie o pagamento usando `pedido.calcularTotal()`.
- Chame `processar()`.
- Mostre `calcularTotal()` do pagamento.

### Parte 4 — Histórico da loja

Crie uma `ArrayList<String>` chamada `historico`.

Registre, no mínimo:

1. A entrada dos clientes na fila.
2. O cliente que foi atendido.
3. A criação do pedido.
4. O pagamento realizado.

Depois, remova a última ação com:

```java
historico.remove(historico.size() - 1);
```

Mostre o histórico antes e depois de desfazer a ação.

## Regras da tarefa

- Use as classes existentes. Não altere a geração automática de `codigo` em `Produto`.
- Use `ArrayList<Cliente>` para a fila e `ArrayList<String>` para o histórico.
- Não use `Queue`, `Stack`, `Deque`, `LinkedList` ou `ArrayDeque` ainda. Esses tipos serão o tema da próxima aula.
- Cada integrante da dupla deve conseguir explicar pelo menos uma decisão do código.

## Entrega

Ao final, apresente o programa executando e responda:

1. Por que o cliente atendido sai da posição `0`?
2. Por que a ação desfeita sai da última posição?
3. A `ArrayList` resolveu os dois casos? Ela parece igualmente adequada para ambos?

## Critérios de conclusão

- Fila com três clientes e atendimento do primeiro.
- Pedido criado para o cliente atendido.
- Ao menos dois produtos adicionados ao pedido.
- Pagamento processado usando o total do pedido.
- Histórico registrado e última ação removida.
- Código executando sem erros.