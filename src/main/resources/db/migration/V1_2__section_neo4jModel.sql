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
  - `DEPENDS`: Conecta uma Application a uma lista de CodeEntity.

- **CodeEntity**: (Representa uma classe do código fonte da aplicação)
- Propriedades:
  - `id`: String.
  - `className`: String – Nome da classe.
  - `packageName`: String – Nome do pacote.
- Relações:
  - `HAS_FUNCTION`: Conecta uma CodeEntity a uma lista de Function.
  - `HAS_FIELD`: Conecta uma CodeEntity a uma lista de Field.
  - `HAS_CODE_ENTITY`: Conecta uma CondeEntity a uma Layer

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
  - `name`: String - Nome da Layer. A Layer representa as possíveis camadas de uma aplicação e os valores permitidos neste campo são: PRESENTATION, APPLICATION, PERSISTENCE, SERVICE, INFRASTRUCTURE e TEST.
- Relações:
  - `HAS_CODE_ENTITY`: Conecta uma Layer a uma lista de CodeEntity. Esta lista representa as CodeEntity que fazem parte desta Layer.

==Fim Estrutura do Banco
');