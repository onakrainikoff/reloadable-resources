package ru.on8off.reloadable.resources.core.datasource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

public class FileResourceDataSource implements ResourceDataSource<InputStream> {
    private final String location;
    private final File file;

    public FileResourceDataSource(String location) {
        Validate.notNull(location, "Param 'location' must not be null");
        this.location = location;
        if (location.startsWith("classpath:")) {
            ClassLoader classLoader = getClass().getClassLoader();
            String resourceName = location.replace("classpath:", "");
            URL resourceFileUrl = classLoader.getResource(resourceName);
            Validate.validState(resourceFileUrl != null, "File doesn't exist in classpath: location=%s", location);
            String resourceFilePath = resourceFileUrl.getFile();
            Validate.validState(resourceFilePath != null, "File doesn't exist in classpath: location=%s", location);
            this.file = new File(resourceFilePath);
        } else {
            this.file = new File(location);
        }
        Validate.validState(file.exists(), "File doesn't exist: location=%s, absolutePath=%s", location, file.getAbsolutePath());
        Validate.validState(file.isFile(), "Path isn't a file: location=%s, absolutePath=%s", location, file.getAbsolutePath());
    }

    @Override
    public Optional<FileReloadableResource<InputStream>> load(LocalDateTime lastModified) throws IOException {
        FileReloadableResource<InputStream> resourceData = null;
        if (!file.canRead()) {
            throw new IOException("Can't read from file: " + file.getAbsolutePath());
        }
        BasicFileAttributes fileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        LocalDateTime fileLastModified = localDateTime(fileAttributes.lastModifiedTime().toMillis());
        if (lastModified == null || fileLastModified.isAfter(lastModified)) {
            resourceData = new FileReloadableResource<>();
            resourceData.setLastModified(fileLastModified);
            resourceData.setLocation(location);
            resourceData.setResource(new FileInputStream(file));
            resourceData.setFileName(file.getName());
            resourceData.setFileExtension(FilenameUtils.getExtension(file.getName()));
            resourceData.setFilePath(file.getAbsolutePath());
            resourceData.setFileCreated(localDateTime(fileAttributes.creationTime().toMillis()));
            resourceData.setFileSize(FileUtils.byteCountToDisplaySize(fileAttributes.size()));
            resourceData.setFileSizeBytes(fileAttributes.size());
        }
        return Optional.ofNullable(resourceData);
    }

    private LocalDateTime localDateTime(long millis) {
        return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
