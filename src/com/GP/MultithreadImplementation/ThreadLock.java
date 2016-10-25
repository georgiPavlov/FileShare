package com.GP.MultithreadImplementation;

/**
 * Created by user on 10/25/16.
 */
public class ThreadLock implements IThreadLock {

    @Override
    public void lockWriteFile(String path) {
        LockEntry entry = new LockEntry(path);
        while (Ram_DB.classesThatAreCurrentlyInReadState.contains(entry)){
            synchronized (Ram_DB.classesThatAreCurrentlyInReadState){
                try {
                    Ram_DB.classesThatAreCurrentlyInReadState.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Ram_DB.pathsThatAreCurrentlyUnderChange.add(path);


    }

    @Override
    public void unlockWriteFile(String path) {
        Ram_DB.pathsThatAreCurrentlyUnderChange.remove(path);
        synchronized (Ram_DB.classesThatAreCurrentlyInReadState){
            Ram_DB.classesThatAreCurrentlyInReadState.notifyAll();
        }


    }

    @Override
    public void lockReadFile(String path) {
           while (Ram_DB.pathsThatAreCurrentlyUnderChange.contains(path)){
               synchronized (Ram_DB.pathsThatAreCurrentlyUnderChange){
                   try {
                       Ram_DB.pathsThatAreCurrentlyUnderChange.wait();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           }

               LockEntry entry = new LockEntry(path);
               if(!Ram_DB.classesThatAreCurrentlyInReadState.contains(entry)){
                   Ram_DB.classesThatAreCurrentlyInReadState.add(entry);
               }

               Ram_DB.classesThatAreCurrentlyInReadState.
                       get(Ram_DB.classesThatAreCurrentlyInReadState.indexOf(entry)).
                       countReadersOnCurrentFile.incrementAndGet();




    }

    @Override
    public void unlockReadFile(String path) {
        LockEntry entry = new LockEntry(path);
        if(Ram_DB.classesThatAreCurrentlyInReadState.
                get(Ram_DB.classesThatAreCurrentlyInReadState.indexOf(entry)).
                countReadersOnCurrentFile.decrementAndGet() == 0){


            Ram_DB.classesThatAreCurrentlyInReadState.
                    remove(Ram_DB.classesThatAreCurrentlyInReadState.indexOf(entry));
            synchronized (Ram_DB.pathsThatAreCurrentlyUnderChange){
                Ram_DB.pathsThatAreCurrentlyUnderChange.notifyAll();
            }

        }
    }
}
