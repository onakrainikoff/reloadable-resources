package ru.on8off.reloadable.resources.core.manager;

import ru.on8off.reloadable.resources.core.data.ReloadableData;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultReloadableListener implements ReloadableListener {
    private String location;
    private Logger log;

    public DefaultReloadableListener(String location) {
        this.location = location;
        this.log = Logger.getLogger(this.getClass().getName());
    }

    @Override
    public void onException(Exception ex) {
        log.log(Level.SEVERE, ()-> String.format("ReloadableResource error: location=%s, exception=%s", location, ex.getMessage()));
    }

    @Override
    public void onReload(ReloadableData<?> oldValue, ReloadableData<?> newValue, long totalTimeMs) {
        log.log(Level.INFO, ()-> String.format("ReloadableResource reloaded: location=%s, time=%dms", location, totalTimeMs));
    }
}
