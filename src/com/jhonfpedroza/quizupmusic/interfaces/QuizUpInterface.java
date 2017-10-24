package com.jhonfpedroza.quizupmusic.interfaces;

import com.jhonfpedroza.quizupmusic.models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface QuizUpInterface extends Remote {

    User logIn(String name) throws RemoteException;
}
