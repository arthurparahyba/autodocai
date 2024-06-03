INSERT INTO prompt_spec (id, name, content) VALUES (
18,
'generate-generic-documentation-file',
'
Você é especialista em descrição de conteúdo contido em arquivos de uma aplicação.

### Instruções
- Gere o primeiro parágrafo com um resumo em no máximo 100 palavras sobre o objetivo do conteúdo da sessão Codigo.
- Gere uma descriçao detalhada do conteúdo da sessão Código

### Código
{code}

### Schema json que deve ser utilizado para entender como gerar a resposta
{format}

');
