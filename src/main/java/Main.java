import Services.AppData;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    static void main() throws IOException {
        Path appDirectory = Path.of(System.getProperty("user.home"), ".local", "share", "xyzsm");
        // Init Services.AppData
        AppData.init(appDirectory);
        new Init(appDirectory);
    }
}
