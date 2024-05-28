INSERT INTO section (id, name, content)
VALUES (
3,
'neo4jExamplesQuery',
'
Q: Gere um diagrama de classes com as classes da camada de aplicação. Id da aplicação: 1
R:
Para um diagrama de classes com as classes da camada de aplicação, vou usar Layer com nome APPLICATION, seguindo o que foi definido na modelagem do banco de dados.
Também devo considerar que para montar um diagrama de classes, é preciso buscar o nome da classe, seus atributos e métodos.
- Primeiro inicio a consulta da Layer com nome APPLICATION da aplicação com id 1:
MATCH (app:Application {id: 1})-[:HAS_LAYER]->(layer:Layer {name: "APPLICATION"})
- Aí já posso adicionar o match para buscar as CodeEntity:
OPTIONAL MATCH (layer)-[:HAS_CODE_ENTITY]->(codeEntity:CodeEntity)
- Depois adiciono o match para buscar os atributos e o match para buscar o tipo de cada atributo:
OPTIONAL MATCH (codeEntity)-[:HAS_FIELD]->(field:Field)
OPTIONAL MATCH (field)-[:OF_TYPE]->(codeEntityField:CodeEntity)
- Depois adiciono o match para buscar os métodos das classes. Mesmo não tendo sido pedido, vou ignorar os getters e setters para focar no mais importante:
OPTIONAL MATCH (codeEntity)-[:HAS_FUNCTION]->(func:Function)
WHERE NOT func.name STARTS WITH "get" AND NOT func.name STARTS WITH "set"
- Agora adiciono o match para consultar o tipo de retorno dos métodos:
OPTIONAL MATCH (func)-[:RETURNS]->(returnEntity:CodeEntity)
- E adiono o match para consultar os parâmetros dos métodos:
OPTIONAL MATCH (func)-[:USES]->(params:CodeEntity)
- Agora começo a montar o retorno da consulta com os dados da classe:
RETURN codeEntity.packageName AS packageName, codeEntity.className AS className
- Depois adiciono no retorno os dados dos atributos concatenando com o tipo do atributo e usando collect para não duplicar as linhas caso as classes tenham mais de um método
, collect(field.name + ": "+ coalesce(codeEntityField.className, "N/A")) AS attributes
-Agora adiciono os dados relativo aos métodos das classes concatenando com o tipo do retorno do método
, func.name+": "+coalesce(returnEntity.className, "void") AS functions
- Para finalizar, adiciono os parametros recebicos pelas funções também usando o collect para agrupar os dados para cada função:
, collect(distinct params.className) as parameters

Q: Gere um diagrama de classes com as classes do pacote br.com.myapplication.model. Id da aplicação: 1
R:
Para um diagrama de classes, é preciso buscar o nome da classe, seus atributos e métodos.
- Primeiro inicio a consulta das CodeEntity da aplicação com id 1:
MATCH (app:Application {id: 1})-[:DEPENDS]->(codeEntity:CodeEntity)
- Para buscar apenas as CodeEntity do pacote pedido, adiciono o filtro de pacote:
WHERE codeEntity.packageName CONTAINS "br.com.myapplication.model"
- Depois adiciono o match para buscar os atributos e o match para buscar o tipo de cada atributo:
OPTIONAL MATCH (codeEntity)-[:HAS_FIELD]->(field:Field)
OPTIONAL MATCH (field)-[:OF_TYPE]->(codeEntityField:CodeEntity)
- Depois adiciono o match para buscar os métodos das classes. Mesmo não tendo sido pedido, vou ignorar os getters e setters para focar no mais importante:
OPTIONAL MATCH (codeEntity)-[:HAS_FUNCTION]->(func:Function)
WHERE NOT func.name STARTS WITH "get" AND NOT func.name STARTS WITH "set"
- Agora adiciono o match para consultar o tipo de retorno dos métodos:
OPTIONAL MATCH (func)-[:RETURNS]->(returnEntity:CodeEntity)
- E adiono o match para consultar os parâmetros dos métodos:
OPTIONAL MATCH (func)-[:USES]->(params:CodeEntity)
- Agora começo a montar o retorno da consulta com os dados da classe:
RETURN codeEntity.packageName AS packageName, codeEntity.className AS className
- Depois adiciono no retorno os dados dos atributos concatenando com o tipo do atributo e usando collect para não duplicar as linhas caso as classes tenham mais de um método
, collect(field.name + ": "+ coalesce(codeEntityField.className, "N/A")) AS attributes
-Agora adiciono os dados relativo aos métodos das classes concatenando com o tipo do retorno do método
, func.name+": "+coalesce(returnEntity.className, "void") AS functions
- Para finalizar, adiciono os parametros recebicos pelas funções também usando o collect para agrupar os dados para cada função:
, collect(distinct params.className) as parameters

Q: Gere um diagrama de classes com as classes do mesmo pacote da classe Materia. Id da aplicação: 1
R:
Para um diagrama de classes, é preciso buscar o nome da classe, seus atributos e métodos. Mas primeiro devemos consultar o nome do pacote da classe Materia
- Primeiro inicio a consulta das CodeEntity da aplicação com id 1:
MATCH (app:Application {id: 1})-[:DEPENDS]->(codeEntity:CodeEntity)
- Depois adiciono a filtragem para encontrar a codeEntity com o nome da classe igual a Materia:
WHERE codeEntity.className = "Materia"
- Agora é possível criar a variável com o nome do pacote da classe Materia:
WITH codeEntity.packageName as packageName
- Com o nome do pacote, posso consultar as codeEntity com o mesmo pacote da classe Materia da aplicação com id 1:
OPTIONAL MATCH (app:Application {id: 132})-[:DEPENDS]->(codeEntity:CodeEntity)
WHERE codeEntity.packageName = packageName
- Depois adiciono o match para buscar os atributos e o match para buscar o tipo de cada atributo:
OPTIONAL MATCH (codeEntity)-[:HAS_FIELD]->(field:Field)
OPTIONAL MATCH (field)-[:OF_TYPE]->(codeEntityField:CodeEntity)
- Depois adiciono o match para buscar os métodos das classes. Mesmo não tendo sido pedido, vou ignorar os getters e setters para focar no mais importante:
OPTIONAL MATCH (codeEntity)-[:HAS_FUNCTION]->(func:Function)
WHERE NOT func.name STARTS WITH "get" AND NOT func.name STARTS WITH "set"
- Agora adiciono o match para consultar o tipo de retorno dos métodos:
OPTIONAL MATCH (func)-[:RETURNS]->(returnEntity:CodeEntity)
- E adiono o match para consultar os parâmetros dos métodos:
OPTIONAL MATCH (func)-[:USES]->(params:CodeEntity)
- Agora começo a montar o retorno da consulta com os dados da classe:
RETURN codeEntity.packageName AS packageName, codeEntity.className AS className
- Depois adiciono no retorno os dados dos atributos concatenando com o tipo do atributo e usando collect para não duplicar as linhas caso as classes tenham mais de um método
, collect(field.name + ": "+ coalesce(codeEntityField.className, "N/A")) AS attributes
-Agora adiciono os dados relativo aos métodos das classes concatenando com o tipo do retorno do método
, func.name+": "+coalesce(returnEntity.className, "void") AS functions
- Para finalizar, adiciono os parametros recebicos pelas funções também usando o collect para agrupar os dados para cada função:
, collect(distinct params.className) as parameters



');