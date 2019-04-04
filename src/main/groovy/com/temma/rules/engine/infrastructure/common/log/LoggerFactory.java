package com.temma.rules.engine.infrastructure.common.log;

import java.util.Objects;
import java.util.function.Function;

public final class LoggerFactory {

    private static final Function<Class<?>, Logger> DEFAULT = Slf4jLogger::get;

    private static Function<Class<?>, Logger> loggerProvider = DEFAULT;

    private LoggerFactory() {
        super();
    }

    public static void setLoggerProvider(Function<Class<?>, Logger> provider) {
        loggerProvider = Objects.requireNonNull(provider, "provider can't be null");
    }

    public static Logger getFor(Class<?> aClass) {
        Objects.requireNonNull(aClass, "aClass can't be null");
        
        return loggerProvider.apply(aClass);
    }

}
