package ru.on8off.reloadable.resources.core.supplier;


import ru.on8off.reloadable.resources.core.ReloadableResource;
import ru.on8off.reloadable.resources.core.ReloadableResourceSupplier;
import ru.on8off.reloadable.resources.core.datasource.ResourceDataSource;
import ru.on8off.reloadable.resources.core.mapper.StringReloadableResourceMapper;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

public class StringReloadableResourceSupplier implements ReloadableResourceSupplier<String> {
    private final ResourceDataSource<InputStream> resourceDataSource;
    private final StringReloadableResourceMapper stringMapper;

    public StringReloadableResourceSupplier(ResourceDataSource<InputStream> resourceDataSource) {
        this.resourceDataSource = resourceDataSource;
        this.stringMapper = new StringReloadableResourceMapper();
    }

    public ReloadableResource<String> get(LocalDateTime lastModified) throws IOException {
        return resourceDataSource.load(lastModified).map(stringMapper).orElse(null);
    }
}
