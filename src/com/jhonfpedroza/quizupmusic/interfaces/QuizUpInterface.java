package com.jhonfpedroza.quizupmusic.interfaces;

import com.jhonfpedroza.quizupmusic.models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface QuizUpInterface extends Remote {

    User logIn(String name) throws RemoteException;
    void logOut(User user) throws RemoteException;
    ArrayList<User> getOnlineUsers() throws RemoteException;
}
