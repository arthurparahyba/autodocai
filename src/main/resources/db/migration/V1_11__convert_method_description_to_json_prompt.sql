INSERT INTO prompt_spec (id, name, content) VALUES (
5,
'convert-method-description-to-json',
'
Analise a descrição a seguir e gere a resposta em JSON

### Descrição:
{beforeOutput}

### Segue o schema json da resposta a ser gerada:
{format}

');