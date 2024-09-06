package ru.on8off.reloadable.resources.core.datasource;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

public class FileResourceDataSource implements ResourceDataSource<InputStream> {
    protected final File file;

    public FileResourceDataSource(String location) throws IOException {
        Validate.notNull(location, "Param 'path' must not be null");
        if (location.startsWith("classpath:")) {
            String filePath = getClass().getClassLoader().getResource(location.replace("classpath:", "")).getFile();
            Validate.validState(filePath != null, "File doesn't exist: location=%s", location);
            this.file = new File(filePath);
        } else {
            this.file = new File(location);
        }
        Validate.validState(file.exists(), "File doesn't exist: location=%s, absolutePath=%s", location, file.getAbsolutePath());
        Validate.validState(file.isFile(), "Path isn't a file: location=%s, absolutePath=%s", location, file.getAbsolutePath());
    }

    @Override
    public ResourceData<InputStream> open(LocalDateTime lastModified) throws IOException {
        ResourceData<InputStream> resourceData = null;
        if (!file.canRead()) {
            throw new IOException("Can't read from file: " + file.getAbsolutePath());
        }
        LocalDateTime fileLastModified =  Instant.ofEpochMilli(file.lastModified())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        if (lastModified == null || fileLastModified.isAfter(lastModified)) {
            resourceData = new ResourceData<>();
            resourceData.setLastModified(fileLastModified);
            resourceData.setLocation(file.getAbsolutePath());
            resourceData.setData(getInputStream());
        }
        return resourceData;
    }

    private InputStream getInputStream() throws IOException {
        String fileExtension = FilenameUtils.getExtension(file.getName());
        FileInputStream fileInputStream = new FileInputStream(file);
        if (fileExtension.equalsIgnoreCase("zip")) {
            return new ZipInputStream(fileInputStream);
        } else if (fileExtension.equalsIgnoreCase("gz")) {
            return new GZIPInputStream(fileInputStream);
        } else {
            return fileInputStream;
        }
    }
}
