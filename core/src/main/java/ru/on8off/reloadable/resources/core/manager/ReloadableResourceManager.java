package ru.on8off.reloadable.resources.core.manager;

import ru.on8off.reloadable.resources.core.ReloadableResourceData;
import ru.on8off.reloadable.resources.core.supplier.ReloadableResourceDataSupplier;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReloadableResourceManager<T> {
    protected ScheduledExecutorService executorService;
    protected ReloadableResourceDataSupplier<T> reloadableResourceDataSupplier;
    protected volatile ReloadableResourceData<T> reloadableResourceData;
    protected boolean started = false;
    protected long time;
    protected TimeUnit unit;
    protected ReentrantLock reloadLock;

    // todo readiness
    public ReloadableResourceManager(ReloadableResourceDataSupplier<T> reloadableResourceDataSupplier, long time, TimeUnit unit) {
        this(reloadableResourceDataSupplier, time, unit, Executors.newSingleThreadScheduledExecutor(), false);
    }
    public ReloadableResourceManager(ReloadableResourceDataSupplier<T> reloadableResourceDataSupplier, long time, TimeUnit unit, boolean lazy) {
        this(reloadableResourceDataSupplier, time, unit, Executors.newSingleThreadScheduledExecutor(), lazy);
    }

    public ReloadableResourceManager(ReloadableResourceDataSupplier<T> reloadableResourceDataSupplier, long time, TimeUnit unit, ScheduledExecutorService executorService, boolean lazy) {
        // todo validation
        this.reloadableResourceDataSupplier = reloadableResourceDataSupplier;
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
            LocalDateTime lastModified = reloadableResourceData != null ? reloadableResourceData.getLastReloaded() : null;
            this.reloadableResourceData = reloadableResourceDataSupplier.get(lastModified).orElse(null);
        } catch (Exception e) {

        } finally {
            reloadLock.unlock();
        }

    }

    public ReloadableResourceData<T> getReloadableResource() {
        return reloadableResourceData;
    }

    public T getResource() {
        return reloadableResourceData.getResource();
    }

    public synchronized void stop(){
        // todo shutdown
    }

}
