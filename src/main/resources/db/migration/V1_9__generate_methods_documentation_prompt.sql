INSERT INTO prompt_spec (id, name, content) VALUES (
3,
'generate-methods-documentation',
'
Você é especialista em descrição de código fonte de aplicações.

### Instruções
- Para cada método implementado na classe, gere dois parágrafos:
  - O primeiro parágrafo com um resumo sobre o que o método faz com no máximo 30 palavras
  - O segundo parágrafo com as ações implementadas no método sequencialmente. Neste parágrafo é importante descrever de forma que seja possível construir um diagrama de sequencia da implementação deste método. Caso ele faça referencia a outras classes chamando seus métodos, informa o nome do método seus parâmetros e o nome da classe que contém o método. Para cada comportamento descrito do método, informa qual método é utilizado para que o comportamento seja executado. Ex: a variável X é verificada se é diferente de nulo, caso sim, ela é salva no banco de dados através do método y da classe Z.lasse.

### Código
{code}

### Schema json que deve ser utilizado para entender como gerar a resposta
{format}
');