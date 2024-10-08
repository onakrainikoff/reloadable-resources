package ru.on8off.reloadable.resources.core.data.source;

import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.Validate;
import ru.on8off.reloadable.resources.core.ReloadableException;

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

@Getter
public class FileDataSource implements ReloadableDataSource<InputStream> {
    private final String location;
    private final File file;

    public FileDataSource(String location) {
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
    public Optional<FileReloadableData<InputStream>> load(LocalDateTime lastModified) {
        FileReloadableData<InputStream> resourceData = null;
        if (!file.canRead()) {
            throw new ReloadableException("Can't read from file: " + file.getAbsolutePath());
        }
        BasicFileAttributes fileAttributes = null;
        try {
            fileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            LocalDateTime fileLastModified = localDateTime(fileAttributes.lastModifiedTime().toMillis());
            if (lastModified == null || fileLastModified.isAfter(lastModified)) {
                resourceData = new FileReloadableData<>();
                resourceData.setLastModified(fileLastModified);
                resourceData.setLocation(location);
                resourceData.setData(new FileInputStream(file));
                resourceData.setFileName(file.getName());
                resourceData.setFileExtension(FilenameUtils.getExtension(file.getName()));
                resourceData.setFilePath(file.getAbsolutePath());
                resourceData.setFileCreated(localDateTime(fileAttributes.creationTime().toMillis()));
                resourceData.setFileSize(FileUtils.byteCountToDisplaySize(fileAttributes.size()));
                resourceData.setFileSizeBytes(fileAttributes.size());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(resourceData);
    }

    private LocalDateTime localDateTime(long millis) {
        return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
