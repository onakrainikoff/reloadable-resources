package ru.on8off.reloadable.resources.core.manager;

import ru.on8off.reloadable.resources.core.data.ReloadableData;
import ru.on8off.reloadable.resources.core.data.supplier.ReloadableDataSupplier;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class BaseReloadableManager<T> implements ReloadableManager<T> {
    protected ScheduledExecutorService executorService;
    protected ReloadableDataSupplier<T> reloadableDataSupplier;
    protected volatile ReloadableData<T> reloadableData;
    protected boolean started = false;
    protected long time;
    protected TimeUnit unit;
    protected ReentrantLock reloadLock;

    public BaseReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long time, TimeUnit unit) {
        this(reloadableDataSupplier, time, unit, Executors.newSingleThreadScheduledExecutor(), false);
    }

    public BaseReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long time, TimeUnit unit, boolean lazy) {
        this(reloadableDataSupplier, time, unit, Executors.newSingleThreadScheduledExecutor(), lazy);
    }

    public BaseReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long time, TimeUnit unit, ScheduledExecutorService executorService, boolean lazy) {
        // todo validation
        this.reloadableDataSupplier = reloadableDataSupplier;
        this.executorService = executorService;
        if (!lazy) {
            reload();
            start();
        }
    }

    @Override
    public synchronized void start() {
        if (!started) {
            this.executorService.scheduleAtFixedRate(this::reload, 0, time, unit);
            this.started = true;
        }
    }

    @Override
    public synchronized boolean isStarted() {
        return started;
    }

    @Override
    public void reload() {
        // todo logging
        try {
            reloadLock.lock();
            LocalDateTime lastModified = reloadableData != null ? reloadableData.getLastReloaded() : null;
            this.reloadableData = reloadableDataSupplier.get(lastModified).orElse(null);
        } catch (Exception e) {

        } finally {
            reloadLock.unlock();
        }

    }

    @Override
    public T getData() {
        return reloadableData == null ? null : reloadableData.getData();
    }

    @Override
    public ReloadableData<T> getReloadableData() {
        return reloadableData;
    }

    @Override
    public synchronized void stop() {
        // todo shutdown
    }

}
