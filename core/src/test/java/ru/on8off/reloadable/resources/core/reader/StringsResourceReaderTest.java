package ru.on8off.reloadable.resources.core.reader;

import org.junit.Test;
import ru.on8off.reloadable.resources.core.datasource.FileResourceDataSource;
import ru.on8off.reloadable.resources.core.reloadable.ReloadableResource;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class StringsResourceReaderTest {

    @Test
    public void read() throws IOException {
        String location = "./src/test/resources/files/file.txt";
        FileResourceDataSource dataSource = new FileResourceDataSource(location);
        StringsResourceReader reader = new StringsResourceReader(dataSource);
        ReloadableResource<List<String>> result = reader.read(null);
        System.out.println(result);
    }
}