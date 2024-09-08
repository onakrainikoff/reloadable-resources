package ru.on8off.reloadable.resources.core;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReloadableResourceManager<T> {
    private ScheduledExecutorService executorService;
    private ReloadableResourceSupplier<T> reloadableResourceSupplier;
    private volatile ReloadableResource<T> reloadableResource;
    private boolean started = false;
    private long time;
    private TimeUnit unit;
    private ReentrantLock reloadLock;

    // todo readiness

    public ReloadableResourceManager(ReloadableResourceSupplier<T> reloadableResourceSupplier, long time, TimeUnit unit) {
        this(reloadableResourceSupplier, time, unit, Executors.newSingleThreadScheduledExecutor(), false);
    }
    public ReloadableResourceManager(ReloadableResourceSupplier<T> reloadableResourceSupplier, long time, TimeUnit unit, boolean lazy) {
        this(reloadableResourceSupplier, time, unit, Executors.newSingleThreadScheduledExecutor(), lazy);
    }

    public ReloadableResourceManager(ReloadableResourceSupplier<T> reloadableResourceSupplier, long time, TimeUnit unit, ScheduledExecutorService executorService, boolean lazy) {
        // todo validation
        this.reloadableResourceSupplier = reloadableResourceSupplier;
        this.executorService = executorService;
        if (!lazy) {
            reload();
            start();
        }
    }

    public synchronized void start() {
        if (!started) {
            this.executorService.scheduleAtFixedRate(this::reload, 0, time, unit);
            this.started = true;
        }
    }

    public synchronized boolean isStarted() {
        return started;
    }

    public synchronized void reload() {
        // todo logging
        try {
            reloadLock.lock();
            LocalDateTime lastModified = reloadableResource != null ? reloadableResource.getLastReloaded() : null;
            this.reloadableResource = reloadableResourceSupplier.get(lastModified).orElse(null);
        } catch (Exception e) {

        } finally {
            reloadLock.unlock();
        }

    }

    public ReloadableResource<T> getReloadableResource() {
        return reloadableResource;
    }

    public T getResource() {
        return reloadableResource.getResource();
    }

    public synchronized void stop(){
        // todo shutdown
    }

}
