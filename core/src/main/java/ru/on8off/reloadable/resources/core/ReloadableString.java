package ru.on8off.reloadable.resources.core;

import ru.on8off.reloadable.resources.core.data.source.FileDataSource;
import ru.on8off.reloadable.resources.core.data.supplier.StringDataSupplier;
import ru.on8off.reloadable.resources.core.manager.SimpleReloadableManager;
import ru.on8off.reloadable.resources.core.manager.ReloadableManager;

import java.util.concurrent.TimeUnit;

public class ReloadableString implements ReloadableResource<String> {
    private final ReloadableManager<String> reloadableManager;

    public ReloadableString(String location, long time, TimeUnit unit) {
        this.reloadableManager = new SimpleReloadableManager<>(new StringDataSupplier(new FileDataSource(location)), time, unit);
    }

    @Override
    public String get() {
        return reloadableManager.getData();
    }

    @Override
    public ReloadableManager<String> getReloadableResourceManager() {
        return reloadableManager;
    }
}
