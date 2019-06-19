package com.example.demo;

public class SQLStatementCountMismatchException extends Exception {

    private final long expected;
    private final long recorded;

    public SQLStatementCountMismatchException(long expected, long recorded) {
        super(String.format("Expected %d statement(s) but recorded %d instead!",
            expected, recorded));
        this.expected = expected;
        this.recorded = recorded;
    }

    public long getExpected() {
        return expected;
    }

    public long getRecorded() {
        return recorded;
    }
}
