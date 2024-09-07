package ru.on8off.reloadable.resources.core.mapper;

import ru.on8off.reloadable.resources.core.ReloadableResource;

import java.util.function.Function;

public interface ReloadableResourceMapper<F, T> extends Function<ReloadableResource<F>, ReloadableResource<T>> {}
