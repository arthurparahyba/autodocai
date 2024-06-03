INSERT INTO section (id, name, content)
VALUES (
7,
'calledParameterMethodsExampleSection',
'
Q: Identifique se os métodos da lista a seguir tem seus parametros utilizados na sessão Codigo:
- public void addItem(Item item)

**Codigo**:
import br.com.example.model.Item;
class Section {
   private List<Item> itens;
   public void addItem(Item item){
      if(item.isValid()){
         save(item);
         this.itens.add(item);
      }
      if(item.isNew()) {
         System.out.println("novo produto adicionado");
      }
   }
   public removeItem(Item item) {
      this.itens.remove(item);
   }
}

R:
**Método addItem(Item item)**
1. Validando os parametros do método addItem
- O método recebe o parametro Item item.
2. Agora é preciso verificar quais métodos apenas destes parâmetros são utlizados.
3. Agora é preciso pesquisar os métodos que são chamados da classe Item (br.com.example.model)
- Método isValid
- Método isNew
4. Agora é preciso identificar o pacote de cada classe

Q: Identifique se os métodos da lista a seguir tem seus parametros utilizados na sessão Codigo:
- public void addToCart(Product product, PriceUpdate priceUpdate)

**Codigo**:
import br.com.example.model.Product;
mport br.com.example.service.PriceUpdate;
class Cart{
   private List<Product> products;
   private Double totalPrice;

   public void addToCart(Product product, PriceUpdate priceUpdate){
         products.add(product)
         if(product.isNotFree){
            this.totalPrice = priceUpdate.calculatePrice(this.products);
        }
   }

   public List<Product> getProducts() {
      return this.products;
   }
}

R:
**Método addToCart(Product product, PriceUpdate priceUpdate)**
1. Validando os parametros do método addToCart:
- O método recebe o parametro Product product e PriceUpdate priceUpdate.
2. Agora é preciso verificar quais métodos apenas das classes destes parâmetros são utlizados.
3. Agora é preciso pesquisar os métodos que são chamados da classe Product (br.com.example.model):
- Método isNotFree
4. Agora é preciso pesquisar os métodos que são chamados da classe PriceUpdate (br.com.example.service):
- Método calculatePrice
');

INSERT INTO prompt_spec_sections (prompts_id, sections_id) VALUES (23, 7);