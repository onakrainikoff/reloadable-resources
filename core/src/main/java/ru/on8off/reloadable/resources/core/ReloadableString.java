package ru.on8off.reloadable.resources.core;

import ru.on8off.reloadable.resources.core.datasource.FileReloadableResourceDataSource;
import ru.on8off.reloadable.resources.core.datasource.ReloadableResourceDataSource;
import ru.on8off.reloadable.resources.core.manager.ReloadableResourceManager;
import ru.on8off.reloadable.resources.core.supplier.StringReloadableResourceSupplier;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ReloadableString extends ReloadableResourceManager<String> {
    public ReloadableString(String location, long time, TimeUnit unit) {
        super(new StringReloadableResourceSupplier(new FileReloadableResourceDataSource(location)), time, unit);
    }
}
