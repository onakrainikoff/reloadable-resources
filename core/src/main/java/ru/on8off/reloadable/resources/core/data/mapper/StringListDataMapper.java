package ru.on8off.reloadable.resources.core.data.mapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import ru.on8off.reloadable.resources.core.data.ReloadableData;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;

public class StringListDataMapper implements ReloadableDataMapper<InputStream, List<String>> {
    private final Charset charset;

    public StringListDataMapper() {
        this(Charset.defaultCharset());
    }

    public StringListDataMapper(Charset charset) {
        this.charset = Validate.notNull(charset, "Param 'charset' must not be null");
    }

    @Override
    public ReloadableData<List<String>> apply(ReloadableData<InputStream> reloadableDataFrom) {
        ReloadableData<List<String>> reloadableDataFromTo = null;
        if (reloadableDataFrom != null) {
            reloadableDataFromTo = new ReloadableData<>();
            reloadableDataFromTo.setLastReloaded(LocalDateTime.now());
            reloadableDataFromTo.setLastModified(reloadableDataFrom.getLastModified());
            // todo buffered reading
            reloadableDataFromTo.setData(IOUtils.readLines(reloadableDataFrom.getData(), charset));
        }
        return reloadableDataFromTo;
    }
}
