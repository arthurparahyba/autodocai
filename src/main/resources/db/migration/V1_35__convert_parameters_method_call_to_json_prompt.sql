INSERT INTO prompt_spec (id, name, content) VALUES (
24,
'convert-parameters-method-calls-to-json-prompt',
'
Uma explicação de comos os parametros são utilizados pelos métodos foi gerada anteriormente. Gere uma resposta em json com o nome de cada método e de suas chamadas aos métodos dos parametros.

{beforeOutput}

### Schema json para gerar a resposta
{format}

');
