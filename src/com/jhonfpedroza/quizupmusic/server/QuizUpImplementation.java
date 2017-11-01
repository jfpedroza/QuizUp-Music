package com.jhonfpedroza.quizupmusic.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jhonfpedroza.quizupmusic.interfaces.QuizUpInterface;
import com.jhonfpedroza.quizupmusic.models.Game;
import com.jhonfpedroza.quizupmusic.models.Question;
import com.jhonfpedroza.quizupmusic.models.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuizUpImplementation extends UnicastRemoteObject implements QuizUpInterface {

    private static Logger logger = Logger.getLogger(QuizUpImplementation.class.getName());

    private List<User> users;
    private List<User> dummies;
    private List<Game> games;
    private List<Question> questions;

    QuizUpImplementation() throws RemoteException {
        super();
        users = new ArrayList<>();
        dummies = new ArrayList<>();
        games = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            long id = System.currentTimeMillis();
            User user = new User(id, "Dummy " + (i + 1));
            dummies.add(user);
        }

        questions = readQuestions();
    }

    @Override
    public User logIn(String name) throws RemoteException {
        User user;
        Optional<User> opt = users.stream().filter(u -> u.getName().equals(name)).findFirst();

        if (opt.isPresent()) {
            user = opt.get();
            if (user.isLogged()) {
                logger.log(Level.WARNING, name + " is already logged in.");
                return null;
            } else {
                user.setLogged(true);
            }
        } else {
            long id = System.currentTimeMillis();
            user = new User(id, name);
            user.setLogged(true);
            users.add(user);
            for (User dummy: dummies) {
                user.addGame(new Game(System.currentTimeMillis(), user, dummy, Game.Status.FINISHED));
            }

            logger.log(Level.INFO, "New user: " + name);
        }

        logger.log(Level.INFO, name + " has logged in.");
        return user;
    }

    @Override
    public void logOut(User user) throws RemoteException {
        user = users.get(users.indexOf(user));
        user.setLogged(false);

        logger.log(Level.INFO, user.getName() + " has logged out.");
        logger.log(Level.INFO, "Online users: " + users.stream().filter(User::isLogged).count());
    }

    @Override
    public ArrayList<User> getOnlineUsers() throws RemoteException {
        ArrayList<User> online = new ArrayList<>();
        users.stream().filter(User::isLogged).forEach(online::add);

        return online;
    }

    @Override
    public Game challenge(User challenger, User challenged) throws RemoteException {
        Game game = new Game(challenger, challenged, Game.Status.CHALLENGED);
        games.add(game);

        logger.log(Level.INFO, challenger.getName() + " challenged " + challenged.getName());
        return game;
    }

    @Override
    public ArrayList<Game> getChallenges(User user) throws RemoteException {
        ArrayList<Game> challenges = new ArrayList<>();
        games.stream().filter(game -> game.getPlayer2().equals(user) && game.getStatus() == Game.Status.CHALLENGED).forEach(challenges::add);

        return challenges;
    }

    @Override
    public void setGameStatus(Game game, Game.Status status) throws RemoteException {
        Optional<Game> opt = games.stream().filter(g -> g.equals(game)).findFirst();
        opt.ifPresent(g -> {
            logger.log(Level.INFO, String.format("Changing status of game %s from %s to %s", g, g.getStatus(), status));
            g.setStatus(status);
            if (status == Game.Status.ONGOING) {
                g.setQuestions(getRandomQuestions(7));
            }
        });
    }

    @Override
    public Game getGame(long id) throws RemoteException {
        return games.stream().filter(game -> game.getId() == id).findFirst().orElse(null);
    }

    private List<Question> readQuestions() {
        String path = "questions.json";
        InputStream stream = QuizUpImplementation.class.getResourceAsStream(path);
        if (stream != null) {
            InputStreamReader reader = new InputStreamReader(stream, Charset.forName("UTF8"));
            JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
            try {
                reader.close();
                JsonArray array = json.getAsJsonArray("questions");
                return Question.readArray(array);
            } catch (IOException ex) {
                Logger.getLogger(QuizUpImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.err.println("Couldn't find file: " + path);
        }

        return null;
    }

    private ArrayList<Question> getRandomQuestions(int amount) {
        ArrayList<Question> selected = new ArrayList<>();
        ArrayList<Question> copy = new ArrayList<>(questions);
        Collections.shuffle(copy);
        copy.stream().limit(amount).forEach(selected::add);

        return selected;
    }
}
