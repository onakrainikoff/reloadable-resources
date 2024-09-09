package ru.on8off.reloadable.resources.core;

import ru.on8off.reloadable.resources.core.datasource.FileReloadableResourceDataSource;
import ru.on8off.reloadable.resources.core.manager.ReloadableResourceManager;
import ru.on8off.reloadable.resources.core.supplier.StringReloadableResourceDataSupplier;

import java.util.concurrent.TimeUnit;

public class ReloadableString implements ReloadableResource<String> {
    private final ReloadableResourceManager<String> reloadableResourceManager;
    public ReloadableString(String location, long time, TimeUnit unit) {
        this.reloadableResourceManager = new ReloadableResourceManager<>(new StringReloadableResourceDataSupplier(new FileReloadableResourceDataSource(location)), time, unit);
    }
    @Override
    public String get() {
        return reloadableResourceManager.getResource();
    }

}
