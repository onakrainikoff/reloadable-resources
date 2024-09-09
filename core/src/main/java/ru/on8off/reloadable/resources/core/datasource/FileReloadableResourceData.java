package ru.on8off.reloadable.resources.core.datasource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.on8off.reloadable.resources.core.ReloadableResourceData;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class FileReloadableResourceData<T> extends ReloadableResourceData<T> {
    private LocalDateTime lastModified;
    private String location;
    private String fileName;
    private String fileExtension;
    private LocalDateTime fileCreated;
    private Long fileSizeBytes;
    private String fileSize;
    private String filePath;
}
