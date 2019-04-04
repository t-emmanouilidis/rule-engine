package com.temma.rules.engine.infrastructure.common.log;

/**
 * Describes the interface to the logging facility.
 * Used to hide the real implementation to be used.
 * The default implementation of the methods in this
 * interface use the standard console facility
 * using {@code System.out} and {@code System.err}
 *
 * @author tryfon
 */
public interface Logger {

    /**
     * Writes the given message to the underlying logger
     * as a debug log statement.
     *
     * @param msg the message to output
     */
    default void debug(String msg) {
        Console.println(msg);
    }

    /**
     * Writes the given message to the underlying logger
     * as an error log statement.
     *
     * @param msg the message to output
     */
    default void error(String msg) {
        Console.printErr(msg);
    }

    /**
     * Writes the given message to the underlying logger
     * and also outputs the stack trace of the given {@link Throwable}.
     *
     * @param msg the message to output
     * @param t   the {@code Throwable} for which to print its stack trace
     */
    default void error(String msg, Throwable t) {
        Console.printErr(msg, t);
    }

}
