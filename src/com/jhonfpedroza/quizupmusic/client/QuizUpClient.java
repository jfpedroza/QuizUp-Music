package com.jhonfpedroza.quizupmusic.client;

import com.jhonfpedroza.quizupmusic.interfaces.QuizUpInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuizUpClient {

    private static QuizUpInterface quizUp;

    public static void main(String[] args) {
        try {
            quizUp = (QuizUpInterface) Naming.lookup("rmi://localhost/quizUp");
            System.out.println(quizUp.test());
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(QuizUpClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
