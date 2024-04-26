package com.autodoc.ai.function;

import com.autodoc.ai.function.util.FileUtils;
import com.fasterxml.jackson.annotation.JsonClassDescription;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

public class GetFolderStructureByPath implements Function<GetFolderStructureByPath.Request, GetFolderStructureByPath.Response> {

    @Override
    public Response apply(Request request) {
        try {
            var folderStructure = FileUtils.readFolderStructure(Path.of(request.folderPath));
            return new Response(folderStructure);
        }catch(Exception e) {
            return new Response(List.of());
        }
    }

    @JsonClassDescription("Na requisição é obrigatório passar o Path da pasta.")
    public record Request(String folderPath) {};

    @JsonClassDescription("Devolve toda a estrutura de pastas do folderPath enviado na requisição.")
    public record Response(List<String> folders) {};


}
