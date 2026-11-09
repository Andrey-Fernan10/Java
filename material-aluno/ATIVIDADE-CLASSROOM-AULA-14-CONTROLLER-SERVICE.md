# Aula 14 — A View pede, o Controller encaminha

## Desafio

A interface da Loja SENAC já está pronta e o `LojaService` já contém as regras da loja. Porém, a View conversa com o `LojaController`, e alguns métodos desse Controller ainda não encaminham as operações.

Sua tarefa é completar o `LojaController` para conectar a interface ao Service. **Não altere a View nem reescreva as regras do Service.**

```text
LojaView → LojaController → LojaService → objetos do domínio
```

O Controller recebe a solicitação da interface e chama o método adequado do Service. O Service valida e executa a operação. Quando o método retorna um dado, o Controller devolve esse resultado à View.

## Arquivos

- Complete `src/main/java/br/com/senac/controller/LojaController.java` usando o esqueleto entregue em `material-aluno`.
- Consulte `src/main/java/br/com/senac/service/LojaService.java` para encontrar os métodos disponíveis.
- Use a `LojaView.java` e a `LojaApplication.java` já fornecidas. Não as altere.

## Parte 1 — Catálogo e estoque

Antes de programar, leia os métodos disponíveis no `LojaService`. Para cada operação abaixo, descubra qual método do Service atende à necessidade e anote o nome na última coluna.

| Método do Controller | O que procurar no `LojaService` |
| --- | --- |
| `cadastrarCliente(cliente)` | A operação que registra o cliente na loja. |
| `cadastrarProduto(produto)` | A operação que registra um produto novo. |
| `buscarProdutoPorCodigo(codigo)` | A busca de um produto pelo código, retornando `null` se não encontrar. |
| `listarClientes()` | A consulta que fornece os clientes cadastrados. |
| `listarProdutos()` | A consulta que fornece os produtos e seus saldos atuais. |
| `reporEstoque(codigo, quantidade)` | A operação que acrescenta unidades ao estoque e valida os dados. |

Depois, implemente os métodos correspondentes do Controller. Use os parâmetros e tipos de retorno já definidos no esqueleto.

### Teste na interface

1. Cadastre um produto e confira se aparece no catálogo e no seletor de venda.
2. Consulte um código existente e um código que não existe.
3. Faça uma reposição válida e confira o saldo atualizado.
4. Tente repor zero ou uma quantidade negativa. Observe a resposta da regra que o Service já implementa.

## Parte 2 — Pedido e compra

Complete a investigação antes de programar:

| Método do Controller | O que procurar no `LojaService` |
| --- | --- |
| `abrirPedido(cliente)` | A operação que inicia um pedido para um cliente e devolve o pedido criado. |
| `finalizarCompra(pedido, pagamento)` | A operação que valida e conclui a compra a partir do pedido e pagamento. |
| `listarPedidos()` | A consulta que fornece os pedidos já finalizados. |

Implemente os métodos correspondentes do Controller. Quando o Service devolver um objeto ou uma lista, o Controller precisa devolver esse resultado à View.

### Teste na interface

1. Faça uma compra válida e confira o comprovante, o histórico e o estoque.
2. Tente comprar mais unidades do que há no estoque.
3. Tente finalizar um pedido sem itens, se o fluxo da tela permitir esse teste.
4. Confira se uma compra recusada não processa o pagamento, não altera o estoque e não entra no histórico.

Essas validações pertencem ao `LojaService`. O Controller deve encaminhar a operação e permitir que a resposta ou exceção chegue à View.

## Entrega no Classroom

Envie o `LojaController.java` completo e responda:

1. Qual método da View inicia o caminho de uma operação?
2. O que o Controller faz antes de devolver o resultado à View?
3. Em qual classe estão as regras que aprovam ou recusam uma compra?
4. O que aconteceu com estoque e histórico quando a compra foi recusada?

## Critério de conclusão

O fluxo funciona pela interface sem duplicar regras no Controller. Uma compra aprovada atualiza os dados; uma compra inválida é recusada pelo Service sem produzir efeitos indevidos.
