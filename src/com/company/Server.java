package com.company;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Server {
    private static final int PORT = 8818;
    private static ArrayList<User> users = new ArrayList<>();
    private static HashMap<User, ObjectOutputStream> oos = new HashMap<>();
    private static ArrayList<String> onlineUsers = new ArrayList<>();
    private static ServerSocket listener;
    public static void main(String[] args) {
        System.out.println("The chat server is running");
        users.add(new User("r0m3l","1234","admin"));
        users.add(new User("tanzim","1234","admin"));
        users.add(new User("hossain","1234","user"));
        users.add(new User("itachi","uchiha","user"));

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
        private User currentUser;
        private User server;

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
                    try{
                        message = (Message) objectInputStream.readObject();
                    } catch (SocketException SE){
                        System.err.println("User disconnected : " + currentUser.getUserName());
                        break;
                    }

                    if(message != null){
                        String[] tokens = message.getMsg().split("#");
                        if(tokens[0].equals("L")){
                            LMessage lMessage = new LMessage(tokens[1],tokens[2],tokens[3]);
                            currentUser = handleLogin(lMessage);
                            Message msg = new Message();
                            msg.setUser(new User("server"));

                            if(currentUser != null){
                                msg.setMsg("Login done");
                            } else{
                                msg.setMsg("Login failed");
                            }
                            objectOutputStream.writeObject(msg);
                            objectOutputStream.writeObject(currentUser);
                        } else if (tokens[0].equals("B")){
                            BMessage bMessage = new BMessage();
                            bMessage.setMsg(tokens[1]);
                            handleBMessage(bMessage);
                        } else if(tokens[0].equals("S")){
                            SMessage sMessage = new SMessage();
                            sMessage.setMsg(tokens[1]);
                            handleSMessage(sMessage);
                        } else if(tokens[0].equals("C")){


                            CMessage cMessage = new CMessage();

                            if(tokens.length == 4){
                                cMessage.setFileName(tokens[3]);
                            }
                            cMessage.setRecipient(tokens[1]);
//                            cMessage.setFileName(tokens[3]);
                            cMessage.setMsg(tokens[2]);
                            cMessage.setUser(currentUser);
//                            objectOutputStream.writeObject(cMessage);
//                            handleCMessage();
                            if(tokens.length == 3) sendTo(cMessage);

                            if(tokens.length == 4){
                                Message msg = new Message();
                                msg.setUser(new User("server"));
                                msg.setMsg("C Message");
                                objectOutputStream.writeObject(msg);
                                objectOutputStream.writeObject(cMessage);
                                cMessage = (CMessage) objectInputStream.readObject();
                                sendTo(cMessage);
                            }


                            //Test code


                        }
                    }


                }
            } catch (IOException e){
                e.printStackTrace();
            } catch (java.lang.ClassNotFoundException e1){
                e1.printStackTrace();
            }
        }

        public User handleLogin (LMessage lMessage){
            User tempUser = null;
            for(User user : users){
                if(user.getUserName().equals(lMessage.getUserName()) &&
                    user.getPassword().equals(lMessage.getPassword()) &&
                user.getUserType().equals(lMessage.getUserType())){
                    System.out.println(lMessage.getUser().getUserName());
                    oos.put(lMessage.getUser(),objectOutputStream);
                    tempUser = user;
                    onlineUsers.add(lMessage.getUser().getUserName());
                    break;
                }
            }
            return tempUser;

        }


        public void sendTo(CMessage cMessage) throws IOException{
            boolean found = false;

            for(User user : oos.keySet()){
                if(user.getUserName().equals(cMessage.getRecipient())){
                    Message newMsg = new Message();
                    newMsg.setUser(new User("server"));
                    newMsg.setMsg("C Message");
                    oos.get(user).writeObject(newMsg);
                    oos.get(user).writeObject(cMessage);
                    found = true;
                    break;
                }
            }
            if(!found){
                cMessage.setMsg("Recipient not online");
            }


        }
        public void handleBMessage (BMessage bMessage) throws IOException{
            Message msg = new Message();
            msg.setMsg(currentUser.getUserName() + " : " + bMessage.getMsg() );
            msg.setUser(new User("server"));
            if(currentUser.getUserType().equals("admin")){
                Set<User> userSet = oos.keySet();
                for(User user: userSet){
                    if(!user.getUserName().equals(currentUser.getUserName())){
                        oos.get(user).writeObject(msg);
                    }
                }

            } else if(currentUser.getUserType().equals("user")){
                msg.setUser(new User("server"));
                msg.setMsg("Can't send BMessage, you must be an admin to send BMessage");
                objectOutputStream.writeObject(msg);
            }
        }
        public void handleSMessage(SMessage sMessage) throws IOException{
            Message msg = new Message();
            msg.setUser(new User("server"));
            msg.setMsg("Active users: ");
            if(sMessage.getMsg().equals("show")){
                Message showMessage = new Message();
                showMessage.setUser(new User("server"));
                showMessage.setMsg("Active users: ");
                objectOutputStream.writeObject(showMessage);

                for(String string : onlineUsers){
                    msg.setMsg(string);
                    msg.setUser(new User("server"));
                    objectOutputStream.writeObject(msg);
                }
            } else if(sMessage.getMsg().equals("logout")){
                BMessage bMessage = new BMessage();
                bMessage.setMsg("Logging out");
                bMessage.setUser(currentUser);
                handleBMessage(bMessage);

                users.remove(currentUser);
                oos.remove(currentUser,oos.get(currentUser));
//                currentUser = null;
                socket.close();
                objectOutputStream.close();
                objectInputStream.close();
                inputStream.close();
                outputStream.close();
            }
        }
    }
}


