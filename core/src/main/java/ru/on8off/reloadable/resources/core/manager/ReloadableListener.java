package ru.on8off.reloadable.resources.core.manager;

import ru.on8off.reloadable.resources.core.data.ReloadableData;

public interface ReloadableListener {
    void onException(Exception ex);
    void onReload(ReloadableData<?> oldValue, ReloadableData<?> newValue);
}
