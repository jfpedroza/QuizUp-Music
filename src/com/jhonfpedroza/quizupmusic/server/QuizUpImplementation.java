package com.jhonfpedroza.quizupmusic.server;

import com.jhonfpedroza.quizupmusic.interfaces.QuizUpInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class QuizUpImplementation extends UnicastRemoteObject implements QuizUpInterface {

    public QuizUpImplementation() throws RemoteException {
        super();
    }

    @Override
    public String test() throws RemoteException{
        return "Test string";
    }
}
