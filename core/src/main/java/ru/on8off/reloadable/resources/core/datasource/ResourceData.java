package ru.on8off.reloadable.resources.core.datasource;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ResourceData<T> {
    private LocalDateTime lastModified;
    private String location;
    private T data;
    private Map<String, String> metaData;
}
