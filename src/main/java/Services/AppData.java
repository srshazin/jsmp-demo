package Services;

import java.nio.file.Path;

public class AppData {
    private Path path;
    private static AppData instance;

    private AppData(Path path){
        this.path = path;
    }

    public static void init(Path dataPath){
        if (instance != null){
            throw new IllegalStateException("Services.AppData already initialized");
        }
        instance = new AppData(dataPath);
    }

    public static AppData getInstance() {
        if (instance == null){
            throw  new IllegalStateException("Services.AppData is not initialized");
        }
        return instance;
    }

    public Path getAppDataPath(){
        return  this.path;
    }



}
