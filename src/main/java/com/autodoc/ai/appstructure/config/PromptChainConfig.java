package com.autodoc.ai.appstructure.config;

import com.autodoc.ai.appstructure.to.*;
import com.autodoc.ai.appstructure.to.MermaidResponse;
import com.autodoc.ai.appstructure.to.ModuleList;
import com.autodoc.ai.promptmanager.builder.PromptBuilderFactory;
import com.autodoc.ai.promptmanager.model.ParallelPromptChain;
import com.autodoc.ai.promptmanager.model.ParallelPromptChainImpl;
import com.autodoc.ai.promptmanager.model.PromptChain;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class PromptChainConfig {

    @Bean
    public PromptChain<ClassInfo> promptChainClassInfo(PromptBuilderFactory promptBuilderFactory) {
        return promptBuilderFactory.builder()
                .toPrompt("generate-class-and-attributes-description", "code")
                .toPrompt("convert-classinfo-description-to-json")
                .build(ClassInfo.class);
    }

    @Bean
    public PromptChain<MethodCallsList> promptChainMethodCallsList(PromptBuilderFactory promptBuilderFactory) {
        var defaultResponse = new MethodCallsList(new ArrayList<>());
        return promptBuilderFactory.builder()
                .toPrompt("extract-attributes-from-code", "code")
                .toPromptValidator("extract-attributes-from-code-validation", defaultResponse)
                .toPrompt("select-application-attributes")
                .toPrompt("format-list-of-application-attributes")
                .toPromptValidator("extract-attributes-from-code-validation", defaultResponse)
                .toPrompt("list-methods-and-called-attributes-methods", "code")
                .build(MethodCallsList.class);
    }

    @Bean
    public PromptChain<MethodParameterCallsList> promptChainParamMethodCallsList(PromptBuilderFactory promptBuilderFactory) {
        var defaultResponse = new MethodCallsList(new ArrayList<>());
        return promptBuilderFactory.builder()
                .toPrompt("extract-methods-with-params-from-code", "code")
                .toPrompt("select-methods-with-application-params")
                .toPrompt("list-selected-methods")
                .toPrompt("list-called-methods-from-params", "code")
//                .toPrompt("fill-parameters-classes-with-package-name", "code")
                .toPrompt("convert-parameters-method-calls-to-json-prompt")
                .build(MethodParameterCallsList.class);
    }

    @Bean
    public PromptChain<MethodParameters> promptChainMethodParameters(PromptBuilderFactory promptBuilderFactory) {
        return promptBuilderFactory.builder()
                .toPrompt("generate-methods-description", "code")
                .toPrompt("convert-method-description-to-json")
                .build(MethodParameters.class);
    }

    @Bean
    public PromptChain<ClassLayerInfo> promptChainClassLayerInfo(PromptBuilderFactory promptBuilderFactory) {
        return promptBuilderFactory.builder()
                .toPrompt("extract-application-layer-category", "code")
                .build(ClassLayerInfo.class);
    }

    @Bean
    @Qualifier("parallelPromptChainCodeEntity")
    public ParallelPromptChain parallelPromptChainCodeEntity(PromptChain<ClassInfo> classInfoPromptChain,
                                                             PromptChain<MethodCallsList> methodCallsListPromptChain,
                                                             PromptChain<MethodParameters> methodParametersPromptChain,
                                                             PromptChain<MethodParameterCallsList> methodParameterCallsListPromptChain,
                                                             PromptChain<ClassLayerInfo> classLayerInfoPromptChain) {
        return new ParallelPromptChainImpl(classInfoPromptChain, methodCallsListPromptChain, methodParametersPromptChain, methodParameterCallsListPromptChain, classLayerInfoPromptChain);
    }

    @Bean
    public PromptChain<ModuleList> promptChainModuleList(PromptBuilderFactory promptBuilderFactory) {
        return promptBuilderFactory.builder()
                .toPrompt("generate-modules-by-classes", "classes")
                .build(ModuleList.class);
    }

    @Bean
    public PromptChain<MermaidResponse> promptChainMermaid(PromptBuilderFactory promptBuilderFactory) {
        return promptBuilderFactory.builder()
                .toPrompt("generate-diagram-query-description", "applicationId", "userMessage")
                .toPrompt("generate-cypher-from-description")
                .toPrompt("generate-mermaid-diagram")
                .build(MermaidResponse.class);
    }
}
