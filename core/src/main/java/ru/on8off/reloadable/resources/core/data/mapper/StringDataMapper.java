package ru.on8off.reloadable.resources.core.data.mapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import ru.on8off.reloadable.resources.core.ReloadableException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class StringDataMapper extends BaseReloadableDataMapper<InputStream, String> {
    private final Charset charset;

    public StringDataMapper() {
        this(Charset.defaultCharset());
    }

    public StringDataMapper(Charset charset) {
        this.charset = Validate.notNull(charset, "Param 'charset' must not be null");
    }

    @Override
    public String map(InputStream dataFrom) {
        try {
            return IOUtils.toString(dataFrom, charset);
        } catch (IOException e) {
            throw new ReloadableException(e);
        }
    }
}
