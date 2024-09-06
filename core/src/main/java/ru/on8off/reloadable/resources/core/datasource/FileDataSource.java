package ru.on8off.reloadable.resources.core.datasource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileDataSource implements DataSource {
    private final Charset charset;
    private final File file;

    public FileDataSource(String path) {
        this.charset = Charset.defaultCharset();
        this.file =

    }

    @Override
    public byte[] load(String resourcePath) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("fileTest.txt").getFile());
        String data = FileUtils.readFileToString(file, "UTF-8");


        return "test".getBytes(StandardCharsets.UTF_8);
    }

    private byte[] read(Path path) throws IOException {
        return Files.readAllBytes(path);
    }
}
