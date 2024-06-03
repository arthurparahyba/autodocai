INSERT INTO prompt_spec (id, name, content) VALUES (
15,
'generate-cypher-from-description',
'
Uma explicação sobre como gerar uma Query Cypher para consultar os dados que foram pedidos pelo usuário foi gerada anteriormente. Dada a explicação,
gere o código Cypher e retorne os dados recuperados através da função disponibilizada.

### Explicação
{beforeOutput}


');

INSERT INTO prompt_spec_tools
(prompts_id, tools_id)
VALUES(15, 2);
