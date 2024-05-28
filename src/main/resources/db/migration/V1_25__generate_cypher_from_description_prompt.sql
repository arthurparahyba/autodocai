INSERT INTO prompt_spec (id, name, content) VALUES (
15,
'generate-cypher-from-description',
'
Uma explicação sobre como gerar uma Query Cypher para consultar os dados que foram pedidos pelo usuário foi gerada anteriormente. Dada a explicação,
gere o código Cypher que será usado para executar a consulta no banco de dados

### Explicação
{beforeOutput}

### Schema json que deve ser usado para gerar a resposta:
{format}

');
