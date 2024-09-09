package ru.on8off.reloadable.resources.core.data.supplier;

import ru.on8off.reloadable.resources.core.data.ReloadableData;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ReloadableDataSupplier<T> {
    Optional<ReloadableData<T>> get(LocalDateTime lastModified) throws IOException;
}
