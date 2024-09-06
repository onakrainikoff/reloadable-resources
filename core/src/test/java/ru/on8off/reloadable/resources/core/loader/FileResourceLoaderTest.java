package ru.on8off.reloadable.resources.core.loader;

import org.junit.Test;
import ru.on8off.reloadable.resources.core.datasource.FileDataSource;

public class FileResourceLoaderTest {

    @Test
    public void load() {
        FileDataSource loader = new FileDataSource();
        byte[] result = loader.load("files/file.txt");
        System.out.println(new String(result));

    }
}