package ru.on8off.reloadable.resources.core.data.mapper;

import ru.on8off.reloadable.resources.core.data.ReloadableData;

import java.time.LocalDateTime;

public abstract class BaseReloadableDataMapper<F, T> implements ReloadableDataMapper<F, T> {
    @Override
    public ReloadableData<T> apply(ReloadableData<F> reloadableDataFrom) {
        ReloadableData<T> reloadableDataTo = null;
        if (reloadableDataFrom != null) {
            reloadableDataTo = new ReloadableData<>();
            reloadableDataTo.setLastReloaded(LocalDateTime.now());
            reloadableDataTo.setLastModified(reloadableDataFrom.getLastModified());
            reloadableDataTo.setLocation(reloadableDataFrom.getLocation());
            try {
                reloadableDataTo.setData(map(reloadableDataFrom.getData()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return reloadableDataTo;
    }

    public abstract T map(F dataFrom) throws Exception;
}
