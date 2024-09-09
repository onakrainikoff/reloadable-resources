package ru.on8off.reloadable.resources.core.supplier;

import ru.on8off.reloadable.resources.core.ReloadableResourceData;
import ru.on8off.reloadable.resources.core.datasource.ReloadableResourceDataSource;
import ru.on8off.reloadable.resources.core.mapper.StringListReloadableResourceMapper;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class StringListReloadableResourceDataSupplier implements ReloadableResourceDataSupplier<List<String>> {
    private final ReloadableResourceDataSource<InputStream> reloadableResourceDataSource;
    private final StringListReloadableResourceMapper stringListReloadableResourceMapper;

    public StringListReloadableResourceDataSupplier(ReloadableResourceDataSource<InputStream> reloadableResourceDataSource) {
        this.reloadableResourceDataSource = reloadableResourceDataSource;
        this.stringListReloadableResourceMapper = new StringListReloadableResourceMapper();
    }

    public Optional<ReloadableResourceData<List<String>>> get(LocalDateTime lastModified) throws IOException {
        return reloadableResourceDataSource.load(lastModified).map(stringListReloadableResourceMapper);
    }
}
