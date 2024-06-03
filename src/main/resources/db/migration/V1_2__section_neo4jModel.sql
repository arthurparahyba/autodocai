INSERT INTO section (id, name, content)
VALUES (
1,
'neo4jModel',
'
== Informações sobre o banco de dados:
O banco de dados contém informações sobre classes, atributos, métodos etc do código fonte de uma aplicação.

== Estrutura do Banco de Dados:
- **Application**: (Representa a aplicação. É um agregador onde as classes (CodeEntity) são associadas de forma que seja possível saber de qual aplicação é aquele CodeEntity).
- Propriedades:
  - `id`: Long – ID gerado automaticamente.
  - `name`: String – Nome da aplicação.
- Relações:
  - `DEPENDS`: Conecta uma Application a uma lista de CodeEntity. (OUTGOING)
  - `HAS_LAYER`: Conecta uma Application a uma lista de CodeEntity. (OUTGOING)
  - `HAS_MODULE`: Conecta uma Application a uma lista de Module. (OUTGOING)

- **CodeEntity**: (Representa uma classe do código fonte da aplicação)
- Propriedades:
  - `id`: String.
  - `className`: String – Nome da classe.
  - `packageName`: String – Nome do pacote.
- Relações:
  - `HAS_FUNCTION`: Conecta uma CodeEntity a uma lista de Function. (OUTGOING)
  - `HAS_FIELD`: Conecta uma CodeEntity a uma lista de Field. (OUTGOING)
  - `BELONGS_TO_LAYER`: Conecta uma CondeEntity a uma Layer. (INCOMING)
  - `BELONGS_TO_MODULE`: Conecta uma CondeEntity a um Module. (INCOMING)

- **Function**: (Representa um método de uma classe)
- Propriedades:
  - `id`: String.
  - `name`: String – Nome da função.
- Relações:
  - `USES`: Conecta uma Function a uma lista de CodeEntity. Estas CodeEntity representam os parametros recebidos pela função.
  - `CALL`: Conecta uma Function a uma lista de outras Function. Esta lista representa as chamadas que ela faz a outras funções na sua implementação.
  - `RETURNS`: Quando uma Function retorna um valor, esta relação representa o tipo de dado retornado pela Function.

- **Field**: (Representa um atributo de uma classe)
- Propriedades:
  - `name`: String – Nome do campo.
- Relações:
  - `OF_TYPE`: Conecta um Field a uma CodeEntity. Esta relação indica o tipo associado a este Field

- **Layer**: (Representa as camadas de uma aplicação)
- Propriedades:
  - `id`: String - id da Layer
  - `name`: String - Nome da Layer. A Layer representa as possíveis camadas de uma aplicação e os valores permitidos neste campo são: PRESENTATION, APPLICATION, PERSISTENCE, SERVICE, INFRASTRUCTURE e TEST.
- Relações:
  - `BELONGS_TO_LAYER`: Conecta uma Layer a uma lista de CodeEntity. Esta lista representa as CodeEntity que fazem parte desta Layer. (OUTGOING)

- **Module**: (Representa os módulos de uma aplicação)
- Propriedades:
  - `id`: String - id do Módulo
  - `name`: String - Nome do Módulo.
- Relações:
  - `BELONGS_TO_MODULE`: Conecta um Module a uma lista de CodeEntity. Esta lista representa as CodeEntity que fazem parte deste Module. (OUTGOING)
==Fim Estrutura do Banco
');