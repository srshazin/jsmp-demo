package Screens;

import Models.User;
import Services.Auth;
import Shared.Utils;

import java.util.*;
import java.util.function.Function;

public class Dashboard {
    private static User activeUser;
    static Menu[]  menu = {
            new Menu("Sales History", "hist",  Dashboard::salesHistory),
            new Menu("Record New Sale", "rec",  NewSalesRecordScreen::show),
            new Menu("Manage Inventory", "invt",  NewSalesRecordScreen::show),
            new Menu("Logout", "logout",  Dashboard::logoutScreen),
    };
    public static void show(){
        getActiveUser();
        dashboard();

    }

    private static void getActiveUser(){
        Auth auth =  new Auth();
        activeUser = auth.getCurrentActiveUser();
        if (activeUser == null){
            IO.println("Unauthorized. Invalid session.");
            System.exit(1);
        }
    }

    private static void dashboard(){

        IO.println("Welcome, " + activeUser.fullName + "!");
        IO.println("\nCashier Menu:");

        for (int i = 0; i < menu.length; i++){
            IO.println("[" + (i+1) + "]\t(" + menu[i].command + ") " + menu[i].displayText);
        }
        Runnable displayFunction = null;
        while (true){
            // read keyboard
            String cmd = IO.readln("> ");
            // verify whether cmd is valid
            for (int i = 0; i < menu.length; i++){
                if (Objects.equals(cmd, menu[i].command)){
                    displayFunction = menu[i].displayFunction;
                    break;
                }
            }
            // check if display function is null
            if (displayFunction == null){
                IO.println("Invalid coommand!");
                continue;
            }
            break;
        }
        // Display function is not null thus we can run display fn safely
        displayFunction.run();

    }

    static void salesHistory(){
        IO.println("Sale history dashboard:");
    }

    static void logoutScreen(){
        Utils.clearConsole();
        IO.println("User: " + activeUser.username + " is logged out.\n");
        new Auth().logout();
    }

}


class Menu{
    String displayText;
    String command;
    Runnable displayFunction;


    public Menu(String displayText, String command, Runnable displayFunction) {
        this.displayText = displayText;
        this.command = command;
        this.displayFunction = displayFunction;
    }
}