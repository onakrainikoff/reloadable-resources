package ru.on8off.reloadable.resources.core.supplier;

import ru.on8off.reloadable.resources.core.ReloadableResourceData;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ReloadableResourceDataSupplier<T> {
    Optional<ReloadableResourceData<T>> get(LocalDateTime lastModified) throws IOException;
}
