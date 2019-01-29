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
