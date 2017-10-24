package com.jhonfpedroza.quizupmusic.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface QuizUpInterface extends Remote {

    String test() throws RemoteException;
}
