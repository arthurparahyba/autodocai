package com.autodoc.ai.function;

import com.autodoc.ai.function.util.FileUtils;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.function.Function;

public class GetFileContentByPath implements Function<GetFileContentByPath.Request, GetFileContentByPath.Response> {

    private static final Logger logger = LoggerFactory.getLogger(GetFileContentByPath.class);

    @Override
    public Response apply(Request request) {
        try {
            logger.info(request.toString());
            var fileContent = FileUtils.readFileContent(Path.of(request.filePath));
            return new Response(fileContent);
        }catch(Exception e) {
            return new Response(e.getMessage());
        }
    }

    @JsonClassDescription("É obrigatório passar o path completo do arquivo.")
    public record Request(String filePath) {};

    @JsonClassDescription("Retorna o conteúdo do arquivo.")
    public record Response(String fileContent) {};


}
