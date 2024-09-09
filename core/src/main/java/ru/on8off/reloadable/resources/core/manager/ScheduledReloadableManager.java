package ru.on8off.reloadable.resources.core.manager;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import ru.on8off.reloadable.resources.core.data.supplier.ReloadableDataSupplier;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledReloadableManager<T> extends BaseReloadableManager<T> {
    private final ScheduledExecutorService executorService;
    private final long period;
    private final TimeUnit unit;

    public ScheduledReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long period, TimeUnit unit) {
        this(reloadableDataSupplier, period, unit, defaultExecutorService(), Collections.emptySet(), false);
    }

    public ScheduledReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long period, TimeUnit unit, boolean lazy) {
        this(reloadableDataSupplier, period, unit, defaultExecutorService(), Collections.emptySet(), lazy);
    }

    public ScheduledReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long period, TimeUnit unit, ReloadableListener listener) {
        this(reloadableDataSupplier, period, unit, defaultExecutorService(), (listener == null ? Collections.emptySet() : Set.of(listener)), false);
    }

    public ScheduledReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long period, TimeUnit unit, ReloadableListener listener, boolean lazy) {
        this(reloadableDataSupplier, period, unit, defaultExecutorService(), (listener == null ? Collections.emptySet() : Set.of(listener)), lazy);
    }
    public ScheduledReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long period, TimeUnit unit, Set<ReloadableListener> listeners, boolean lazy) {
        this(reloadableDataSupplier, period, unit, defaultExecutorService(), listeners, lazy);
    }

    public ScheduledReloadableManager(ReloadableDataSupplier<T> reloadableDataSupplier, long period, TimeUnit unit, ScheduledExecutorService executorService, Set<ReloadableListener> listeners, boolean lazy) {
        super(reloadableDataSupplier, listeners);
        Validate.isTrue(period > 0, "Param 'period' must be > 0");
        this.period = period;
        this.unit = Validate.notNull(unit, "Param 'unit' must not be null");
        this.executorService = Validate.notNull(executorService, "Param 'executorService' must not be null");
        if (!lazy) {
            start();
        }
    }

    @Override
    public synchronized void start() {
        if (!started && !executorService.isShutdown()) {
            super.start();
            this.executorService.scheduleAtFixedRate(()-> {
                try {
                    reload();
                } catch (Exception ex) {
                    if(reloadableListeners.isEmpty()) {
                        ex.printStackTrace();
                    }
                }
            }, period, period, unit);
        }
    }

    @Override
    public synchronized boolean isStarted() {
        return started;
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
