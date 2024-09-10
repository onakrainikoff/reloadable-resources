package ru.on8off.reloadable.resources.core.data.supplier;


import ru.on8off.reloadable.resources.core.data.ReloadableData;
import ru.on8off.reloadable.resources.core.data.mapper.StringDataMapper;
import ru.on8off.reloadable.resources.core.data.source.ReloadableDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;

public class StringDataSupplier implements ReloadableDataSupplier<String> {
    private final ReloadableDataSource<InputStream> reloadableDataSource;
    private final StringDataMapper stringDataMapper;

    public StringDataSupplier(ReloadableDataSource<InputStream> reloadableDataSource) {
        this.reloadableDataSource = reloadableDataSource;
        this.stringDataMapper = new StringDataMapper();
    }

    public Optional<ReloadableData<String>> get(LocalDateTime lastModified) throws IOException {
        return reloadableDataSource.load(lastModified).map(stringDataMapper);
    }
}
