package com.jhonfpedroza.quizupmusic.server;

import com.jhonfpedroza.quizupmusic.interfaces.QuizUpInterface;
import com.jhonfpedroza.quizupmusic.models.Game;
import com.jhonfpedroza.quizupmusic.models.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuizUpImplementation extends UnicastRemoteObject implements QuizUpInterface {

    private List<User> users;
    private List<User> dummies;

    QuizUpImplementation() throws RemoteException {
        super();
        users = new ArrayList<>();
        dummies = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            long id = System.currentTimeMillis();
            User user = new User(id, "Dummy " + (i + 1));
            dummies.add(user);
        }
    }

    @Override
    public User logIn(String name) throws RemoteException {
        User user;
        Optional<User> opt = users.stream().filter(u -> u.getName().equals(name)).findFirst();

        if (opt.isPresent()) {
            user = opt.get();
            if (user.isLogged()) {
                Logger.getLogger(QuizUpImplementation.class.getName()).log(Level.WARNING, name + " is already logged in.");
                return null;
            } else {
                user.setLogged(true);
            }
        } else {
            long id = System.currentTimeMillis();
            user = new User(id, name);
            users.add(user);
            for (User dummy: dummies) {
                user.addGame(new Game(System.currentTimeMillis(), user, dummy));
            }

            Logger.getLogger(QuizUpImplementation.class.getName()).log(Level.INFO, "New user: " + name);
        }

        Logger.getLogger(QuizUpImplementation.class.getName()).log(Level.INFO, name + " has logged in.");
        return user;
    }

    @Override
    public void logOut(User user) throws RemoteException {
        user = users.get(users.indexOf(user));
        user.setLogged(false);

        Logger.getLogger(QuizUpImplementation.class.getName()).log(Level.INFO, user.getName() + " has logged out.");
        Logger.getLogger(QuizUpImplementation.class.getName()).log(Level.INFO, "Online users: " + users.stream().filter(User::isLogged).count());
    }

    @Override
    public ArrayList<User> getOnlineUsers() throws RemoteException {
        ArrayList<User> online = new ArrayList<>();
        users.stream().filter(User::isLogged).forEach(online::add);

        return online;
    }
}
