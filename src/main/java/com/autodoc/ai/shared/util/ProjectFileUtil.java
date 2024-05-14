package com.autodoc.ai.shared.util;

import com.autodoc.ai.shared.doc.DocumentationCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class ProjectFileUtil {
    private static final Logger logger = LoggerFactory.getLogger(ProjectFileUtil.class);

    public static String readContentForFile(Path filePath) {
        try {
            var buffer = new StringBuffer();
            logger.info("lendo o arquivo %s".formatted(filePath));
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (Exception e) {
            logger.warn("Conteúdo do arquivo não pode ser lido: %s".formatted(filePath.toString()));
            return "Arquivo: "+filePath.toString();
//            throw new RuntimeException("Não foi possível ler o arquivo com path %s".formatted(filePath), e);
        }
    }

    public record ProjectFolder(Path projectPath, Path folderPath, String documentation, List<ProjectFile> files, List<ProjectFolder> folders){
        public List<Path> getFilesPath() {
            var files = new ArrayList<Path>();

            this.files.stream()
                    .map(ProjectFile::path)
                    .forEach(files::add);

            this.folders.stream()
                    .map(ProjectFolder::getFilesPath)
                    .forEach(files::addAll);

            return files;
        }

        public List<ProjectFile> findWithPath(Set<Path> paths) {
            var projectFilesFound = new ArrayList<ProjectFile>();

            final var projectFiles = this.files.stream()
                    .filter(file -> paths.contains(file.path))
                    .toList();
            projectFilesFound.addAll(projectFiles);

            final var projectFolderFiles = this.folders.stream()
                    .flatMap(folder -> folder.findWithPath(paths).stream())
                    .toList();
            projectFilesFound.addAll(projectFolderFiles);
            return projectFilesFound;
        }

        public List<Path> getFoldersPath() {
            var folders = new ArrayList<Path>();
            folders.add(this.folderPath);

            this.folders.stream()
                    .map(ProjectFolder::getFoldersPath)
                    .forEach(folders::addAll);

            return folders;
        }
    };

    public record ProjectFileDoc(String resume, String description, DocumentationCategory category){};

    public record ProjectFile(Path path, ProjectFileDoc documentation){};

    public static ProjectFolder toProjectFolder(Path projectPath) {

        List<Path> paths = new ArrayList<>();
        Path gitignore = projectPath.resolve(".gitignore");
        final List<String> ignorePatterns = new ArrayList<>();

        if (Files.exists(gitignore)) {
            try {
                ignorePatterns.addAll(Files.readAllLines(gitignore));
            } catch (IOException e) {
                throw new RuntimeException("Erro ao ler .gitignore: " + e.getMessage());
            }
        }

        ProjectFolder folder = new ProjectFolder(projectPath, projectPath, "", new ArrayList<>(), new ArrayList<>());
        folder.folders.addAll(getFoldersFrom(projectPath, projectPath, ignorePatterns));
        folder.files.addAll(getFilesFrom(folder.projectPath, folder.folderPath, ignorePatterns));
        return folder;
    }

    private static List<ProjectFolder> getFoldersFrom(Path projectPath, Path folderPath, List<String> ignorePatterns) {
        List<ProjectFolder> folders = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(folderPath, 1)) {
            stream.filter(path -> Files.isDirectory(path) && !path.equals(folderPath))
                    .filter(path -> shouldInclude(path, ignorePatterns, projectPath))
                    .forEach(path -> {
                        var folder = new ProjectFolder(projectPath, path, "", new ArrayList<>(), new ArrayList<>());
                        folder.files.addAll(getFilesFrom(folder.projectPath, folder.folderPath, ignorePatterns));
                        folder.folders.addAll(getFoldersFrom(projectPath, path, ignorePatterns));
                        folders.add(folder);
                    });
        } catch (IOException e) {
            throw new RuntimeException("Erro ao percorrer a lista de pastas do path %s. Corrija o caminho do path e tente novamente.".formatted(projectPath.toString()));
        }

        return folders;
    }

    private static List<ProjectFile> getFilesFrom(Path projectPath, Path folderPath, List<String> ignorePatterns) {
        List<ProjectFile> files = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(folderPath, 1)) {
            stream.filter(Files::isRegularFile)
                    .filter(path -> shouldInclude(path, ignorePatterns, projectPath))
                    .forEach(path -> {
                        var projectFile = new ProjectFile(path, null);
                        files.add(projectFile);
                    });
        } catch (IOException e) {
            throw new RuntimeException("Erro ao percorrer a lista de pastas do path %s. Corrija o caminho do path e tente novamente.".formatted(projectPath.toString()));
        }
        return files;
    }

    private static boolean shouldInclude(Path path, List<String> ignorePatterns, Path rootPath) {
        Path relativePath = rootPath.relativize(path);
        String pathString = relativePath.toString().replace(File.separator, "/");

        if (pathString.startsWith(".")) {
            return false;
        }

        return ignorePatterns.stream().noneMatch(pattern -> {
            String regex = pattern.replace("*", ".*").replace("/", ".*");
            return pathString.matches(regex);
        });
    }
}
