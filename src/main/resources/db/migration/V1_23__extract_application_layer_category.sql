INSERT INTO prompt_spec (id, name, content) VALUES (
13,
'extract-application-layer-category',
'
Execute a verificação de um código fonte fornecido a seguir. Essa verificação tem o único objetivo de identificar qual camada da arquitetura o código pertence de acordo com as funcionalidades implementadas. Considere as seguintes camadas:
- Presentation (ou User Interface): Responsável pela interação direta com o usuário final.
- Application (ou Business Logic): Contém a lógica de negócios da aplicação. Responsável por processar dados e aplicar regras de negócio.
- Persistence (ou Data Access): Responsável pelo armazenamento e recuperação de dados. Pode incluir acesso a bancos de dados, sistemas de arquivos, serviços web, entre outros
- Service (ou Business Layer): Fornece serviços ou funcionalidades específicas para outras partes do sistema. Encapsula a lógica de negócios complexa
- Infrastructure (ou External Access Layer): Responsável por interagir com sistemas externos, como APIs de terceiros, serviços web, sistemas legados, etc. Fornece abstrações para comunicação de rede, manipulação de arquivos, autenticação, etc.
- Test: Responsável por verificar e validar o comportamento correto da aplicação. Garantir que as funcionalidades implementadas atendam aos requisitos especificados
###Codigo
{code}

### Exemplos de resposta:
**Quando o código pertence a camada de aplicação**:
{"{"}
  "layer": "APPLICATION"
{"}"}
**Quando o código pertence a camada de apresentação**:
{"{"}
  "layer": "PRESENTATION"
{"}"}


### Segue o schema json da resposta a ser gerada:
{format}

');