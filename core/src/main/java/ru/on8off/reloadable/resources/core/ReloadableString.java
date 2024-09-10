package ru.on8off.reloadable.resources.core;

import ru.on8off.reloadable.resources.core.data.source.FileDataSource;
import ru.on8off.reloadable.resources.core.data.supplier.StringDataSupplier;
import ru.on8off.reloadable.resources.core.manager.DefaultReloadableListener;
import ru.on8off.reloadable.resources.core.manager.ReloadableListener;
import ru.on8off.reloadable.resources.core.manager.ReloadableManager;
import ru.on8off.reloadable.resources.core.manager.ScheduledReloadableManager;

import java.util.concurrent.TimeUnit;

public class ReloadableString implements ReloadableResource<String> {
    private String location;
    private final ReloadableManager<String> reloadableManager;

    public ReloadableString(String location, long time, TimeUnit unit) {
        this.location = location;
        this.reloadableManager = new ScheduledReloadableManager<>(new StringDataSupplier(new FileDataSource(location)), time, unit, new DefaultReloadableListener(location));
    }

    public ReloadableString(String location, long time, TimeUnit unit, long initialDelay, ReloadableListener listener) {
        this.reloadableManager = new ScheduledReloadableManager<>(new StringDataSupplier(new FileDataSource(location)), time, unit, listener);
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
