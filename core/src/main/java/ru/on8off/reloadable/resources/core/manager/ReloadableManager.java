package ru.on8off.reloadable.resources.core.manager;

import ru.on8off.reloadable.resources.core.data.ReloadableData;

public interface ReloadableManager<T> {
    void start();

    boolean isStarted();

    void reload();

    T getData();

    ReloadableData<T> getReloadableData();

    void stop();
}
