INSERT INTO prompt_spec (id, name, content) VALUES (
8,
'extract-attributes-from-code',
'
O objetivo é criar uma lista de atributos encontrados em um determinado código fonte.

### Exemplos
Q: Quais os atributos do código a seguir?
package com.mysystem.model;
import java.util.List;
public class Item {"{"}
    private String name;
    private List<HistoryValue> historyValues;
    private List<Item> relatedItems;
{"}"}
A: Atributos da classe com.mysystem.model.Item:
String name do pacote java.lang
List<HistoryValue> historyValues do pacote java.util
List<Item> relatedItems do pacote java.util

Q: Quais os atributos do código a seguir?
package com.mysystem.mdule1;
import java.util.List;
public interface MyInterfaceDefinition {"{"}
    String process(Param para1);
    List<Item> findItemsByName(String name);
{"}"}
A: Atributos da classe com.mysystem.mdule1.MyInterfaceDefinition :
Nenhum atributo encontrado
### Fim Exemplos

Dado os exemplos anteriores, execute o que é pedido abaixo:

Q: Quais os atributos do código a seguir?
{code}

A:
');