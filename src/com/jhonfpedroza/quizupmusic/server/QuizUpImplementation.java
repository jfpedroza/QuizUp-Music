package com.jhonfpedroza.quizupmusic.server;

import com.jhonfpedroza.quizupmusic.interfaces.QuizUpInterface;
import com.jhonfpedroza.quizupmusic.models.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        Logger.getLogger(QuizUpImplementation.class.getName()).log(Level.INFO, name + " has logged in.");
        return user;
    }

    @Override
    public void logOut(User user) throws RemoteException {
        users.remove(user);

        Logger.getLogger(QuizUpImplementation.class.getName()).log(Level.INFO, user.getName() + " has logged out.");
        Logger.getLogger(QuizUpImplementation.class.getName()).log(Level.INFO, "User count: " + users.size());
    }
}
