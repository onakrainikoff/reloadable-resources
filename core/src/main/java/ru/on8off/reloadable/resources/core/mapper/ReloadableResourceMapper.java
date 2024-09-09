package ru.on8off.reloadable.resources.core.mapper;

import ru.on8off.reloadable.resources.core.ReloadableResourceData;

import java.util.function.Function;

public interface ReloadableResourceMapper<F, T> extends Function<ReloadableResourceData<F>, ReloadableResourceData<T>> {}
