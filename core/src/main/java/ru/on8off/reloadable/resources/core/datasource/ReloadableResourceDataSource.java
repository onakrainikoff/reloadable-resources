package ru.on8off.reloadable.resources.core.datasource;

import ru.on8off.reloadable.resources.core.ReloadableResourceData;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ReloadableResourceDataSource<T> {
    Optional<? extends ReloadableResourceData<T>> load(LocalDateTime lastModified) throws IOException;
}
