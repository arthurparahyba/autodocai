INSERT INTO prompt_spec (id, name, content) VALUES (
11,
'list-methods-and-called-attributes-methods',
'
Um sistema identificou os atributos que devem ser analisados no código fornecido posteriormente. Estes são os atributos identificados que são implementados na mesma aplicação que o código fornecido foi implementado. O objetivo é ler o código fornecido e encontrar os métodos que utilizam os atributos listados e quais métodos destes atributos são chamados.

{calledMethodExample}

Agora dada a lista de atributos a seguir, analise o código fornecido na sequência e liste os atributos encontrados conforme os exemplos anteriores

{beforeOutput}

### Código
{code}

### Segue o schema json da resposta a ser gerada:
{format}

');