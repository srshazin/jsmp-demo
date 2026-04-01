package Models;

import java.io.Serializable;

public class User implements Serializable {
    public  String id;
    public String fullName;
    public String username;
    public String email;
    public String password;
    public String session;

    public User(String id ,String fullName, String username, String email, String password){
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
