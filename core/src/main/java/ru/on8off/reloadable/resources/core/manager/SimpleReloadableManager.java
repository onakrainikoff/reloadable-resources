package ru.on8off.reloadable.resources.core.manager;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import ru.on8off.reloadable.resources.core.data.ReloadableData;
import ru.on8off.reloadable.resources.core.data.supplier.ReloadableDataSupplier;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleReloadableManager<T> implements ReloadableManager<T> {
    private ScheduledExecutorService executorService;
    private ReloadableDataSupplier<T> reloadableDataSupplier;
    private volatile ReloadableData<T> reloadableData;
    private boolean started = false;
    private long period;
    private TimeUnit unit;
    private long initialDelay;
    private ReentrantLock reloadLock;

    public SimpleReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long period, TimeUnit unit) {
        this(reloadableDataSupplier, period, unit, false);
    }

    public SimpleReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long period, TimeUnit unit, boolean lazy) {
        this(reloadableDataSupplier, period, unit, 0, defaultExecutorService(), lazy);
    }
    public SimpleReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long period, TimeUnit unit, long initialDelay, boolean lazy) {
        this(reloadableDataSupplier, period, unit, initialDelay, defaultExecutorService(), lazy);
    }
    public SimpleReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long period, TimeUnit unit, long initialDelay, ScheduledExecutorService executorService, boolean lazy) {
        // todo validation
        this.reloadableDataSupplier = reloadableDataSupplier;
        this.executorService = executorService;
        this.period = period;
        this.unit = unit;
        this.initialDelay = initialDelay;
        if (!lazy) {
            reload();
            start();
        }
    }

    @Override
    public synchronized void start() {
        if (!started && !executorService.isShutdown()) {
            this.executorService.scheduleAtFixedRate(this::reload, initialDelay, period, unit);
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
        if (!executorService.isShutdown()) {
            this.executorService.shutdown();
        }
    }

    private static ScheduledExecutorService defaultExecutorService() {
        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("SimpleReloadableManager-%d")
                .daemon(true)
                .priority(Thread.MAX_PRIORITY)
                .build();
        return Executors.newSingleThreadScheduledExecutor(factory);
    }

}
