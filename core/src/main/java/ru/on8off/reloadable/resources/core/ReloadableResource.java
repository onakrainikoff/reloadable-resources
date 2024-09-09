package ru.on8off.reloadable.resources.core;

import ru.on8off.reloadable.resources.core.manager.ReloadableManager;

public interface ReloadableResource<T> {
    T get();

    ReloadableManager<T> getReloadableResourceManager();
}
