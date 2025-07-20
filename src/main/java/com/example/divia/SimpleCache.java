package com.example.divia;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class SimpleCache<T> {
    private final Supplier<T> cacheSetter;
    private LocalDateTime lastFetch = LocalDateTime.MIN;
    private T cached;
    private final int durationInSeconds;

    public SimpleCache(Supplier<T> cacheSetter, int durationInSeconds) {
        this.cacheSetter = cacheSetter;
        this.durationInSeconds = durationInSeconds;
    }

    private boolean hasExpired() {
        return LocalDateTime.now().minusSeconds(durationInSeconds).isAfter(lastFetch);
    }

    public T Get() {
        if (hasExpired()) {
            T newValue = cacheSetter.get();
            cached = newValue;
            lastFetch = LocalDateTime.now();
            return newValue;
        } else {
            return cached;
        }
    }

    public void Set(T value) {
        this.cached = value;
    }
}
