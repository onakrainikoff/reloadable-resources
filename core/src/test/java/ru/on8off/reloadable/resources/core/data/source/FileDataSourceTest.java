package ru.on8off.reloadable.resources.core.data.source;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FileDataSourceTest {
    static final String TEST_FILE_TXT_PATH = "files/file.txt";
    static File TEST_FILE_TXT;
    static List<String> TEXT;

    @BeforeAll
    public static void init() throws IOException {
        TEST_FILE_TXT = new File(FileDataSourceTest.class.getClassLoader().getResource(TEST_FILE_TXT_PATH).getFile());
        TEXT = FileUtils.readLines(TEST_FILE_TXT, Charset.defaultCharset());
    }

    static Stream<String> locationsSource() {
        return Stream.of(
                "classpath:" + TEST_FILE_TXT_PATH,
                TEST_FILE_TXT.getAbsolutePath()
        );
    }

    @ParameterizedTest
    @MethodSource("locationsSource")
    public void testLocations(String location) throws IOException {
        FileDataSource dataSource = new FileDataSource(location);
        Optional<FileReloadableData<InputStream>> resource = dataSource.load(null);
        assertTrue(resource.isPresent());
        FileReloadableData<InputStream> result = resource.get();
        assertNotNull(result.getLocation());
        assertNotNull(result.getLastModified());
        assertNotNull(result.getData());
        assertNotNull(result.getFileName());
        assertNotNull(result.getFileExtension());
        assertNotNull(result.getFilePath());
        assertNotNull(result.getFileCreated());
        assertNotNull(result.getFileSize());
        assertNotNull(result.getFileSizeBytes());
        assertEquals(TEXT, IOUtils.readLines(result.getData(), Charset.defaultCharset()));
    }

    @Test
    public void testLastModified() throws IOException, InterruptedException {
        File file = createTempFileTxt(TEXT);
        FileDataSource dataSource = new FileDataSource(file.getAbsolutePath());
        Optional<FileReloadableData<InputStream>> resource = dataSource.load(null);
        assertTrue(resource.isPresent());
        FileReloadableData<InputStream> result = resource.get();
        assertNotNull(result.getLocation());
        assertNotNull(result.getLastModified());
        assertNotNull(result.getData());
        assertNotNull(result.getFileName());
        assertNotNull(result.getFileExtension());
        assertNotNull(result.getFilePath());
        assertNotNull(result.getFileCreated());
        assertNotNull(result.getFileSize());
        assertNotNull(result.getFileSizeBytes());
        assertEquals(TEXT, IOUtils.readLines(result.getData(), Charset.defaultCharset()));

        Thread.sleep(100);
        LocalDateTime last = result.getLastModified();
        resource = dataSource.load(last);
        assertFalse(resource.isPresent());

        List<String> newText = List.of("Test string number 1");
        FileUtils.writeLines(file, newText);
        resource = dataSource.load(last);
        assertTrue(resource.isPresent());
        result = resource.get();
        assertNotNull(result.getLocation());
        assertNotNull(result.getLastModified());
        assertNotNull(result.getData());
        assertNotNull(result.getFileName());
        assertNotNull(result.getFileExtension());
        assertNotNull(result.getFilePath());
        assertNotNull(result.getFileCreated());
        assertNotNull(result.getFileSize());
        assertNotNull(result.getFileSizeBytes());
        assertEquals(newText, IOUtils.readLines(result.getData(), Charset.defaultCharset()));

        Thread.sleep(100);
        last = result.getLastModified();
        resource = dataSource.load(last);
        assertFalse(resource.isPresent());
    }

    @Test
    public void testExceptions() throws IOException {
        Throwable exception = assertThrows(NullPointerException.class, () -> new FileDataSource(null));
        assertTrue(exception.getMessage().contains("Param 'location' must not be null"));

        exception = assertThrows(IllegalStateException.class, () -> new FileDataSource("test.txt"));
        assertTrue(exception.getMessage().contains("File doesn't exist:"));

        exception = assertThrows(IllegalStateException.class, () -> new FileDataSource("classpath:test.txt"));
        assertTrue(exception.getMessage().contains("File doesn't exist in classpath:"));

        String dir = new File("").getAbsolutePath();
        exception = assertThrows(IllegalStateException.class, () -> new FileDataSource(dir));
        assertTrue(exception.getMessage().contains("Path isn't a file:"));
    }


    static File createTempFileTxt(Collection<String> lines) throws IOException {
        File file = File.createTempFile("file-", ".txt");
        System.out.println("Created temp file: " + file.getAbsolutePath());
        file.deleteOnExit();
        if (lines != null && !lines.isEmpty()) {
            FileUtils.writeLines(file, lines);
        }
        return file;
    }

}