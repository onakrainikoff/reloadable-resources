package ru.on8off.reloadable.resources.core.data.source;

import ru.on8off.reloadable.resources.core.data.ReloadableData;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ReloadableDataSource<T> {
    Optional<? extends ReloadableData<T>> load(LocalDateTime lastModified);
}
