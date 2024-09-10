package ru.on8off.reloadable.resources.core.manager;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import ru.on8off.reloadable.resources.core.ReloadableException;
import ru.on8off.reloadable.resources.core.data.ReloadableData;
import ru.on8off.reloadable.resources.core.data.supplier.ReloadableDataSupplier;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScheduledReloadableManager<T> implements ReloadableManager<T> {
    private final ReloadableDataSupplier<T> reloadableDataSupplier;
    private final List<ReloadableListener> reloadableListeners = new CopyOnWriteArrayList<>();
    private volatile ReloadableData<T> reloadableData;
    private final ReentrantLock reloadLock = new ReentrantLock();
    private boolean started = false;
    private final ScheduledExecutorService executorService;
    private final long period;
    private final TimeUnit unit;

    public ScheduledReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long period, TimeUnit unit) {
        this(reloadableDataSupplier, period, unit, defaultExecutorService(), Collections.emptyList(), false);
    }

    public ScheduledReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long period, TimeUnit unit, boolean lazy) {
        this(reloadableDataSupplier, period, unit, defaultExecutorService(), Collections.emptyList(), lazy);
    }

    public ScheduledReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long period, TimeUnit unit, ReloadableListener listener) {
        this(reloadableDataSupplier, period, unit, defaultExecutorService(), (listener == null ? Collections.emptyList() : List.of(listener)), false);
    }

    public ScheduledReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long period, TimeUnit unit, ReloadableListener listener, boolean lazy) {
        this(reloadableDataSupplier, period, unit, defaultExecutorService(), (listener == null ? Collections.emptyList() : List.of(listener)), lazy);
    }

    public ScheduledReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long period, TimeUnit unit, List<ReloadableListener> listeners, boolean lazy) {
        this(reloadableDataSupplier, period, unit, defaultExecutorService(), listeners, lazy);
    }

    public ScheduledReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long period, TimeUnit unit, ScheduledExecutorService executorService, List<ReloadableListener> listeners, boolean lazy) {
        this.reloadableDataSupplier = Validate.notNull(reloadableDataSupplier, "Param 'reloadableDataSupplier' must not be null");
        Validate.isTrue(period > 0, "Param 'period' must be > 0");
        this.period = period;
        this.unit = Validate.notNull(unit, "Param 'unit' must not be null");
        this.executorService = Validate.notNull(executorService, "Param 'executorService' must not be null");
        if (listeners != null && !listeners.isEmpty()) {
            this.reloadableListeners.addAll(listeners);
        }
        if (!lazy) {
            start();
        }
    }

    @Override
    public synchronized void start() {
        if (!started && !executorService.isShutdown()) {
            reload();
            this.executorService.scheduleAtFixedRate(() -> {
                try {reload();} catch (Exception ignored) {}
            }, period, period, unit);
            this.started = true;
        }
    }

    public synchronized void startAsync() {
        if (!started && !executorService.isShutdown()) {
            this.executorService.scheduleAtFixedRate(() -> {
                try {reload();} catch (Exception ignored) {}
            }, period, period, unit);
            this.started = true;
        }
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public void reload() {
        try {
            reloadLock.lock();
            long startedTime = System.currentTimeMillis();
            LocalDateTime lastModified = reloadableData != null ? reloadableData.getLastReloaded() : null;
            ReloadableData<T> newValue = reloadableDataSupplier.get(lastModified).orElse(null);
            ReloadableData<T> oldValue = this.reloadableData;
            if (newValue != null) {
                this.reloadableData = newValue;
            }
            long totalTime = System.currentTimeMillis() - startedTime;
            onReloadNotify(oldValue, newValue, totalTime);
        } catch (Exception ex) {
            onExceptionNotify(ex);
            throw new ReloadableException(ex);
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
    public List<ReloadableListener> getReloadableListeners() {
        return reloadableListeners;
    }


    @Override
    public synchronized void stop() {
        if (!executorService.isShutdown()) {
            this.executorService.shutdown();
        }
    }

    private void onReloadNotify(ReloadableData<T> oldValue, ReloadableData<T> newValue, long totalTimeMs) {
        if (!reloadableListeners.isEmpty()) {
            for (ReloadableListener listener : reloadableListeners) {
                try {
                    listener.onReload(oldValue, newValue, totalTimeMs);
                } catch (Throwable t) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, t, ()-> "ScheduledReloadableManager onReloadNotify error:");
                }
            }
        }
    }

    private void onExceptionNotify(Exception ex) {
        if (!reloadableListeners.isEmpty()) {
            for (ReloadableListener listener : reloadableListeners) {
                try {
                    listener.onException(ex);
                } catch (Throwable t) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, t, ()-> "ScheduledReloadableManager onExceptionNotify error:");
                }
            }
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
