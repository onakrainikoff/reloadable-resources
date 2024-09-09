package ru.on8off.reloadable.resources.core.data.mapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public class StringListDataMapper extends BaseReloadableDataMapper<InputStream, List<String>> {
    private final Charset charset;

    public StringListDataMapper() {
        this(Charset.defaultCharset());
    }

    public StringListDataMapper(Charset charset) {
        this.charset = Validate.notNull(charset, "Param 'charset' must not be null");
    }

    @Override
    public List<String> map(InputStream dataFrom) throws Exception {
        return IOUtils.readLines(dataFrom, charset);
    }

}
