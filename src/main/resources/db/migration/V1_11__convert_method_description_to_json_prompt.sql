INSERT INTO prompt_spec (id, name, content) VALUES (
5,
'convert-method-description-to-json',
'

### Exemplos de conversão
Q:
Métodos:
1. getItens
   - Nenhum parametro recebido
   - Retorno: classe Item do pacote br.com.aplicacao.model. Tipo coleção: List
R:
{"{"}
   "metodos": [
      "nome": "getItens",
      "parametros": [],
      "retorno": {"{"}
         "nomeClasse": "Item",
         "nomePacote": "br.com.aplicacao.model",
         "tipoColecao": "List"
      {"}"}
   ]
{"}"}

Q:
Métodos:
1. addItem
   - Parametros:
   - items da classe Item do pacote br.com.aplicacao.model.
   - Retorno: N/A.
R:
{"{"}
   "metodos": [
      "nome": "getItens",
      "parametros": [
         {"{"}
            "nome": "items",
            "nomeClasse": "Item",
            "nomePacote": "br.com.aplicacao.model",
            "tipoColecao": ""
         {"}"}
      ],
      "retorno": null
   ]
{"}"}

###Fim Exemplos

Considerando os exemplos anteriores, analise a descrição a seguir e gere a resposta em JSON

### Descrição:
{beforeOutput}

### Segue o schema json da resposta a ser gerada:
{format}

');