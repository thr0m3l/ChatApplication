package com.company;

import java.util.Scanner;

public class Main {

    private static boolean isLoggedIn = true;
    private static String userName = "r0m3l";

    public static void main(String[] args) {
        Client client = new Client("localhost",8818);
        Thread t = new Thread(client);
        t.start();

        Scanner scanner = new Scanner(System.in);
        while (isLoggedIn){
            System.out.println("Write a message to a server: ");
            String msg = scanner.nextLine();
            Message message = new Message(msg,userName);
            client.send(message);



        }
    }
}
