package com.GP.MultithreadImplementation;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by user on 10/25/16.
 */
public class LockEntry {
    public  String pathFile;
    public  AtomicInteger countReadersOnCurrentFile;

    public LockEntry(String pathFile) {
        this.pathFile = pathFile;
        countReadersOnCurrentFile = new AtomicInteger();

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LockEntry entry = (LockEntry) o;

        return pathFile != null ? pathFile.equals(entry.pathFile) : entry.pathFile == null;

    }

    @Override
    public int hashCode() {
        return pathFile != null ? pathFile.hashCode() : 0;
    }
}
