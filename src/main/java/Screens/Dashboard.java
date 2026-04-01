package Screens;

import Models.User;
import Services.Auth;

public class Dashboard {
    private static User activeUser;
    public static void show(){
        getActiveUser();
        dashboard();

    }

    private static void getActiveUser(){
        Auth auth =  new Auth();
        activeUser = auth.getCurrentActiveUser();
    }

    private static void dashboard(){
        IO.println("Welcome, " + activeUser.fullName + "!");
    }
}
