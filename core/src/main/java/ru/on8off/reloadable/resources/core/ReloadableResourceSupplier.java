package ru.on8off.reloadable.resources.core;

import java.io.IOException;
import java.time.LocalDateTime;

public interface ReloadableResourceSupplier<T> {
    ReloadableResource<T> get(LocalDateTime lastModified) throws IOException;
}
