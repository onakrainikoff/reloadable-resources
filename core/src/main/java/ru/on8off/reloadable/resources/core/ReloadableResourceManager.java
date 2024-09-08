package ru.on8off.reloadable.resources.core;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReloadableResourceManager<T> {
    private ScheduledExecutorService executorService;
    private ReloadableResourceSupplier<T> reloadableResourceSupplier;
    private volatile ReloadableResource<T> reloadableResource;

    // todo readiness


    // todo logging conguration
    public ReloadableResourceManager(ReloadableResourceSupplier<T> reloadableResourceSupplier, long reloadEveryMs) {
        this(reloadableResourceSupplier, reloadEveryMs, Executors.newSingleThreadScheduledExecutor());
        // todo ThreadFactory
    }

    public ReloadableResourceManager(ReloadableResourceSupplier<T> reloadableResourceSupplier, long reloadEveryMs, ScheduledExecutorService executorService) {
        // todo validation
        this.reloadableResourceSupplier = reloadableResourceSupplier;
        this.executorService = executorService;
        this.executorService.scheduleAtFixedRate(this::reload, 0, reloadEveryMs, TimeUnit.MILLISECONDS);
    }

    public void reload() {
        // todo lock
        // todo logging
        try {
            LocalDateTime lastModified = reloadableResource != null ? reloadableResource.getLastReloaded() : null;
            this.reloadableResource = reloadableResourceSupplier.get(lastModified).orElse(null);
        } catch (Exception e) {

        }
    }

    public T getReloadableResource() {
        return reloadableResource.getResource();
    }


    // todo shutdown

}
