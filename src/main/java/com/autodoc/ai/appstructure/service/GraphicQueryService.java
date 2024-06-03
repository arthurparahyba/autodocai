package com.autodoc.ai.appstructure.service;

import com.autodoc.ai.appstructure.to.MermaidResponse;
import com.autodoc.ai.appstructure.to.SequenceDiagramMethod;
import com.autodoc.ai.project.service.MessageService;
import com.autodoc.ai.promptmanager.builder.PromptBuilderFactory;
import com.autodoc.ai.promptmanager.model.PromptChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GraphicQueryService {

    private static final Logger logger = LoggerFactory.getLogger(GraphicQueryService.class);

    @Autowired
    private PromptChain<MermaidResponse> mermaidResponsePromptChain;

    @Autowired
    private PromptBuilderFactory promptBuilderFactory;

    @Autowired
    private MessageService messageService;

    public void processSequenceDiagram(String userMessage, Long applicationId) {
        var tagMessage = MessageService.createTag();
        try {
            //valida se é possível identificar a classe e o método a partir do qual o diagrama de sequencia será criado
            var sequenceDiagramMethod = promptBuilderFactory.builder()
                    .toPrompt("identify-method-for-sequence-diagram", "userMessage")
                    .build(SequenceDiagramMethod.class)
                    .execute(Map.of("userMessage", userMessage));

            if (!sequenceDiagramMethod.isValid()) {
                //Se não for encontrado, gera uma explicação em texto sobre o pedido do usuário informando que ele deve ser mais específico com relação ao qual método e classe o diagrama de sequencia será criado
                //FIM
                messageService.send(applicationId, tagMessage.toTextMessage("Não foi possível identificar qual o método e classe a partir do qual o diagrama de sequencia será gerado"));
                return;
            }

        } finally {
            messageService.send(applicationId, tagMessage.toTextMessage("[END]"));
        }



        //Busca os dados da sequencia usando uma consulta específica para isso que traz os método e as classes chamadas a partir do método informado pelo usuário
        //Gera o esqueleto do diagrama de sequencia em Mermaid
        //Busca a documentação dos métodos chamados pelo método inicial e atualiza o diagrama. Executa este processo recursivamente até o final
        //FIM: Retorna o diagrama
    }

    public void process(String userMessage, Long applicationId) {
        var tagMessage = MessageService.createTag();
        try {
            messageService.send(applicationId, tagMessage);

            var response = mermaidResponsePromptChain
                    .execute(Map.of("applicationId", applicationId.toString(), "userMessage", userMessage));

            logger.info("Código Mermaid gerado: \n%s".formatted(response.mermaidText()));
            messageService.send(applicationId, tagMessage.toGraphicMessage(response.mermaidText()));

        }catch(Exception e) {
            logger.error("Erro no processamento do gráfico", e);
            messageService.send(applicationId, tagMessage.toTextMessage("Erro no processamento do gráfico "));
            messageService.send(applicationId, tagMessage.toTextMessage("[END]"));

        }

    }

}
