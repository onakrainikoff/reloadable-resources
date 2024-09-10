package ru.on8off.reloadable.resources.core.manager;

import ru.on8off.reloadable.resources.core.data.ReloadableData;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ReloadableManager<T> {
    void start();

    boolean isStarted();

    void reload();

    T getData();

    ReloadableData<T> getReloadableData();

    List<ReloadableListener> getReloadableListeners();
    void stop();
}
