package ru.on8off.reloadable.resources.core.reloadable;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReloadableResource<T>{
    private LocalDateTime lastReloaded;
    private LocalDateTime lastModified;
    private String location;
    private T resource;
}
