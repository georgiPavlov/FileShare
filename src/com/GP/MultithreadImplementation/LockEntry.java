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

        LockEntry lockEntry = (LockEntry) o;

        if (!pathFile.equals(lockEntry.pathFile)) return false;
        return countReadersOnCurrentFile.equals(lockEntry.countReadersOnCurrentFile);

    }

    @Override
    public int hashCode() {
        int result = pathFile.hashCode();
        result = 31 * result + countReadersOnCurrentFile.hashCode();
        return result;
    }
}
