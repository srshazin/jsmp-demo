package Screens;


import Models.User;
import Services.Auth;
import Shared.Utils;

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
        IO.println("Login to your account");
        String username = IO.readln("Username: ");
        String password = IO.readln("Password: ");

        try {
            User loggedUser = new Auth().login(username, password);
            if (loggedUser == null){
                Utils.clearConsole();
                IO.println("Invalid Credentials. Try again!");
                loginScreen();
            }
            Dashboard.show();
        } catch (Exception e) {
            IO.println("Error while logging in. Error:  " + e.getMessage());
            loginScreen();
        }
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
            IO.println("User created.");
            Dashboard.show();
            // Call dashboard;
        } catch (Exception e) {
            IO.println("User reg failed. Error: " + e.getMessage());
            registrationScreen();
        }

    }
}
