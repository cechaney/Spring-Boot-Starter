package com.cec.sbs.health;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.cec.sbs.config.Configurator;


@Component
public class ConfigHealthIndicator implements HealthIndicator {

    private AtomicBoolean configLoaded = new AtomicBoolean(false);
    private String configPath;
    private Configurator parent;
    private Date lastConfigUpdate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

	@Override
    public Health health() {

        if(configLoaded.get()){
            return Health.up()
            		.withDetail("settings", parent.getGlobalConfig())
            		.withDetail("lastConfigUpdate",  dateFormat.format(lastConfigUpdate))
            		.build();
        } else {
            return Health.down()
                    .withDetail("reason", "Configuration file never loaded")
                    .withDetail("configFile", configPath)
                    .build();
        }
    }

    public void loadConfig(){
        configLoaded.set(true);
    }

    public void unloadConfig(){
        configLoaded.set(false);
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String getConfigPath(){
    	return this.configPath;
    }

	public Configurator getParent() {
		return parent;
	}

	public void setParent(Configurator parent) {
		this.parent = parent;
	}

    public Date getLastConfigUpdate() {
		return lastConfigUpdate;
	}

	public void setLastConfigUpdate(Date lastConfigUpdate) {
		this.lastConfigUpdate = lastConfigUpdate;
	}

}
