package ru.on8off.reloadable.resources.core.mapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import ru.on8off.reloadable.resources.core.ReloadableResourceData;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

public class StringReloadableResourceMapper implements ReloadableResourceMapper<InputStream, String> {
    private final Charset charset;

    public StringReloadableResourceMapper() {
        this(Charset.defaultCharset());
    }

    public StringReloadableResourceMapper(Charset charset) {
        this.charset = Validate.notNull(charset, "Param 'charset' must not be null");
    }

    @Override
    public ReloadableResourceData<String> apply(ReloadableResourceData<InputStream> reloadableResourceDataFrom) {
        ReloadableResourceData<String> reloadableResourceDataFromTo = null;
        if (reloadableResourceDataFrom != null) {
            reloadableResourceDataFromTo = new ReloadableResourceData<>();
            reloadableResourceDataFromTo.setLastReloaded(LocalDateTime.now());
            reloadableResourceDataFromTo.setLastModified(reloadableResourceDataFrom.getLastModified());
            try {
                // todo buffered reading
                reloadableResourceDataFromTo.setResource(IOUtils.toString(reloadableResourceDataFrom.getResource(), charset));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return reloadableResourceDataFromTo;
    }
}
