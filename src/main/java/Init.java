
import Models.User;
import Screens.AuthScreen;
import Screens.Dashboard;
import Services.Auth;

import java.io.IOException;
import java.nio.file.*;

public class Init {
    // Load config path:
    Path appDirectory;
    Init(Path appDirectory) throws IOException {
        this.appDirectory =  appDirectory;
        loadPaths();
        startApp();
    }
    void loadPaths() throws IOException {
        // create app directory if not exists
        Files.createDirectories(appDirectory);
        // create empty session file if not exist
        try{
            Files.createFile(appDirectory.resolve("ACTIVE_SESSION"));
        }catch(FileAlreadyExistsException ignored){}
//        Path resolvedPath =  appDirectory.resolve("users.dat");

//        Files.writeString(resolvedPath, "\ntest2", StandardOpenOption.CREATE, StandardOpenOption.APPEND);

//        ObjectOutputStream outputStream = new ObjectOutputStream(Files.newOutputStream(resolvedPath));
//        final List<User> users = new ArrayList<>();
//        users.add(new User("Shazin Rahman", "shazin", "shazin@gmail.com", "123456"));
//        outputStream.writeObject(users);

//        try {
//            ObjectInputStream input = new ObjectInputStream(Files.newInputStream(resolvedPath));
//            List<User> user = (List<User>) input.readObject();
//            IO.println("read object " + user.size());
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }

    }
    void startApp() throws IOException {
        Path sessionFile = appDirectory.resolve("ACTIVE_SESSION");
        String session = Files.readString(sessionFile);
        // check if user with this session exists
        Auth auth = new Auth();
        User currentUser = auth.getUserWithSession(session);
        if (currentUser == null){
            // Now current session found show auth screen
            AuthScreen.show();
            return;
        } else{
            Dashboard.show();
        }
        // show user screen;
    }

}
