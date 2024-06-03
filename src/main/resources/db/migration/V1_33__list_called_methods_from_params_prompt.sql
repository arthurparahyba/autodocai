INSERT INTO prompt_spec (id, name, content) VALUES (
23,
'list-called-methods-from-params',
'
O objetivo é saber como cada método do Codigo está chamando os métodos de seus parametros.

###Exemplos
{calledParameterMethodsExampleSection}
###Fim Exemplos

De acordo com os exemplos anteriores, Identifique se os métodos da lista a seguir tem seus parametros utilizados na sessão Codigo:
{beforeOutput}

**Codigo**:
{code}

');
