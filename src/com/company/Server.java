package com.company;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int PORT = 8818;
    private static final ArrayList<User> users = new ArrayList<>();
    private static ServerSocket listener;
    public static void main(String[] args) {
        System.out.println("The chat server is running");
        users.add(new User("r0m3l","1234","admin"));
        users.add(new User("tanzim","1234","admin"));

        try{
            listener = new ServerSocket(PORT);
        } catch (IOException e){
            System.err.println("Unable to initiate server socket");
            e.printStackTrace();
        }

        try{
            while (true){
                Handler serverHandler = new Handler(listener.accept());
                Thread serverThread = new Thread(serverHandler);
                serverThread.start();
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try{
                listener.close();
            } catch (IOException e1){
                e1.printStackTrace();
            }
        }
    }
    private static class Handler implements Runnable {
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("Attempting to connect a user . . .");
            try{
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                objectOutputStream = new ObjectOutputStream(outputStream);
                objectInputStream = new ObjectInputStream(inputStream);

                while (socket.isConnected()){
                    LMessage lMessage = null;
                    lMessage = (LMessage) objectInputStream.readObject();
                    if(lMessage!=null){
                        boolean found = false;
                        for(User user : users){
                            if(lMessage.getUserName().equals(user.getUserName()) &&
                            lMessage.getPassword().equals(user.getPassword()) &&
                            lMessage.getUserType().equals(user.getUserType())){
                                objectOutputStream.writeObject(new Message("Login done","server"));
                                found = true;
                            }
                        }
                        if(!found){
                            objectOutputStream.writeObject(new Message("Login failed","server"));
                        }
                    }




                    Message message = null;
                    message = (Message) objectInputStream.readObject();

                    if(message != null){
                        System.out.println(message.getUser() + " : " + message.getMsg());
                    }


                }
            } catch (IOException e){
                e.printStackTrace();
            } catch (java.lang.ClassNotFoundException e1){
                e1.printStackTrace();
            }
        }
    }
}


