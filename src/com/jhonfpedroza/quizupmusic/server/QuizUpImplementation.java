package com.jhonfpedroza.quizupmusic.server;

import com.jhonfpedroza.quizupmusic.interfaces.QuizUpInterface;
import com.jhonfpedroza.quizupmusic.models.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class QuizUpImplementation extends UnicastRemoteObject implements QuizUpInterface {

    private List<User> users;

    QuizUpImplementation() throws RemoteException {
        super();
        users = new ArrayList<>();
    }

    @Override
    public User logIn(String name) throws RemoteException{
        long id = System.currentTimeMillis();
        User user = new User(id, name);
        users.add(user);

        System.out.println(name + " has logged in.");
        return user;
    }
}
