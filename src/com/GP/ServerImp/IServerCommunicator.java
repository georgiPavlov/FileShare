package com.GP.ServerImp;

/**
 * Created by user on 10/24/16.
 */
public interface IServerCommunicator {
    boolean loginAuthentication();
    boolean commitFromAdmin();
    boolean sendFileToClient();
    boolean deleteFromAdmin();
    boolean deleteFromAdmin(String textPath);


}
