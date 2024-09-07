package ru.on8off.reloadable.resources.core.supplier;

import ru.on8off.reloadable.resources.core.ReloadableResource;
import ru.on8off.reloadable.resources.core.ReloadableResourceSupplier;
import ru.on8off.reloadable.resources.core.datasource.ResourceDataSource;
import ru.on8off.reloadable.resources.core.mapper.StringListReloadableResourceMapper;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

public class StringListReloadableResourceSupplier implements ReloadableResourceSupplier<List<String>> {
    private final ResourceDataSource<InputStream> resourceDataSource;
    private final StringListReloadableResourceMapper stringListMapper;

    public StringListReloadableResourceSupplier(ResourceDataSource<InputStream> resourceDataSource) {
        this.resourceDataSource = resourceDataSource;
        this.stringListMapper = new StringListReloadableResourceMapper();
    }

    public ReloadableResource<List<String>> get(LocalDateTime lastModified) throws IOException {
        return resourceDataSource.load(lastModified).map(stringListMapper).orElse(null);
    }
}
