package com.company;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable{
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private String hostname;
    private int PORT;

    public Client(String hostname, int PORT) {
        this.hostname = hostname;
        this.PORT = PORT;
    }

    @Override
    public void run() {
            try{
                socket = new Socket(hostname,PORT);

                outputStream = socket.getOutputStream();
                objectOutputStream = new ObjectOutputStream(outputStream);

                inputStream = socket.getInputStream();
                objectInputStream = new ObjectInputStream(inputStream);

//                Message msg = new Message("Hello", "r0m3l");

//                objectOutputStream.writeObject(msg);
//                objectOutputStream.flush();
                while (socket.isConnected()){
                    Message msg = null;
                    LMessage lmsg = null;
                    try{
                        msg = (Message) objectInputStream.readObject();

                        if(msg != null){
                            if(msg.getUser().getUserType().equals("server")){
                                if(msg.getMsg().equals("Login done")){
                                    System.out.println("Login successful");
                                    Main.isLoggedIn = true;
                                    Main.user = (User) objectInputStream.readObject();
                                } else {
                                    System.err.println("Login Failed");
                                }
                            }
                        }

                    } catch (java.lang.ClassNotFoundException e){
                        e.printStackTrace();
                    }
                }

            } catch (IOException e){
                System.err.println("Couldn't connect to the server");
                e.printStackTrace();
            }
    }

    public  <T extends Message> void send(T message){
        try{
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
        } catch (IOException e){
            System.err.println("Unable to send message");
        }
    }
}
