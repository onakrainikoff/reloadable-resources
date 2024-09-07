package ru.on8off.reloadable.resources.core.mapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import ru.on8off.reloadable.resources.core.ReloadableResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

public class StringMapper implements Mapper<InputStream, String> {
    private final Charset charset;

    public StringMapper() {
        this(Charset.defaultCharset());
    }

    public StringMapper(Charset charset) {
        this.charset = Validate.notNull(charset, "Param 'charset' must not be null");
    }

    @Override
    public ReloadableResource<String> apply(ReloadableResource<InputStream> reloadableResourceFrom) {
        ReloadableResource<String> reloadableResourceFromTo = null;
        if (reloadableResourceFrom != null) {
            reloadableResourceFromTo = new ReloadableResource<>();
            reloadableResourceFromTo.setLastReloaded(LocalDateTime.now());
            reloadableResourceFromTo.setLastModified(reloadableResourceFrom.getLastModified());
            try {
                reloadableResourceFromTo.setResource(IOUtils.toString(reloadableResourceFrom.getResource(), charset));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return reloadableResourceFromTo;
    }
}
