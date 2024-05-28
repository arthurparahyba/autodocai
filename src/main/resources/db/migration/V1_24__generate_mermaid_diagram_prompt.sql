INSERT INTO prompt_spec (id, name, content) VALUES (
14,
'generate-mermaid-diagram',
'
Você é especialista em geração de código Mermaid. De acordo com os exemplos de diagrama mermaid e o dados fornecidos a seguir, gere um diagrama
de acordo com o que foi pedido pelo usuário.

{mermaidExamples}

### Segue os dados que devem ser utilizados para gerar o gráfico de acordo com o que foi pedido pelo usuário.
{data}

{format}

');

INSERT INTO prompt_spec_sections (prompts_id, sections_id) VALUES (14, 2);
