package ru.on8off.reloadable.resources.core.datasource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import ru.on8off.reloadable.resources.core.reloadable.ReloadableResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FileResourceDataSourceTest {
    List<String> fileTxtLinesExpected = Arrays.asList(
            "Test string number 1",
            "Test string number 2",
            "Test string number 3"
    );


    @Test
    public void testOpenAbsolutePathLocation() throws IOException {
        String location = new File(new File("").getAbsolutePath(), "src/test/resources/files/file.txt").getAbsolutePath();
        FileResourceDataSource dataSource = new FileResourceDataSource(location);
        ResourceData<InputStream> result = dataSource.open(null);
        assertNotNull(result);
        assertNotNull(result.getLocation());
        assertNotNull(result.getLastModified());
        assertNotNull(result.getData());
        assertEquals(fileTxtLinesExpected, IOUtils.readLines(result.getData()));
    }

    @Test
    public void testOpenRelativePathLocation() throws IOException {
        String location = "./src/test/resources/files/file.txt";
        FileResourceDataSource dataSource = new FileResourceDataSource(location);
        ResourceData<InputStream> result = dataSource.open(null);
        assertNotNull(result);
        assertNotNull(result.getLocation());
        assertNotNull(result.getLastModified());
        assertNotNull(result.getData());
        assertEquals(fileTxtLinesExpected, IOUtils.readLines(result.getData()));
    }

    @Test
    public void testOpenClassPathLocation() throws IOException {
        String location = "classpath:files/file.txt";
        FileResourceDataSource dataSource = new FileResourceDataSource(location);
        ResourceData<InputStream> result = dataSource.open(null);
        assertNotNull(result);
        assertNotNull(result.getLocation());
        assertNotNull(result.getLastModified());
        assertNotNull(result.getData());
        assertEquals(fileTxtLinesExpected, IOUtils.readLines(result.getData()));
    }

    @Test
    public void testOpenZip() throws IOException {
        String location = "./src/test/resources/files/file.txt.zip";;
        FileResourceDataSource dataSource = new FileResourceDataSource(location);
        ResourceData<InputStream> result = dataSource.open(null);
        assertNotNull(result);
        assertNotNull(result.getLocation());
        assertNotNull(result.getLastModified());
        assertNotNull(result.getData());
        System.out.println(result.getData());
        assertEquals(fileTxtLinesExpected, IOUtils.readLines(result.getData()));
    }
}