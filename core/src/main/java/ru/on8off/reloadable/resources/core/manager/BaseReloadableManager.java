package ru.on8off.reloadable.resources.core.manager;

import org.apache.commons.lang3.Validate;
import ru.on8off.reloadable.resources.core.data.ReloadableData;
import ru.on8off.reloadable.resources.core.data.supplier.ReloadableDataSupplier;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.ReentrantLock;

public abstract class BaseReloadableManager<T> implements ReloadableManager<T> {
    protected final ReloadableDataSupplier<T> reloadableDataSupplier;
    protected final Set<ReloadableListener> reloadableListeners = new ConcurrentSkipListSet<>();
    protected volatile ReloadableData<T> reloadableData;
    protected final ReentrantLock reloadLock = new ReentrantLock();
    protected boolean started = false;

    public BaseReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, Set<ReloadableListener> listeners) {
        this.reloadableDataSupplier = Validate.notNull(reloadableDataSupplier, "Param 'reloadableDataSupplier' must not be null");
        if (listeners != null && !listeners.isEmpty()) {
            this.reloadableListeners.addAll(listeners);
        }
    }

    @Override
    public void start() {
        if (!started) {
            reload();
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
            throw new RuntimeException(ex);
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
    public Collection<ReloadableListener> getReloadableListeners() {
        return reloadableListeners;
    }

    @Override
    public void stop() {
        this.started = false;
    }

    protected void onReloadNotify(ReloadableData<T> oldValue, ReloadableData<T> newValue, long totalTimeMs) {
        if (!reloadableListeners.isEmpty()) {
            for (ReloadableListener listener : reloadableListeners) {
                try {
                    listener.onReload(oldValue, newValue, totalTimeMs);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }

    protected void onExceptionNotify(Exception ex) {
        if (!reloadableListeners.isEmpty()) {
            for (ReloadableListener listener : reloadableListeners) {
                try {
                    listener.onException(ex);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }

}
