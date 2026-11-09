# Atividade — Sprint da Loja 2.0: compra protegida por estoque

**UC4 — Aula 13: Sprint de funcionalidades**

Preencha esta ficha durante a atividade e entregue sua cópia pelo Google Classroom. Você vai trabalhar no `LojaService` que a turma já construiu. Cadastro, busca de produtos, abertura de pedidos e validações básicas da compra já existem. Não recrie esses métodos. Sua missão é acrescentar a regra de estoque e comprovar que o restante do fluxo continua funcionando.

**Estudante(s):** __________________________________________________________  
**Turma:** ____________________  **Data:** ____/____/______

## Missão

Impedir que uma compra aprovada deixe o estoque negativo.

Ao final, o sistema deverá conferir o estoque de todos os itens antes de processar o pagamento. Se a compra for válida, deverá processar o pagamento, atualizar os saldos e registrar o pedido. Se alguma regra falhar, não poderá haver pagamento processado, saldo alterado ou pedido registrado.

## O que já existe no projeto

Antes de programar, localize estes elementos:

| Classe ou método | Responsabilidade atual |
|---|---|
| `LojaService.finalizarCompra(...)` | Valida regras básicas, processa o pagamento e registra o pedido. |
| `Pedido.getItens()` | Devolve os itens que fazem parte do pedido. |
| `ItemPedido.getProduto()` | Devolve o produto de uma linha do pedido. |
| `ItemPedido.getQuantidade()` | Devolve a quantidade pedida naquela linha. |
| `Produto.getQuantidade()` | Informa o saldo atual do produto. |
| `Produto.setQuantidade(...)` | Atualiza o saldo do produto. |
| `ItemPedido` | Já impede produto nulo e quantidade menor ou igual a zero. |

**Não remova as validações que já existem em `finalizarCompra`.** Complete o fluxo mantendo as regras atuais de cliente, pedido, produto cadastrado e valor do pagamento.

## Exercício 1 — Reproduza a falha

Antes de editar o Service, use a `Main` para simular uma compra acima do saldo. Cadastre um produto com 5 unidades e tente finalizar um pedido com 6. Observe o que o programa faz.

| O que observar | Resultado atual |
|---|---|
| A compra foi aceita ou recusada? | Foi aceita |
| O pagamento foi processado? | Sim |
| O saldo do produto mudou? | Não |
| O pedido entrou no histórico? | Sim depois do pagamento ser processado |

Faltava conferir se a quantidade pedida estava disponível. A validação deve ocorrer em `finalizarCompra`, depois de validar os itens e antes de processar o pagamento.

## O que foi implementado

`Produto` agora mantém o saldo com `getQuantidade()` e `setQuantidade()`. O saldo começa em zero e o setter recusa valores negativos. O `LojaService` valida o estoque de todos os produtos antes de chamar `pagamento.processar()`, considera o total de linhas repetidas do mesmo produto, atualiza os saldos após o pagamento e só então registra o pedido.

## Exercício 2 — Valide o estoque

Acrescente a validação ao método existente `finalizarCompra`. Preserve as verificações que já estão funcionando. Reutilize `ItemPedido` e `Produto`; não crie outro cadastro nem duplique a quantidade em uma nova lista.

Complete a ideia da regra:

> Para cada item, a quantidade pedida deve ser _menor ou igual_
> que a quantidade disponível no produto.


Registre os testes feitos:

| Saldo antes | Quantidade pedida | Resultado esperado | Resultado obtido |
|---:|---:|---|---|
| 5 | 2 | Compra aprovada com saldo final de 3 | Passou com saldo final 3 |
| 5 | 5 | Compra aprovada e saldo final 0 | Passou com saldo final 0 |
| 6 | 7 | Compra recusada e o saldo continua com 6 | Compra recusada e saldo continua 6 |

Registre onde fez a alteração:

LojaService.java no método finalizarCompra

Cole abaixo o trecho de validação que você acrescentou:

```java
for (itemPedido item : pedido.getItens()) {
    Produto produto = item.getProduto();
    if (item.getQuantidade() > produto.getQuantidade()) {
        throw new IllegalArgumentException(
            "Estoque insuficiente para o produto: " + produto.getNome() + 
            " (Solicitado: " + item.getQuantidade() + ", Disponível: " + produto.getQuantidade() + ")"
        );
    }
}
```

## Exercício 3 — Atualize depois da validação

Integre a baixa do estoque ao fluxo que já existe. Primeiro, todas as validações do pedido precisam passar. Depois, processe o pagamento, atualize os saldos e registre o pedido. Uma falha não pode deixar cobrança, saldo alterado ou pedido no histórico.

Organize as etapas na ordem escolhida:

| Etapa | Número da ordem |
|---|---:|
| Conferir o saldo de todos os itens | 1 |
| Processar o pagamento | 2 |
| Atualizar o saldo dos produtos | 3 |
| Registrar o pedido finalizado | 4 |

Agora teste uma falha. Para a situação abaixo, registre o estado antes e depois da tentativa:

| Situação | Antes da tentativa | Depois da tentativa |
|---|---|---|
| Saldo 3 com 4 pedidos | Saldo 3, pagamento não processado e pedidos registrados: 0 | Saldo: 3; pagamento: não processado E pedidos registrados 0 |

**Como foi confirmado:** Quando é dado um pedido inválido o ThrowNew


## Exercício 4 — Teste de regressão

Execute cada cenário na sua própria `Main`. Além da nova regra de estoque, confirme que a validação de pagamento da Aula 12 continua funcionando. Registre o resultado esperado e o observado.

| Cenário | Resultado esperado | Resultado observado | Passou? |
|---|---|---|---|
| Saldo 5; pedido 3 | Aprovar, saldo final 2 | Aprovado, saldo final 2 | Sim |
| Saldo 4; pedido 4 | Aprovar, saldo final 0 | Aprovado, saldo final 0 | Sim |
| Saldo 3; pedido 4 | Recusar sem efeitos | Recusado; saldo e histórico inalterados; pagamento não processado | Sim |
| Pagamento com valor-base diferente do total | Recusar sem efeitos | Recusado; saldo e histórico inalterados; pagamento não processado | Sim |
| Quantidade zero ao criar `ItemPedido` | Lançar `IllegalArgumentException` | `IllegalArgumentException` lançada | Sim |

Os cenários foram executados pela `Main`; os seis testes imprimiram `PASSOU`.

### Desafio extra — O mesmo produto em duas linhas

Considere um produto com saldo 5 e um pedido com duas linhas do mesmo produto: 3 unidades em uma linha e 3 na outra. A validação simples de cada linha, isoladamente, seria suficiente? Explique o risco e proponha como o Service poderia considerar a quantidade total pedida daquele produto.

Não. Verificar cada linha separadamente aceitaria 3 e 3 porque ambas são menores que o saldo 5, embora o pedido totalize 6. O `LojaService` soma as quantidades por produto e compara o total acumulado com o estoque antes de processar o pagamento.

## Entrega pelo Classroom

Envie esta ficha preenchida e anexe:

- [x] `LojaService.java` atualizado, preservando as validações que já existiam.
- [x] A `Main` usada para testar os cenários.
- [x] Registro dos testes com compra aceita, no limite e recusada na saída da `Main`.
- [x] Respostas desta ficha, incluindo o teste de pagamento divergente.
- [x] Se resolveu o desafio extra, a explicação da estratégia usada.

**Em uma frase: qual regra da operação o `LojaService` passou a proteger?**

O `LojaService` garante que uma compra só seja processada se houver estoque suficiente para todos os itens, sem permitir saldo negativo nem efeitos parciais quando a validação falha.

