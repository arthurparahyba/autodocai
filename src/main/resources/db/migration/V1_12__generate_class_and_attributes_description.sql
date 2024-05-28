INSERT INTO prompt_spec (id, name, content) VALUES (
6,
'generate-class-and-attributes-description',
'
Preciso gerar um detalhamento a partir de um determinado código de uma aplicação extraindo as informações sobre a classe e seus atributos. Se o código contiver atributos dentro de uma coleção, identifique o tipo da coleção e detalhe a classe do elemento dentro da coleção e seu pacote.

### Exemplos de detalhamento
Q: Qual o nome da classe, seu pacote e os atributos?
package com.mysystem.model;
import java.util.List;
import com.mysystem.productmodule.Product;
public class Item {"{"}
    private String name;
    private List<HistoryValue> historyValues;
    private List<Item> relatedItems;
    private Product product;

    public Item(String name) {"{"}
       this.name = name;
    {"}"}

    public void executa() {"{"}
       System.out.println("Apenas uma execução");
    {"}"}
{"}"}
R: Classe:  Item do pacote com.mysystem.model
Atributos:
- String name do pacote java.lang
- Lista de HistoryValue historyValues do pacote com.mysystem.model
- List de Item relatedItems do pacote com.mysystem.model
- Product product do pacote com.mysystem.productmodule

Q: Qual o nome da classe, seu pacote e os atributos?
package com.mysystem.mdule1;
import java.util.List;
public interface MyInterfaceDefinition {"{"}
    String process(Param para1);
    List<Item> findItemsByName(String name);
{"}"}
R: Classe:  MyInterfaceDefinition do pacote  com.mysystem.mdule1
Atributos:
Não possui atributos
### Fim Exemplos

Q: Qual o nome da classe, seu pacote e os atributos?
{code}

');