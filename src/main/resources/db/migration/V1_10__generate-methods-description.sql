INSERT INTO prompt_spec (id, name, content) VALUES (
4,
'generate-methods-description',
'
O objetivo é analisar um código fonte fornecido e extrair informações sobre os métodos implementados:
{methodDescriptionExamples}

###Instruções:
1. Se o retorno não tiver sido encontrado, não crie o atributo retorno no json da resposta.

### Código:
{code}

');