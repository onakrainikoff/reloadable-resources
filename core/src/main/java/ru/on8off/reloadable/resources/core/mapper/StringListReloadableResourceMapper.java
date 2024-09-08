package ru.on8off.reloadable.resources.core.mapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import ru.on8off.reloadable.resources.core.ReloadableResource;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;

public class StringListReloadableResourceMapper implements ReloadableResourceMapper<InputStream, List<String>> {
    private final Charset charset;

    public StringListReloadableResourceMapper() {
        this(Charset.defaultCharset());
    }

    public StringListReloadableResourceMapper(Charset charset) {
        this.charset = Validate.notNull(charset, "Param 'charset' must not be null");
    }

    @Override
    public ReloadableResource<List<String>> apply(ReloadableResource<InputStream> reloadableResourceFrom) {
        ReloadableResource<List<String>> reloadableResourceFromTo = null;
        if (reloadableResourceFrom != null) {
            reloadableResourceFromTo = new ReloadableResource<>();
            reloadableResourceFromTo.setLastReloaded(LocalDateTime.now());
            reloadableResourceFromTo.setLastModified(reloadableResourceFrom.getLastModified());
            // todo buffered reading
            reloadableResourceFromTo.setResource(IOUtils.readLines(reloadableResourceFrom.getResource(), charset));
        }
        return reloadableResourceFromTo;
    }
}
