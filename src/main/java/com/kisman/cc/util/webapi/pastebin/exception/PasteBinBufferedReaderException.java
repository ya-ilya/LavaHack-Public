package com.kisman.cc.util.webapi.pastebin.exception;

public class PasteBinBufferedReaderException extends RuntimeException {
    public PasteBinBufferedReaderException(final String msg) {
        super(msg);
        this.setStackTrace(new StackTraceElement[0]);
    }

    @Override public String toString() {return "PasteBinBufferedReader error! Please create ticket in TheDiscord about this crash!";}
    @Override public synchronized Throwable fillInStackTrace() {return this;}
}
