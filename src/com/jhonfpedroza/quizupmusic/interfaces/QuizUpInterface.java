package com.jhonfpedroza.quizupmusic.interfaces;

import com.jhonfpedroza.quizupmusic.models.Game;
import com.jhonfpedroza.quizupmusic.models.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface QuizUpInterface extends Remote {

    User logIn(String name) throws RemoteException;
    void logOut(User user) throws RemoteException;
    ArrayList<User> getOnlineUsers() throws RemoteException;
    Game challenge(User challenger, User challenged) throws RemoteException;
    ArrayList<Game> getChallenges(User user) throws RemoteException;
    void setGameStatus(Game game, Game.Status status) throws RemoteException;
    Game getGame(long id) throws RemoteException;
}
