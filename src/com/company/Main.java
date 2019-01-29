package com.company;

import java.util.Scanner;

public class Main {

    public static boolean isLoggedIn = false;
    private static String userName = "r0m3l";

    public static void main(String[] args) {
        Client client = new Client("localhost",8818);
        Thread t = new Thread(client);
        t.start();

        Scanner scanner = new Scanner(System.in);

        while (!isLoggedIn){
            System.out.println("Message Format: L#UserName#Password#UserType");
            String input = scanner.nextLine();
            String[] tokens = input.split("#");

            LMessage lmsg = new LMessage(tokens[1],tokens[2],tokens[3]);
            client.send(lmsg);
            try{
                Thread.sleep(100);
            } catch (InterruptedException e){
                e.printStackTrace();
            }


        }

        while (isLoggedIn){
            System.out.println("Write a message to a server: ");
            String msg = scanner.nextLine();
            Message message = new Message(msg,userName);
            client.send(message);



        }
    }
}
