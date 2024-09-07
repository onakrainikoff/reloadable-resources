package ru.on8off.reloadable.resources.core.datasource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.on8off.reloadable.resources.core.ReloadableResource;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class FileReloadableResource<T> extends ReloadableResource<T> {
    private LocalDateTime lastModified;
    private String location;
    private String fileName;
    private String fileExtension;
    private LocalDateTime fileCreated;
    private Long fileSizeBytes;
    private String fileSize;
    private String filePath;
}
