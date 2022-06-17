package com.github.elic0de.h1.utils;

import com.github.elic0de.h1.H1Plugin;
import lombok.experimental.UtilityClass;

import java.util.logging.Logger;

@UtilityClass
public class LogUtil {
    public void info(final String info) {
        getLogger().info(info);
    }

    public void warn(final String warn) {
        getLogger().info(warn);
    }

    public void error(final String error) {
        getLogger().info(error);
    }

    public Logger getLogger() {
        return H1Plugin.INSTANCE.getPlugin().getLogger();
    }
}
