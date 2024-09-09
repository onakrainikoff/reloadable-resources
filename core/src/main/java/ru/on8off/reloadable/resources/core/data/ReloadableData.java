package ru.on8off.reloadable.resources.core.data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReloadableData<T>{
    private LocalDateTime lastReloaded;
    private LocalDateTime lastModified;
    private String location;
    private T data;
}
