package ru.on8off.reloadable.resources.core.datasource;

import java.io.IOException;
import java.time.LocalDateTime;

public interface DataSource {
    byte[] load (LocalDateTime lastModified) throws IOException;
}
