INSERT INTO prompt_spec (id, name, content) VALUES (
19,
'identify-method-for-sequence-diagram',
'
Você deve validar se no pedido do usuário é possível identificar a classe e o método a partir do qual um sistema irá gerar um diagrama de sequencia usando código Mermaid. Se no pedido do usuário for encontrado o nome da classe e método, utilize a função fornecida para pesquisar a classe no banco de dados.

Você deve retornar uma de duas opções:
1. O nome da classe e seu método e a indicação que o pedido é valido (isValid = true)
2. O nome da classe e seu método vazios e a indicação que o pedido é inválido (isValid = false)

### Pedido Usuário
{userMessage}

### Schema json que deve ser utilizado para entender como gerar a resposta
{format}

');
