package ru.on8off.reloadable.resources.core.data.mapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import ru.on8off.reloadable.resources.core.data.ReloadableData;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

public class StringDataMapper implements ReloadableDataMapper<InputStream, String> {
    private final Charset charset;

    public StringDataMapper() {
        this(Charset.defaultCharset());
    }

    public StringDataMapper(Charset charset) {
        this.charset = Validate.notNull(charset, "Param 'charset' must not be null");
    }

    @Override
    public ReloadableData<String> apply(ReloadableData<InputStream> reloadableDataFrom) {
        ReloadableData<String> reloadableDataFromTo = null;
        if (reloadableDataFrom != null) {
            reloadableDataFromTo = new ReloadableData<>();
            reloadableDataFromTo.setLastReloaded(LocalDateTime.now());
            reloadableDataFromTo.setLastModified(reloadableDataFrom.getLastModified());
            try {
                // todo buffered reading
                reloadableDataFromTo.setData(IOUtils.toString(reloadableDataFrom.getData(), charset));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return reloadableDataFromTo;
    }
}
