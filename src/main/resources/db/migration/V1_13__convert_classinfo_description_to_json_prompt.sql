INSERT INTO prompt_spec (id, name, content) VALUES (
7,
'convert-classinfo-description-to-json',
'
Em outro momento foi gerado o detalhamento de um código fonte de uma aplicação. Foi listado o nome da classe e seus atributos. O objetivo agora é gerar uma resposta para o usuário no formato Json utilizando o Schema Json definido a seguir.

### Exemplos
Pedido: Converta o texto para Json.
**Texto**
R: Classe:  Item do pacote com.mysystem.model
Atributos:
- String name do pacote java.lang
- Lista de HistoryValue historyValues do pacote com.mysystem.model
- Lista de Item item do pacote com.mysystem.model
- Product product do pacote com.mysystem.productmodule
**Fim Texto**
Resposta:
{"{"}
  "nomeClasse": "Item",
  "nomePacote": "com.mysystem.model",
  "attributes": [
    {"{"}
      "nomeAtributo": "name",
      "nomeClasse": "String",
      "nomePacote": "java.lang",
      "tipoColecao": ""
    {"}"},
    {"{"}
      "nomeAtributo": "historyValues",
      "nomeClasse": "HistoryValue",
      "nomePacote": "com.mysystem.model",
      "tipoColecao": "Lista"
    {"}"},
    {"{"}
      "nomeAtributo": "item",
      "nomeClasse": "Item",
      "nomePacote": "com.mysystem.model",
      "tipoColecao": "Lista"
    {"}"},
    {"{"}
      "nomeAtributo": "product",
      "nomeClasse": "Product",
      "nomePacote": "com.mysystem.productmodule",
      "tipoColecao": ""
    {"}"}
    ]
{"}"}
### Fim Exemplos

Q: Converta o texto para Json.
**Texto**
{beforeOutput}
**Fim Texto**

### Schema json para gerar a resposta:
{format}

');