INSERT INTO section (id, name, content)
VALUES (
6,
'calledMethodExample',
'
### Exemplos de como executar a ação:
Exemplo 1:
###Atributos Da Aplicação
- Application do pacote br.com.app.fin.model

### Código
package br.com.escola.financeiro;
import br.com.escola.model.Application
class Financeiro {
    private String name;
    private Application app;

    public void cadastrar(Aluno aluno) {
        if(app.valida(aluno){
            app.registra(aluno);
        }
    }
}

**Segue um exemplo de resposta**
{
    "metodos":[
    {
        "nomeMetodo": "cadastrar",
        "chamadas": [
        {
            "nomeMetodoChamado": "valida",
            "nomeClasse": "Application",
            "nomePacote": "br.com.app.fin.model"
        },
        {
            "nomeMetodoChamado": "registra",
            "nomeClasse": "Application",
            "nomePacote": "br.com.app.fin.model"
        }
        ]
    }
    ]
}

Exemplo 2:
###Atributos Da Aplicação
- Item do pacote br.com.mysystem.model

### Codigo
package br.com.mysystem;
import br.com.mysystem.model.Item
class Validate{
    private String name;
    private Item item;

    public void execute() {
        if(name.isValid(){
            return true;
        }
    }
}

**Segue um exemplo de resposta**
{
    "metodos": []
}
### Fim Exemplos
');