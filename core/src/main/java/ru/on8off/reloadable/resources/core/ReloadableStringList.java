package ru.on8off.reloadable.resources.core;

import ru.on8off.reloadable.resources.core.data.source.FileDataSource;
import ru.on8off.reloadable.resources.core.data.supplier.StringListDataSupplier;
import ru.on8off.reloadable.resources.core.manager.BaseReloadableManager;
import ru.on8off.reloadable.resources.core.manager.ReloadableManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReloadableStringList implements ReloadableResource<List<String>> {
    private final ReloadableManager<List<String>> reloadableManager;

    public ReloadableStringList(String location, long time, TimeUnit unit) {
        this.reloadableManager = new BaseReloadableManager<>(new StringListDataSupplier(new FileDataSource(location)), time, unit);
    }

    @Override
    public List<String> get() {
        return reloadableManager.getData();
    }

    @Override
    public ReloadableManager<List<String>> getReloadableResourceManager() {
        return reloadableManager;
    }
}
