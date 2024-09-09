package ru.on8off.reloadable.resources.core.manager;

import ru.on8off.reloadable.resources.core.data.ReloadableData;

import java.util.Collection;

public interface ReloadableManager<T> {
    void start();

    boolean isStarted();

    void reload();

    T getData();

    ReloadableData<T> getReloadableData();

    Collection<ReloadableListener> getReloadableListeners();
    void stop();
}
