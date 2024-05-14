INSERT INTO section (id, name, content)
VALUES (
1,
'neo4jModel',
'
1. O banco de dados contém informações sobre classes, atributos, métodos etc de uma aplicação.
== Estrutura do Banco de Dados onde serão feitas as consultas para extrair os dados da `aplicação` (Modelagem Neo4j):
- **Application**: (Representa a aplicação. Ela não é uma classe que pertence ao código, mas apenas um agregador das classes da aplicação. Ela nunca deve ser considerada nos códigos Mermaid)
- Propriedades:
  - `id`: Long – ID gerado automaticamente.
  - `name`: String – Nome da aplicação.
- Relações:
  - `DEPENDS`: Conecta uma Application a uma lista de CodeEntity.

- **CodeEntity**:
- Propriedades:
  - `id`: Long – ID gerado automaticamente.
  - `className`: String – Nome da classe.
  - `packageName`: String – Nome do pacote.
- Relações:
  - `DEPENDS`: Conecta uma CodeEntity a uma lista de outras CodeEntity.
  - `HAS_FUNCTION`: Conecta uma CodeEntity a uma lista de Function.
  - `HAS_FIELD`: Conecta uma CodeEntity a uma lista de Field.

- **Function**:
- Propriedades:
  - `id`: Long – ID gerado automaticamente.
  - `name`: String – Nome da função.
- Relações:
  - `USES`: Conecta uma Function a uma lista de CodeEntity. Estas CodeEntity representam os parametros recebidos pela função.

- **Field**:
- Propriedades:
  - `name`: String – Nome do campo.
- Relações:
  - `OF_TYPE`: Conecta um Field a uma CodeEntity.
==Fim Estrutura do Banco
');