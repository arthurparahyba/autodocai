//package com.autodoc.ai.appstructure.prompt;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.ai.chat.ChatClient;
//import org.springframework.ai.chat.Generation;
//import org.springframework.ai.chat.messages.Message;
//import org.springframework.ai.chat.messages.SystemMessage;
//import org.springframework.ai.chat.messages.UserMessage;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.chat.prompt.PromptTemplate;
//import org.springframework.ai.openai.OpenAiChatOptions;
//import org.springframework.ai.parser.BeanOutputParser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class GenerateCypherQueryPrompt {
//
//    private static final Logger logger = LoggerFactory.getLogger(GenerateCypherQueryPrompt.class);
//
//    @Autowired
//    private ChatClient chatClient;
//
//    public CipherResponse process(String question, Long appId) {
//
//        var promptText = """
//              Você é especializado em geração de códigos Cypher para o banco de dados Neo4j.
//
//              O banco de dados armazena a estrutura do código de aplicações, incluindo suas classes, métodos, atributos e relacionamentos. Você precisa entender a modelagem do banco de dados Neo4j para criar queries Cypher que extraiam os dados corretos e possam ser usados para gerar diagramas.
//
//              **Objetivo**: Gere a consulta Cypher correta para extrair dados específicos para a criação do diagrama desejado de acordo com a mensagem do usuário.
//              Os dados devem refletir a estrutura de classes armazenada no banco de dados Neo4j. O id da aplicação no banco de dados é {applicationId}
//
//              **Estrutura do Banco de Dados (Modelagem Neo4j)**:
//
//              - **Application**:
//                - Propriedades:
//                  - `id`: Long – ID gerado automaticamente.
//                  - `name`: String – Nome da aplicação.
//                - Relações:
//                  - `DEPENDS`: Conecta uma Application a uma lista de CodeEntity.
//
//              - **CodeEntity**:
//                - Propriedades:
//                  - `id`: Long – ID gerado automaticamente.
//                  - `className`: String – Nome da classe.
//                  - `packageName`: String – Nome do pacote.
//                - Relações:
//                  - `DEPENDS`: Conecta uma CodeEntity a uma lista de outras CodeEntity.
//                  - `HAS_FUNCTION`: Conecta uma CodeEntity a uma lista de Function.
//                  - `HAS_FIELD`: Conecta uma CodeEntity a uma lista de Field.
//
//              - **Function**:
//                - Propriedades:
//                  - `id`: Long – ID gerado automaticamente.
//                  - `name`: String – Nome da função.
//                - Relações:
//                  - `USES`: Conecta uma Function a uma lista de CodeEntity.
//
//              - **Field**:
//                - Propriedades:
//                  - `name`: String – Nome do campo.
//                - Relações:
//                  - `OF_TYPE`: Conecta um Field a uma CodeEntity.
//
//              **Exemplos de Queries Cypher**:
//
//              - **Exemplo 1: Diagrama de Classes apenas com os atributos das classes**:
//              ```cypher
//              MATCH (app:Application {"{"}id: 1{"}"})-[:DEPENDS]->(ce:CodeEntity)
//              OPTIONAL MATCH (ce)-[:HAS_FIELD]->(field:Field)
//              RETURN
//                  app.id AS applicationId,
//                  app.name AS applicationName,
//                  ce.id AS codeEntityId,
//                  ce.className AS className,
//                  ce.packageName AS packageName,
//                  collect(field.name) AS fields
//              ORDER BY ce.id
//
//              ```
//              - **Exemplo 2: gere um diagrama de classes com os métodos das classes da aplicaçãos**:
//              ```cypher
//              MATCH (app:Application {"{"}id: 1{"}"})-[:DEPENDS]->(ce:CodeEntity)
//              OPTIONAL MATCH (ce)-[:HAS_FUNCTION]->(f:Function)
//              RETURN
//                  app.id AS applicationId,
//                  app.name AS applicationName,
//                  ce.id AS codeEntityId,
//                  ce.className AS className,
//                  ce.packageName AS packageName,
//                  collect(f.name) AS functions
//              ORDER BY ce.id
//              ```
//
//              - **Exemplo 3: gere um diagrama de classes que mostra as relações entre as classes**:
//              ```cypher
//              MATCH (app:Application {"{"}id: 1{"}"})-[:DEPENDS]->(ce:CodeEntity)
//              OPTIONAL MATCH (ce)-[:DEPENDS]->(related:CodeEntity)
//              RETURN
//                  app.id AS applicationId,
//                  app.name AS applicationName,
//                  ce.id AS codeEntityId,
//                  ce.className AS className,
//                  ce.packageName AS packageName,
//                  collect({"{"}
//                      id: related.id,\s
//                      className: related.className,\s
//                      packageName: related.packageName
//                  {"}"}) AS relatedClasses
//              ORDER BY ce.id
//
//
//              Ação Esperada:
//              1. Entenda a Solicitação do Usuário: Compreenda o tipo de diagrama solicitado na mensagem do usuário.
//              2. Gere a Consulta Cypher: Crie uma query Cypher apropriada que extraia os dados corretos do banco de dados Neo4j de acordo com a estrutura fornecida, que serão necessários para gerar o diagrama em Mermaid em um momento posterior.
//              3. Gere uma explicação sobre cada coluna gerada pela consulta nos dados que serão gerados.
//
//
//              {format}
//
//            """;
//
//
//        var outputParser = new BeanOutputParser<>(CipherResponse.class);
//
//        PromptTemplate promptTemplate = new PromptTemplate(promptText, Map.of("applicationId", appId, "format", outputParser.getFormat()));
//        Prompt systemPrompt = promptTemplate.create();
//
//        var options = OpenAiChatOptions.builder()
//                .withModel("gpt-4-turbo")
//                //.withTemperature(0.4f)
//                .build();
//
//        Message systemMessage = new SystemMessage(systemPrompt.getInstructions().get(0).getContent());
//        Message userMessage = new UserMessage(question);
//
//        Prompt prompt = new Prompt(
//                List.of(userMessage, systemMessage)
//                //, options
//        );
//
//        Generation generation = chatClient.call(prompt).getResult();
//        return outputParser.parse(generation.getOutput().getContent());
//    }
//
//    public record CipherResponse(List<String> columDescription, String cipherQuery) {};
//
//}
