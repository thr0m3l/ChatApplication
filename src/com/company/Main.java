package com.company;

import java.util.Scanner;

public class Main {

    public static boolean isLoggedIn = false;
    public static User user = null;

    public static void main(String[] args) {
        Client client = new Client("localhost",8818);
        Thread t = new Thread(client);
        t.start();

        Scanner scanner = new Scanner(System.in);

        while (!isLoggedIn){
            System.out.println("Message Format: L#UserName#Password#UserType");
            String input = scanner.nextLine();
            Message msg = new Message();
            msg.setMsg(input);

            client.send(msg);
            try{
                Thread.sleep(100);
            } catch (InterruptedException e){
                e.printStackTrace();
            }


        }

        while (isLoggedIn){
//            System.out.println("Write a message to the server: ");
            String msg = scanner.nextLine();
            Message message = new Message(msg,user);
            client.send(message);
        }
    }
}
