package ru.on8off.reloadable.resources.core.datasource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileResourceDataSource implements ResourceDataSource<InputStream> {
    private final String location;
    private final File file;

    public FileResourceDataSource(String location) {
        Validate.notNull(location, "Param 'location' must not be null");
        this.location = location;
        if (location.startsWith("classpath:")) {
            ClassLoader classLoader = getClass().getClassLoader();
            String resourceName = location.replace("classpath:", "");
            String resourceFilePath = classLoader.getResource(resourceName).getFile();
            Validate.validState(resourceFilePath != null, "File doesn't exist: location=%s", location);
            this.file = new File(resourceFilePath);
        } else {
            this.file = new File(location);
        }
        Validate.validState(file.exists(), "File doesn't exist: location=%s, absolutePath=%s", location, file.getAbsolutePath());
        Validate.validState(file.isFile(), "Path isn't a file: location=%s, absolutePath=%s", location, file.getAbsolutePath());
    }

    @Override
    public ResourceData<InputStream> load(LocalDateTime lastModified) throws IOException {
        ResourceData<InputStream> resourceData = null;
        if (!file.canRead()) {
            throw new IOException("Can't read from file: " + file.getAbsolutePath());
        }
        LocalDateTime fileLastModified = localDateTime(file.lastModified());
        if (lastModified == null || fileLastModified.isAfter(lastModified)) {
            resourceData = new ResourceData<>();
            resourceData.setLastModified(fileLastModified);
            resourceData.setLocation(location);
            resourceData.setData(new FileInputStream(file));
            resourceData.setMetaData(getMetaData());
        }
        return resourceData;
    }

    private Map<String, String> getMetaData() throws IOException {
        Map<String, String> metaData = new LinkedHashMap<>();
        BasicFileAttributes fileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        metaData.put("file.name", file.getName());
        metaData.put("file.extension", FilenameUtils.getExtension(file.getName()));
        metaData.put("file.modified", localDateTime(file.lastModified()).toString());
        metaData.put("file.created", localDateTime(fileAttributes.creationTime().toMillis()).toString());
        metaData.put("file.size", FileUtils.byteCountToDisplaySize(file.length()));
        metaData.put("file.path", file.getAbsolutePath());
        return metaData;
    }

    private LocalDateTime localDateTime(long millis) {
        return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
