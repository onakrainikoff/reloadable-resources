package ru.on8off.reloadable.resources.core.data.mapper;

import ru.on8off.reloadable.resources.core.data.ReloadableData;

import java.util.function.Function;

public interface ReloadableDataMapper<F, T> extends Function<ReloadableData<F>, ReloadableData<T>> {}
