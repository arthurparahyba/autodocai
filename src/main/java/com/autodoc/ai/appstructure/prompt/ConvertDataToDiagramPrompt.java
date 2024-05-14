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
//import java.util.stream.Collectors;
//
//@Component
//public class ConvertDataToDiagramPrompt {
//
//    private static final Logger logger = LoggerFactory.getLogger(ConvertDataToDiagramPrompt.class);
//
//    @Autowired
//    private ChatClient chatClient;
//
//    public MermaidResponse process(String question, String databaseData, List<String> columnDescription, Long appId) {
//
//        var promptText = """
//              Você é especialista em converter dados formatados no estilo csv em código Mermaid.
//
//              ### Ação Esperada:
//                - **Dados a serem utilizados**: Os dados que serão usados para gerar o código Mermaid com as informações do sistema estão na seção **DATA**. Estes dados vêm com cabeçalho que deve ser usado para interpretar o que tem em cada linha e coluna.
//                -- **Interpretação dos Dados do Cabeçalho**:
//                    {columnDescription}
//
//                - Converter para Mermaid: Com base nos dados fornecidos na seção **DATA** gere o código Mermaid seguindo estritamente o formato Mermaid, de acordo com o tipo do gráfico solicitado pelo usuário.
//                Se os dados da seção **DATA** não contiverem informações suficientes para cumprir com o pedido do usuário, gere um gráfico simplificado, mas sempre com as informações disponíveis da seção **DATA**. **É crucial não inventar informações no código Marmaid que não existem nos dados fornecidos**. Caso não existam dados disponíveis, não gere nada.
//
//              == Instruções:
//              - A seção **EXEMPLOS** fornece exemplos de código Mermaid para que você possa entender melhor o formato esperado.
//              - A seção **DATA** fornece os dados que devem ser usados para gerar o código Mermaid.
//
//              **DATA**:
//              {databaseData}
//              **FIM DOS DADOS
//
//              **EXEMPLOS**
//              {examples}
//              **FIM DOS EXEMPLOS
//
//              ** Format:
//              {format}
//
//            """;
//
//        var examples = """
//                **class diagram 1**
//                classDiagram
//                    class Shape {
//                        +String color
//                        +void draw()
//                    }
//                    class Circle {
//                        -double radius
//                        +double getArea()
//                    }
//                    class Rectangle {
//                        -double width
//                        -double height
//                        +double getArea()
//                    }
//                    class Square {
//                        -double side
//                        +double getArea()
//                    }
//                    class Drawing {
//                        -List<Shape> shapes
//                        +void addShape(Shape shape)
//                        +void drawAllShapes()
//                    }
//
//                    Shape <|-- Circle
//                    Shape <|-- Rectangle
//                    Shape <|-- Square
//
//                    Drawing "1" *-- "*" Shape : contains
//
//                    Circle ..> Drawing : uses
//                    Rectangle ..> Drawing : uses
//                    Square ..> Drawing : uses
//
//                **sequence diagram 1**
//                sequenceDiagram
//                    participant User
//                    participant System
//                    participant Database
//
//                    User->>System: Request Data
//                    activate System
//                    System->>Database: Fetch Data
//                    activate Database
//                    Database-->>System: Return Data
//                    deactivate Database
//                    System-->>User: Display Data
//                    deactivate System
//
//                """;
//
//        var outputParser = new BeanOutputParser<>(MermaidResponse.class);
//
//        var description = columnDescription.stream().collect(Collectors.joining("\n"));
//        PromptTemplate promptTemplate = new PromptTemplate(promptText, Map.of("databaseData", databaseData, "columnDescription", description, "examples", examples, "format", outputParser.getFormat()));
//        Prompt systemPrompt = promptTemplate.create();
//
//        var options = OpenAiChatOptions.builder()
//                .withModel("gpt-3.5-turbo")
//                .withTemperature(0.30f)
//                .withFrequencyPenalty(0f)
//                .withPresencePenalty(0f)
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
//    public record MermaidResponse(String mermaidText) {};
//
//}
