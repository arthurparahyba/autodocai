package com.autodoc.ai.promptmanager.service;

import com.autodoc.ai.promptmanager.model.AutodocPromptSpec;
import com.autodoc.ai.promptmanager.repository.AutodocTool;
import com.autodoc.ai.promptmanager.repository.PromptSpec;
import com.autodoc.ai.promptmanager.repository.Section;
import com.autodoc.ai.promptmanager.repository.PromptSpecRepository;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PromptSpecService {

    @Autowired
    private PromptSpecRepository promptSpecRepository;

    public Optional<AutodocPromptSpec> findByName(String name) {
        var specOp = promptSpecRepository.findByName(name);
        if(specOp.isEmpty()) {
            return Optional.empty();
        }
        var spec = specOp.get();

        final Map<String, Object> sections = spec.getSections().stream().collect(Collectors.toUnmodifiableMap(Section::getName, Section::getContent));
        final List<String> functions = spec.getTools().stream().map(AutodocTool::getName).toList();

        var autodocSpec = new AutodocPromptSpec(name, spec.getContent(), sections, functions, OpenAiChatOptions.builder());
        return Optional.of(autodocSpec);
    }
}
