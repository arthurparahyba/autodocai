INSERT INTO section (id, name, content)
VALUES (
4,
'methodDescriptionExamples',
'
### Exemplos de como descrever parametros
Considerando que a classe Item está no pacote br.com.mysystem
Q: descreva o parâmetro: Item item
R: item da classe Item do pacote br.com.mysystem
Q: descreva o parâmetro List<Item> items
R: items da classe Item do pacote br.com.mysystem. Tipo coleção List
Q: descreva o parâmetro Item[] items
R: items da classe Item do pacote br.com.mysystem. Tipo coleção Array
Q: descreva o parâmetro Map<String, Item> items
R: items da classe Item do pacote br.com.mysystem. Tipo coleção Map
Q: descreva o parâmetro String name
R: name da classe String do pacote java.lang


### Exemplos de Resposta Esperada
Aqui estão alguns exemplos de respostas esperadas para diferentes tipos de métodos:
**Método sem parâmetros e com retorno de coleção:**
import br.com.aplicacao.model.Item;
class ItemRepository {
    public List<Item> getItens() {
        return this.itens;
    }
}
Retorno esperado:
Métodos:
1. getItens
   - Nenhum parametro recebido
   - Retorno: classe Item do pacote br.com.aplicacao.model. Tipo coleção: List

**Método sem parâmetros e com retorno de um elemento:**
import br.com.aplicacao.model.Item;
class ItemRepository {
    private List<Item> items;

    public Item getILasttem() {
        return items.get(items.size() - 1);
    }
}
Retorno esperado:
Métodos:
1. getILasttem
   - Nenhum parametro recebido
   - Retorno: classe Item do pacote br.com.aplicacao.model.

**Método com parâmetros e com retorno de um elemento:**
import br.com.aplicacao.model.Item;
class ItemRepository {
    public Item getILasttem(List<Item> items) {
        return items.get(items.size() - 1);
    }
}
Retorno esperado:
Métodos:
1. getILasttem
   - Parametros:
   - items da classe Item do pacote br.com.aplicacao.model. Tipo coleção: List
   - Retorno: classe Item do pacote br.com.aplicacao.model.

**Método com parâmetros e sem retorno:**
import br.com.aplicacao.model.Item;
class ItemRepository {
    private List<Item> items;
    public void addItem(Item item) {
        items.add(item);
    }
}
Retorno esperado:
Métodos:
1. addItem
   - Parametros:
   - items da classe Item do pacote br.com.aplicacao.model.
   - Retorno: N/A.

### Fim Exemplos
'
);