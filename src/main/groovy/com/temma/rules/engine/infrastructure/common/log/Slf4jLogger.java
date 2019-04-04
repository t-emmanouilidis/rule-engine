package com.temma.rules.engine.infrastructure.common.log;

import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Slf4J based {@link Logger} implementation.
 *
 * @author tryfon
 */
public class Slf4jLogger implements Logger {

    private org.slf4j.Logger logger;

    private Slf4jLogger(org.slf4j.Logger logger) {
        super();

        this.logger = Objects.requireNonNull(logger, "logger can't be null");
    }

    @Override
    public void debug(String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }

    @Override
    public void error(String msg) {
        if (logger.isErrorEnabled()) {
            logger.error(msg);
        }
    }

    @Override
    public void error(String msg, Throwable t) {
        if (logger.isErrorEnabled()) {
            logger.error(msg, t);
        }
    }

    /**
     * Creates a new {@link Logger} instance given a
     * {@link org.slf4j.Logger}.
     *
     * @param aClass the {@code Class} for which to create a {@code Logger}
     * @return a {@code Logger}
     */
    public static Logger get(Class<?> aClass) {
        Objects.requireNonNull(aClass, "aClass can't be null");

        return new Slf4jLogger(LoggerFactory.getLogger(aClass));
    }

}
