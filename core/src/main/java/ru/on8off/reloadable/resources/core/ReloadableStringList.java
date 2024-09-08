package ru.on8off.reloadable.resources.core;

import ru.on8off.reloadable.resources.core.datasource.FileReloadableResourceDataSource;
import ru.on8off.reloadable.resources.core.manager.ReloadableResourceManager;
import ru.on8off.reloadable.resources.core.supplier.StringListReloadableResourceSupplier;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReloadableStringList extends ReloadableResourceManager<List<String>> {
    public ReloadableStringList(String location, long time, TimeUnit unit) {
        super(new StringListReloadableResourceSupplier(new FileReloadableResourceDataSource(location)), time, unit);
    }
}
