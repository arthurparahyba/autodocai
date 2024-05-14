INSERT INTO section (id, name, content)
VALUES (
3,
'neo4jExamplesQuery',
'
- **Exemplo 1:
    Objetivo: Query usada para recuperar  as classes, atributos, funções, parâmetros das funções e as chamadas que elas fazem a outras funções de outras classes.**:
    Observações:
        1. Essa query utiliza OPTIONAL, pois trará os dados de outros Nodes quando existir. Isso faz com que ela não filtra nenhuma CodeEntity por não ter determinada relação com outro Node.
        2. Adicione ou remova OPTIONAL MATCH caso necessário de acordo com os dados a serem retornados.
        3. Os dados retornados em lista são agrupados utilizando COLLECT fazendo com que a requisição não gere muitas linhas duplicadas
    Query:
    ```cypher
    MATCH (app:Application {id: 157})-[:DEPENDS]->(codeEntity:CodeEntity)
    OPTIONAL MATCH (codeEntity)-[:HAS_FIELD]->(field:Field)
    OPTIONAL MATCH (codeEntity)-[:HAS_FUNCTION]->(function:Function)
    OPTIONAL MATCH (function)-[:RECEIVES]->(parameter:Parameter)
    OPTIONAL MATCH (function)-[:USES]->(calledFunction:Function)-[:HAS_FUNCTION]->(calledFunctionCodeEntity:CodeEntity)
    RETURN codeEntity.className AS className,
           COLLECT(DISTINCT field.name) AS fields,
           function.name AS functionName,
           COLLECT(DISTINCT parameter.name) AS parameters,
           calledFunction.name AS calledFunctionName, calledFunctionCodeEntity.className AS calledFunctionClassName
    ```
');