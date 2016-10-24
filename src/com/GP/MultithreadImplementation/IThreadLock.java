package com.GP.MultithreadImplementation;

/**
 * Created by user on 10/25/16.
 */
public interface IThreadLock {
    void lockWriteFile();
    void unlockWriteFile();
    void lockReadFile();
    void unlockReadFile();

}
