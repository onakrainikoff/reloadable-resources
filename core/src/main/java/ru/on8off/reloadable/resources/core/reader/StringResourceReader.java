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

public class StringResourceReader implements ResourceReader<String> {
    private final ResourceDataSource<InputStream> resourceDataSource;
    private final Charset charset;

    public StringResourceReader(ResourceDataSource<InputStream> resourceDataSource) {
        this(resourceDataSource, Charset.defaultCharset());
    }

    public StringResourceReader(ResourceDataSource<InputStream> resourceDataSource, Charset charset) {
        this.resourceDataSource = Validate.notNull(resourceDataSource, "Param 'resourceDataSource' must not be null");
        this.charset = Validate.notNull(charset, "Param 'charset' must not be null");
    }

    @Override
    public ReloadableResource<String> read(LocalDateTime lastModified) throws IOException {
        ResourceData<InputStream> resourceData =  resourceDataSource.load(lastModified);
        ReloadableResource<String> reloadableResource = null;
        if (resourceData != null) {
            reloadableResource = new ReloadableResource<>();
            reloadableResource.setLastReloaded(LocalDateTime.now());
            reloadableResource.setLastModified(resourceData.getLastModified());
            reloadableResource.setResource(IOUtils.toString(resourceData.getData(), charset));
        }
        return reloadableResource;
    }
}
