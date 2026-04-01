package Screens;


import Models.User;
import Services.Auth;

import java.util.Objects;
import java.util.UUID;

public class AuthScreen {
    public static void show(){
        chooseAuthOption();
    }
    private static void chooseAuthOption(){
        String choice =  IO.readln("Select option [login/register]: ");
        if (Objects.equals(choice, "login")){
            loginScreen();
        }
        else if (Objects.equals(choice, "register")) {
            registrationScreen();
        }
        else {
            IO.println("Invalid choice.");
            // again show the same screen
            chooseAuthOption();
        }
    }
    private static void loginScreen(){
        IO.println("Presenting login screen");
    }
    private static void registrationScreen(){
        String username, fullName, email, password;

        username = IO.readln("Username: ");
        fullName = IO.readln("Full Name: ");
        email = IO.readln("Email: ");
        password = IO.readln("Password: ");

        // create a new user
        final String newUserId =UUID.randomUUID().toString();
        Auth auth = new Auth();

        try {
            User newUser = auth.registerUser(new User(
                    newUserId,
                    fullName,
                    username,
                    email,
                    password
            ));
            Dashboard.show();
            // Call dashboard;
        } catch (Exception e) {
            e.printStackTrace();
            IO.println("User reg failed");
        }

    }
}
