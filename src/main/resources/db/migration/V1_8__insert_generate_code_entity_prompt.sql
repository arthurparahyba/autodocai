INSERT INTO prompt_spec (id, name, content) VALUES (
2,
'generate-code-entity',
'
Por favor, analise a documentação do seguinte arquivo ({filePath}) de código-fonte de uma aplicação e gere uma representação JSON para ele.

### Objetivo:
Dado a documentação de um código na seção **Document**, interprete as informações desta documentação seguindo as intruções da seção **Instruções** e gere um documento json com essas informações.

### Instruções:

**Definições Gerais**:
- **name**: O nome do campo, método ou classe.
- **packageName**: O pacote ao qual o tipo pertence. Regras:
  - **Go**:
    - **Tipos Embutidos**: Use `builtin` para tipos como `string`, `int`, etc.
  - **Python**: Para tipos embutidos como `str` e `int`, use `builtins`.
  - **Java**: Para tipos embutidos como `String`, `List`, etc., use `java.lang` ou `java.util`, etc.
    - **Pacotes Personalizados**: Indique o nome do pacote fornecido no código ou importado.
  - **Tipos com subtipos**: Listas, mapas e etc podem ser usados para definir o tipo de um atributo (ex: List<Produto>) neste caso, deve ser retornado não o pacote de List, mas o pacote de Produto, assim como no nome da classe deve vir Produto.
   - **Classes sem import**: Algumas classes podem não estar sendo importadas, pois podem estar no mesmo pacote que a classe que está sendo lida. Caso a classe não seja um tipo embutido, considere que o nome da classe dela é o mesmo da classe que está sendo lida
** Fim Definições Gerais **

**Tipos**:
- **ClassInfo**:
  - **name (String)**: Nome da classe.
  - **packageName (String)**: Pacote ao qual a classe pertence.
  - **attributes (List<Attribute>)**: Lista de atributos da classe.
  - **methods (List<Method>)**: Lista de métodos.

- **Attribute**:
  - **name (String)**: Nome do atributo.
  - **className (String)**: Nome da classe do tipo do atributo.
  - **packageName (String)**: Pacote ao qual o atributo pertence.

- **Method**:
  - **name (String)**: Nome do método.
  - **params (List<Param>)**: Lista de parâmetros do método, incluindo nome e tipo.
  - **calls (List<Call>)**: Lista de métodos/funções chamados.
  - **returns (Return)**: Tipo de retorno.

- **Param**:
  - **name (String)**: Nome do parâmetro.
  - **className (String)**: Nome da classe do tipo do parâmetro.
  - **packageName (String)**: Pacote ao qual o parâmetro pertence.

- **Call**:
  - **function (String)**: Nome da função chamada.
  - **className (String)**: Nome da classe do tipo da função.
  - **packageName (String)**: Pacote ao qual a classe da função pertence.

- **Dependency**:
  - **className (String)**: Nome da classe do tipo do retorno.
  - **packageName (String)**: Pacote ao qual o tipo retornado pertence.
  - **collectionType** (String): Tipo do retorno caso seja uma coleção. Ex: Map, List etc.
** Fim Tipos **

== Document
{document}
==

{format}

');