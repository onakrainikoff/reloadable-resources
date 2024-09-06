package ru.on8off.reloadable.resources.core.reader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import ru.on8off.reloadable.resources.core.reloadable.ReloadableResource;
import ru.on8off.reloadable.resources.core.datasource.ResourceData;
import ru.on8off.reloadable.resources.core.datasource.ResourceDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;

public class StringsResourceReader implements ResourceReader<List<String>> {
    private final ResourceDataSource<InputStream> resourceDataSource;
    private final Charset charset;

    public StringsResourceReader(ResourceDataSource<InputStream> resourceDataSource) {
        this(resourceDataSource, Charset.defaultCharset());
    }

    public StringsResourceReader(ResourceDataSource<InputStream> resourceDataSource, Charset charset) {
        this.resourceDataSource = Validate.notNull(resourceDataSource, "Param 'resourceDataSource' must not be null");
        this.charset = Validate.notNull(charset, "Param 'charset' must not be null");
    }

    @Override
    public ReloadableResource<List<String>> read(LocalDateTime lastModified) throws IOException {
        ResourceData<InputStream> resourceData =  resourceDataSource.load(lastModified);
        ReloadableResource<List<String>> reloadableResource = null;
        if (resourceData != null) {
            reloadableResource = new ReloadableResource<>();
            reloadableResource.setLastModified(resourceData.getLastModified());
            reloadableResource.setResource(IOUtils.readLines(resourceData.getData(), charset));
        }
        return reloadableResource;
    }
}
