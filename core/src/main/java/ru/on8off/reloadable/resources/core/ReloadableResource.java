package ru.on8off.reloadable.resources.core;

import java.util.function.Supplier;

public interface ReloadableResource<T>{
    T get();
    T getReloadableResourceData();
    T getReloadableResourceManager();

}
