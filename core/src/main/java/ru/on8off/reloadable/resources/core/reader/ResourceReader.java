package ru.on8off.reloadable.resources.core.reader;

import ru.on8off.reloadable.resources.core.reloadable.ReloadableResource;

import java.io.IOException;
import java.time.LocalDateTime;

public interface ResourceReader <T> {
    ReloadableResource<T> read(LocalDateTime lastModified) throws IOException;
}
