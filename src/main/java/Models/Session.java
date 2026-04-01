package Models;

import java.io.Serializable;

public class Session implements Serializable {
    public String id;
    public String session;

    public Session(String id, String session){
        this.id = id;
        this.session = session;
    }
}
