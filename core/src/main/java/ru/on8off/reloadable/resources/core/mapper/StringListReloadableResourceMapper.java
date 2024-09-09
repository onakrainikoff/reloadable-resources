package ru.on8off.reloadable.resources.core.mapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import ru.on8off.reloadable.resources.core.ReloadableResourceData;

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
    public ReloadableResourceData<List<String>> apply(ReloadableResourceData<InputStream> reloadableResourceDataFrom) {
        ReloadableResourceData<List<String>> reloadableResourceDataFromTo = null;
        if (reloadableResourceDataFrom != null) {
            reloadableResourceDataFromTo = new ReloadableResourceData<>();
            reloadableResourceDataFromTo.setLastReloaded(LocalDateTime.now());
            reloadableResourceDataFromTo.setLastModified(reloadableResourceDataFrom.getLastModified());
            // todo buffered reading
            reloadableResourceDataFromTo.setResource(IOUtils.readLines(reloadableResourceDataFrom.getResource(), charset));
        }
        return reloadableResourceDataFromTo;
    }
}
