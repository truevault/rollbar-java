package com.truevault.rollbar.payload.data;

class DummyStack extends Exception {

    private static final Throwable DUMMY_THROWABLE = getThrowable0();

    public DummyStack() {
        super("dummy");
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        StackTraceElement[] stack = new StackTraceElement[3];
        System.arraycopy(DUMMY_THROWABLE.getStackTrace(), 0, stack, 0, 3);

        return stack;
    }

    private static Throwable getThrowable0() {
        return getThrowable1();
    }

    private static Throwable getThrowable1() {
        return getThrowable2();
    }

    private static Throwable getThrowable2() {
        return new Throwable();
    }
}
