package Services;

import Models.Session;
import Models.User;
import Shared.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Auth {

    private Path appDataPath = AppData.getInstance().getAppDataPath();
    Path usersPath = appDataPath.resolve("users.dat");




    private List<User> getExistingUsers() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(usersPath));
            List<User> users = (List<User>) inputStream.readObject();
            inputStream.close();
            return  users;
        } catch (Exception e) {
            if (e instanceof NoSuchFileException){
                IO.println("users.dat doesn't exist, creating one...");
                try {
                    ObjectOutputStream outputStream = new ObjectOutputStream(
                            Files.newOutputStream(appDataPath.resolve("users.dat"))
                    );
                    // write an empty array
                    outputStream.writeObject(new ArrayList<User>());
                }catch(Exception e2){
                    IO.println("Failed creating sessions.dat file. Error: " + e2.getMessage());
                    return  null;
                }
            }
            return new ArrayList<>();
        }

    }

    public User registerUser(User user) throws Exception {
        // load existing users
        List<User> existingUsers = getExistingUsers();
        if (existingUsers == null) {
            existingUsers = new ArrayList<>();
        }

        // check if username is in registered user
        if (!existingUsers.isEmpty()){
            List<User> matchedUsers = existingUsers.stream()
                    .filter(user1 -> Objects.equals(user1.username, user.username))
                    .toList();
            if (!matchedUsers.isEmpty()){
                throw  new Exception("User with this username already exists");
            }
        }
        // Username is valid
        // Register this user
        existingUsers.add(user);

        // save the record into the local file
        ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(usersPath));
        outputStream.writeObject(existingUsers);
        // Create new session for this user
        String _session = Utils.generate32CharString();
        createNewSessionEntry(user.id, _session);

        // save this session as current session
        Files.writeString(appDataPath.resolve("ACTIVE_SESSION"), _session, StandardOpenOption.WRITE);

        outputStream.close();


        // return the new user
        return user;

    }

    public User getUserWithSession(String session){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(
                    appDataPath.resolve("sessions.dat")
            ));
            List<Session> sessions = (List<Session>) inputStream.readObject();
            inputStream.close();
            if (sessions == null) return  null;

            List<Session> _session = sessions.stream()
                    .filter(session1 -> Objects.equals(session1.session, session))
                    .toList();
            if (_session.isEmpty()){
                return  null;
            }
            // now we have a valid session
            String matchedSessionUserId = _session.getFirst().id;
            final List<User> existingUsers = getExistingUsers();

           List<User> matchedUser =   existingUsers.stream()
                   .filter(u-> Objects.equals(u.id, matchedSessionUserId))
                   .toList();

           if (matchedUser.isEmpty()) return null;

           return matchedUser.getFirst();
        } catch (Exception e) {
            if (e instanceof NoSuchFileException){
                IO.println("sessions file doesn't exist");
                try {
                    ObjectOutputStream outputStream = new ObjectOutputStream(
                            Files.newOutputStream(appDataPath.resolve("sessions.dat"))
                    );
                    // write an empty array
                    outputStream.writeObject(new ArrayList<Session>());
                }catch(Exception e2){
                    IO.println("Failed creating sessions.dat file. Error: " + e2.getMessage());
                    return  null;
                }
            }

            return  null;
        }
    }

    private void createNewSessionEntry(String id, String sessStr) {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(
                    Files.newInputStream(appDataPath.resolve("sessions.dat"))
            );
            List<Session> _sessions = (List<Session>) inputStream.readObject();
            _sessions.add(new Session(id, sessStr));

            // Write it back
            ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(appDataPath.resolve("sessions.dat")));
            outputStream.writeObject(_sessions);
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User getCurrentActiveUser(){
        // First read the active session
       try {
           Path sessionFile = appDataPath.resolve("ACTIVE_SESSION");
           String session = Files.readString(sessionFile);

           // find user with this session
           return getUserWithSession(session);

       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }
}
