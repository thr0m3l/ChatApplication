package com.company;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    private static final int PORT = 8818;
    private static final ArrayList<User> users = new ArrayList<>();
    private static final HashMap<User, ObjectOutputStream> oos = new HashMap<>();
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

                System.out.println("User connected!");

                while (socket.isConnected()){


                    //Message Processing

                    Message message = null;
                    message = (Message) objectInputStream.readObject();

                    if(message != null){
                        String[] tokens = message.getMsg().split("#");
                        if(tokens[0].equals("L")){
                            LMessage lMessage = new LMessage(tokens[1],tokens[2],tokens[3]);
                            boolean loggedIn = handleLogin(lMessage);
                            Message msg = new Message();
                            msg.setUser(new User("server"));

                            if(loggedIn){
                                msg.setMsg("Login done");
                            } else{
                                msg.setMsg("Login failed");
                            }

                            objectOutputStream.writeObject(msg);
                        } else if (tokens[0].equals("B")){

                        }
                    }


                }
            } catch (IOException e){
                e.printStackTrace();
            } catch (java.lang.ClassNotFoundException e1){
                e1.printStackTrace();
            }
        }

        public boolean handleLogin (LMessage lMessage){
            boolean isFound = false;

            for(User user : users){
                if(user.getUserName().equals(lMessage.getUserName()) &&
                    user.getPassword().equals(lMessage.getPassword()) &&
                user.getUserType().equals(lMessage.getUserType())){
                    isFound = true;
                    oos.put(user,objectOutputStream);
                }
            }

            return isFound;
        }
        public void sendTo (CMessage cMessage){
            
        }
    }
}


