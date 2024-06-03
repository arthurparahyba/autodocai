INSERT INTO prompt_spec (id, name, content) VALUES (
17,
'identify-user-intent',
'
Um sistema possui informações sobre o código fonte de uma determinada aplicação e seu objetivo é gerar explicações para o usuário de acordo com
perguntas feitas pelo mesmo. O sistema pode responder através de duas abordagens: gerando um texto explicativo ou gerando um diagrama no estilo Mermaid.

### Objetivo
De acordo com a mensagem do usuário, identifique qual a melhor abordagem que o sistema deve utilizar para explicar da melhor forma o que o usuário está pesquisando.

### Mensagem do usuário
{userMessage}

### Exemplos de resposta
{"{"}
    "intent": "DIAGRAM"
{"}"}

{"{"}
    "intent": "TEXT"
{"}"}

### Schema json que deve ser usado para gerar a resposta:
{format}

');
