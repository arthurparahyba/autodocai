INSERT INTO prompt_spec (id, name, content) VALUES (
21,
'select-methods-with-application-params',
'
Você é especialista em selecionar métodos que recebem parametros cujas classes são implementadas na propria aplicação e não em bibliotecas externas. Dada uma lista de enunciados de métodos, você deve analisar quais recebem parâmetros implementados na aplicação

### Exemplo de como analisar os métodos de acordo com seus enunciados.
Q:
Classe: br.com.exemplo.model.Item
-public Item()
-public Item(String name)
-public Item findBestItem(List<Item> itens)
-public void setName(String name)
-public void update(String name, Long id, List<Section> sections)
-public void save(ItemRepository itemRepository)
R:
- public Item(): Item é um construtor e por isso não é aceito
- public Item(String name): Item é um construtor que recebe uma String. Não é um método e por isso não é aceito
- public Item findBestItem(List<Item> itens): findBestItem é um método que recebe uma List. Este método não é aceito por recebe apenas parametros do tipo coleção (java.utilList)
- public void setName(String name): setName é um método que recebe String. Este método não é aceito, pois apenas recebe parâmetros do tipo default da linguagem (java.lang.String)
- public void update(String name, Long id, List<Section> sections): É um método que recebe String, Long e List. Este método não é aceito pois recebe apenas tipo default da linguagem(java.lang.String e java.lang.Long) e coleção (java.util.List)
- public void save(ItemRepository itemRepository): É um método que recebe ItemRepository . É aceito pois recebe um parâmetro que não é default da linguagem nem coleção### Fim dos exemplos
### Fim dos exemplos

Baseado nos exemplos anteriores, analise os enunciados dos métodos a seguir

{beforeOutput}

Explique passo a passo

');
