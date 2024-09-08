package ru.on8off.reloadable.resources.core;

import ru.on8off.reloadable.resources.core.datasource.FileReloadableResourceDataSource;
import ru.on8off.reloadable.resources.core.datasource.ReloadableResourceDataSource;
import ru.on8off.reloadable.resources.core.manager.ReloadableResourceManager;
import ru.on8off.reloadable.resources.core.supplier.StringReloadableResourceSupplier;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ReloadableString extends ReloadableResourceManager<String> {
    private String location;
    private FileReloadableResourceDataSource resourceDataSource;

    public ReloadableString(String location, long time, TimeUnit unit) {
        super();
        this.location = location;
        this.resourceDataSource = new FileReloadableResourceDataSource(location);
        this.reloadableResourceSupplier = new StringReloadableResourceSupplier(resourceDataSource);
        reload();
        start();
    }
}
