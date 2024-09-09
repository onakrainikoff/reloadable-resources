package ru.on8off.reloadable.resources.core.data.supplier;

import ru.on8off.reloadable.resources.core.data.ReloadableData;
import ru.on8off.reloadable.resources.core.data.source.ReloadableDataSource;
import ru.on8off.reloadable.resources.core.data.mapper.StringListDataMapper;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class StringListDataSupplier implements ReloadableDataSupplier<List<String>> {
    private final ReloadableDataSource<InputStream> reloadableDataSource;
    private final StringListDataMapper stringListDataMapper;

    public StringListDataSupplier(ReloadableDataSource<InputStream> reloadableDataSource) {
        this.reloadableDataSource = reloadableDataSource;
        this.stringListDataMapper = new StringListDataMapper();
    }

    public Optional<ReloadableData<List<String>>> get(LocalDateTime lastModified) throws IOException {
        return reloadableDataSource.load(lastModified).map(stringListDataMapper);
    }
}
