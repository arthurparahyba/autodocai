package com.autodoc.ai.function.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static String readFileContent(Path filePath) {
        try {
            var buffer = new StringBuffer();
            logger.info("lendo o arquivo %s".formatted(filePath));
            if(Files.isDirectory(filePath)){
                logger.info("Não é possível ler o conteúdo interno de uma pasta. Aceito apenas path de arquivos texto.");
                throw new RuntimeException("Não é possível ler o conteúdo interno de uma pasta. Aceito apenas path de arquivos texto. (%s)".formatted(filePath));
            }
            List<String> linhas = Files.readAllLines(filePath);
            for (String linha : linhas) {
                buffer.append(linha);
            }
            return buffer.toString();
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível ler o arquivo com path %s".formatted(filePath), e);
        }
    }

    public static List<String> readFolderStructure(Path filePath) {
        var folders = new ArrayList<String>();
        try (Stream<Path> stream = Files.walk(filePath)) {
            stream.forEach(path -> {
                folders.add(path.toAbsolutePath().toString());
            });

        } catch (IOException e) {
            throw new RuntimeException("Erro ao percorrer a lista de pastas do path %s. Corrija o caminho do path e tente novamente.".formatted(filePath.toString()));
        }
        return folders;
    }

}
