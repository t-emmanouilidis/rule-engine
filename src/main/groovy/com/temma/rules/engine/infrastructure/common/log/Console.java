package com.temma.rules.engine.infrastructure.common.log;

public final class Console {

    private Console() {
        super();
    }

    public static void println(String msg) {
        System.out.println(msg);
    }

    public static void printErr(String errorMsg) {
        System.err.println(errorMsg);
    }

    public static void printErr(String errorMsg, Throwable t) {
        System.err.println(errorMsg);
        t.printStackTrace(System.err);
    }

}
