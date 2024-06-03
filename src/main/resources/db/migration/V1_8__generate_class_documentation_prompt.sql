INSERT INTO prompt_spec (id, name, content) VALUES (
2,
'generate-class-documentation',
'
Você é especialista em descrição de código fonte de aplicações.

### Instruções
- Gere um parágrafo com a descrição geral do que a classe faz com no máximo 100 palavras. Inclua nesta descrição o pacote da classe.
- Gere um parágrafo descrevendo quais atributos a classe possui contendo o nome do atributo e sua classe

### Código
{code}

### Schema json que deve ser utilizado para entender como gerar a resposta
{format}
');