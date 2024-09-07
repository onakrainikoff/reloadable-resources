package ru.on8off.reloadable.resources.core.supplier;

import ru.on8off.reloadable.resources.core.ReloadableResource;
import ru.on8off.reloadable.resources.core.ReloadableResourceSupplier;
import ru.on8off.reloadable.resources.core.datasource.ReloadableResourceDataSource;
import ru.on8off.reloadable.resources.core.mapper.StringListReloadableResourceMapper;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class StringListReloadableResourceSupplier implements ReloadableResourceSupplier<List<String>> {
    private final ReloadableResourceDataSource<InputStream> reloadableResourceDataSource;
    private final StringListReloadableResourceMapper stringListReloadableResourceMapper;

    public StringListReloadableResourceSupplier(ReloadableResourceDataSource<InputStream> reloadableResourceDataSource) {
        this.reloadableResourceDataSource = reloadableResourceDataSource;
        this.stringListReloadableResourceMapper = new StringListReloadableResourceMapper();
    }

    public Optional<ReloadableResource<List<String>>> get(LocalDateTime lastModified) throws IOException {
        return reloadableResourceDataSource.load(lastModified).map(stringListReloadableResourceMapper);
    }
}
