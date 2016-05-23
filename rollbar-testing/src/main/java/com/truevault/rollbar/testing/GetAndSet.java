package com.truevault.rollbar.testing;

public interface GetAndSet<T, U> {
    U get(T t);
    T set(T t, U val);
}
