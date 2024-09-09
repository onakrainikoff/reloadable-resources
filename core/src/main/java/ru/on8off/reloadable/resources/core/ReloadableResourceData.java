package ru.on8off.reloadable.resources.core;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReloadableResourceData<T>{
    private LocalDateTime lastReloaded;
    private LocalDateTime lastModified;
    private String location;
    private T resource;
}
