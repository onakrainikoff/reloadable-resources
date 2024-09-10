package ru.on8off.reloadable.resources.core.data.mapper;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StringListDataMapperTest {
    static final String TEST_FILE_TXT_PATH = "files/file.txt";
    static File TEST_FILE_TXT;
    static List<String> TEXT;

    @BeforeAll
    public static void init() throws IOException {
        TEST_FILE_TXT = new File(StringListDataMapperTest.class.getClassLoader().getResource(TEST_FILE_TXT_PATH).getFile());
        TEXT = FileUtils.readLines(TEST_FILE_TXT, Charset.defaultCharset());
    }

    @Test
    void map() throws FileNotFoundException {
        StringListDataMapper mapper = new StringListDataMapper();
        InputStream from = new FileInputStream(TEST_FILE_TXT);
        List<String> to = mapper.map(from);
        assertNotNull(to);
        assertEquals(TEXT, to);
    }
}