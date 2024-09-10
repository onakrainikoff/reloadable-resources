package ru.on8off.reloadable.resources.core.data.mapper;

import org.junit.jupiter.api.Test;
import ru.on8off.reloadable.resources.core.data.ReloadableData;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BaseReloadableDataMapperTest {
    static final String TEST_TEXT = "Test text";
    @Test
     public void testApply() {
        BaseReloadableDataMapper<byte[], String> mapper = new BaseReloadableDataMapper<byte[], String>() {
            @Override
            public String map(byte[] dataFrom) {
                return new String(dataFrom);
            }
        };
        ReloadableData<byte[]> from = new ReloadableData<>();
        from.setData(TEST_TEXT.getBytes());
        from.setLastModified(LocalDateTime.now());
        from.setLastReloaded(LocalDateTime.now());
        from.setLocation("test-location");

        ReloadableData<String> to = mapper.apply(from);
        assertNotNull(to);
        assertEquals( from.getLastReloaded(), to.getLastReloaded());
        assertEquals( from.getLastModified(), to.getLastModified());
        assertEquals( from.getLocation(), to.getLocation());
        assertEquals( TEST_TEXT, to.getData());

        to = mapper.apply(null);
        assertNull(to);
    }
}