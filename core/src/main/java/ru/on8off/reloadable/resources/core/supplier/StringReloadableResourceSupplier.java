package ru.on8off.reloadable.resources.core.supplier;


import ru.on8off.reloadable.resources.core.ReloadableResource;
import ru.on8off.reloadable.resources.core.ReloadableResourceSupplier;
import ru.on8off.reloadable.resources.core.datasource.ReloadableResourceDataSource;
import ru.on8off.reloadable.resources.core.mapper.StringReloadableResourceMapper;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;

public class StringReloadableResourceSupplier implements ReloadableResourceSupplier<String> {
    private final ReloadableResourceDataSource<InputStream> resourceDataSource;
    private final StringReloadableResourceMapper stringReloadableResourceMapper;

    public StringReloadableResourceSupplier(ReloadableResourceDataSource<InputStream> reloadableResourceDataSource) {
        this.resourceDataSource = reloadableResourceDataSource;
        this.stringReloadableResourceMapper = new StringReloadableResourceMapper();
    }

    public Optional<ReloadableResource<String>> get(LocalDateTime lastModified) throws IOException {
        return resourceDataSource.load(lastModified).map(stringReloadableResourceMapper);
    }
}
