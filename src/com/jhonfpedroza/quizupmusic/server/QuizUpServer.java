package com.jhonfpedroza.quizupmusic.server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuizUpServer {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            QuizUpImplementation quizUp = new QuizUpImplementation();
            Naming.bind("quizUp", quizUp);
            System.out.println("Server running!");
        } catch (RemoteException | AlreadyBoundException | MalformedURLException ex) {
            Logger.getLogger(QuizUpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
