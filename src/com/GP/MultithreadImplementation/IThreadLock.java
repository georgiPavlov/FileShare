package com.GP.MultithreadImplementation;

/**
 * Created by user on 10/25/16.
 */
public interface IThreadLock {
    void lockWriteFile(String path);
    void unlockWriteFile(String path);
    void lockReadFile(String path);
    void unlockReadFile(String path);

}
