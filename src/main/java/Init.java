
import Models.Session;
import Models.User;
import Screens.AuthScreen;
import Screens.Dashboard;
import Services.Auth;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

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

    }

    void generateInitialFiles() {
        final InitPaths<?>[] initialFiles =  {
                new InitPaths<List<User>>("users.dat", new ArrayList<>()),
                new InitPaths<List<Session>>("sessions.dat", new ArrayList<>()),

        };
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

class InitPaths<T> {
    String pathName;
    T initialObject;


    public InitPaths(String pathName, T initialObject) {
        this.pathName = pathName;
        this.initialObject = initialObject;
    }
}


