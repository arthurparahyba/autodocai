INSERT INTO prompt_spec (id, name, content) VALUES (
4,
'generate-methods-description',
'
O objetivo é analisar um código fonte fornecido e extrair informações sobre os métodos implementados:
{methodDescriptionExamples}

### Código:
{code}

');