# Pendências do módulo de estoque e CMV

## Regras de negócio

- [x] Definir os tipos de transação aceitos: `inflow` e `outflow`.
- [x] Atualizar o estoque ao registrar uma entrada.
- [x] Atualizar o estoque ao registrar uma saída.
- [x] Recalcular o custo médio ponderado após cada entrada.
- [x] Calcular o CMV de cada saída com base no custo médio vigente.
- [x] Impedir saída com quantidade maior que o estoque disponível.
- [x] Criar estoque automaticamente no primeiro lançamento do produto.
- [x] Tornar transações imutáveis após criação.
- [x] Exigir transação de reversão para corrigir ou desfazer um lançamento.

## Validações

- [x] Validar produto obrigatório e existente.
- [x] Validar tipo de transação obrigatório.
- [x] Validar quantidade maior que zero.
- [x] Validar valor unitário maior ou igual a zero.
- [x] Normalizar quantidade e valores para as escalas definidas.
- [x] Retornar mensagens de erro padronizadas para dados inválidos.

## Persistência

- [x] Adicionar a coluna `cmv` para armazenar o CMV da transação.
- [x] Definir a precisão e a escala dos campos monetários.
- [x] Garantir que a atualização da transação e do estoque ocorra na mesma transação do banco.
- [x] Usar bloqueio pessimista no estoque durante o lançamento.

## API

- [x] Definir o formato da resposta de uma saída com o CMV calculado.
- [x] Disponibilizar endpoint para consultar o saldo atual do estoque.
- [ ] Definir endpoint ou consulta para relatório de CMV por período.
- [ ] Padronizar os códigos HTTP e o formato dos erros.

## Testes

- [ ] Testar entrada inicial de produto.
- [ ] Testar entradas sucessivas com recálculo do custo médio.
- [ ] Testar saída com cálculo do CMV.
- [ ] Testar saída sem estoque suficiente.
- [ ] Testar produto inexistente.
- [ ] Testar quantidade e valor inválidos.
- [ ] Testar edição e exclusão de transações, caso sejam permitidas.
- [ ] Testar lançamentos concorrentes.
