package com.qkedy.jaga.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for creating pools of objects.
 */
public class Pool<T> {

    public interface PoolObjectFactory<T> {
        T createObject();
    }

    private final List<T> freeObjects;
    private final PoolObjectFactory<T> factory;
    private final int maxSize;

    public Pool(PoolObjectFactory<T> factory, int maxSize) {
        this.factory = factory;
        this.maxSize = maxSize;
        this.freeObjects = new ArrayList<>(maxSize);
    }

    public T newObject() {
        return freeObjects.size() == 0 ? factory.createObject() : freeObjects.remove(freeObjects.size() - 1);
    }

    public void free(T object) {
        if (freeObjects.size() < maxSize)
            freeObjects.add(object);
    }
}
