package com.autodoc.ai.project.util;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

class ProjectFileUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(ProjectFileUtilTest.class);

    @Test
    public void testListFilesFromPath() throws Exception {
        var folderPath = Path.of("C:\\Users\\arthu\\OneDrive\\Documentos\\ia-test\\project-manipulator");
        try (Stream<Path> paths = Files.walk(folderPath, 1)) {
            paths.filter(path -> Files.isDirectory(path) && !path.equals(folderPath))
                    .forEach(System.out::println);
        }

        var paths = ProjectFileUtil.toProjectFolder(Path.of("C:\\Users\\arthu\\OneDrive\\Documentos\\ia-test\\project-manipulator"));
        logger.info(paths.toString());
    }
}