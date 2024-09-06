package ru.on8off.reloadable.resources.core.datasource;

import lombok.Data;

import java.io.InputStream;
import java.time.LocalDateTime;

@Data
public class ResourceData<T> {
    private LocalDateTime lastModified;
    private String location;
    private T data;

}
