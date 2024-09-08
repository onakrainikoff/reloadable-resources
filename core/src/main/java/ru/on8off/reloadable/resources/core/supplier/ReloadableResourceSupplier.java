package ru.on8off.reloadable.resources.core.supplier;

import ru.on8off.reloadable.resources.core.ReloadableResource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ReloadableResourceSupplier<T> {
    Optional<ReloadableResource<T>> get(LocalDateTime lastModified) throws IOException;
}
