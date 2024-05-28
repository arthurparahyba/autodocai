INSERT INTO prompt_spec (id, name, content) VALUES (
10,
'format-list-of-application-attributes',
'
Um sistema lê um arquivo de código fonte que tem uma classe e busca os atributos que de tipos que foram implementados no mesmo sistema. Ele apresenta o nome da classe lida e seu pacote e logo a seguir ele lista os atributos encontrados.
Segue um exemplo do texto gerado:
### Texto
A: Primeiro é preciso identificar o pacote que corresponde à aplicação da classe Item:
- O pacote da classe é br.com.mysystem.module1.model e o do sistema é br.com.mysystem
Depois é preciso identificar quais atributos estão dentro do pacote br.com.mysystem:
Atributos Encontrados:
Nenhum atributo encontrado
### Fim Texto

Neste texto nenhum atributo foi encontrado, mas quando atributos são encontrados eles são listados. Segue um exemplo de texto que lista atributos encontrados:
### Texto
A: Primeiro é preciso identificar o pacote que corresponde à aplicação da classe Application:
- O pacote da classe é br.com.app.model e o do sistema é br.com.app
Depois é preciso identificar quais atributos estão dentro do pacote br.com.mysystem:
Atributos Encontrados:
- Item do pacote br.com.app.fin.model
- Section do pacote br.com.app.fin.model
### Fim Texto

O objetivo é identificar no texto fornecido quais são os atributos encontrados e criar uma lista de itens com título `###Atributos Da Aplicação`. Dado o texto a seguir, quais atributos foram encontrados?
### Texto
{beforeOutput}
### Fim Texto

');