package com.autodoc.ai.function.config;

import com.autodoc.ai.function.GetFileContentByPath;
import com.autodoc.ai.function.GetFolderStructureByPath;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class ConfigFunction {

    @Bean
    @Description("Dado o path de um arquivo, esta função lê o conteúdo do arquivo e retorna.")
    public Function<GetFileContentByPath.Request, GetFileContentByPath.Response> getFileContent() {
        return new GetFileContentByPath();
    }

    @Bean
    @Description("Dado o Path de uma pasta, esta função retorna toda a estrutura de pastas e arquivos dentro desta.")
    public Function<GetFolderStructureByPath.Request, GetFolderStructureByPath.Response> getFolderStructureByPath() {
        return new GetFolderStructureByPath();
    }

}
