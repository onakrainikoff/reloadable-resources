package ru.on8off.reloadable.resources.core.data.source;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.on8off.reloadable.resources.core.data.ReloadableData;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class FileReloadableData<T> extends ReloadableData<T> {
    private LocalDateTime lastModified;
    private String location;
    private String fileName;
    private String fileExtension;
    private LocalDateTime fileCreated;
    private Long fileSizeBytes;
    private String fileSize;
    private String filePath;
}
