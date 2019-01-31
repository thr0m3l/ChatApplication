package com.company;

import sun.misc.IOUtils;

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
                        try{
                            msg = (Message) objectInputStream.readObject();
                        } catch (EOFException eof){
                            System.err.println("Logged out");
                            break;
                        }

                        if(msg != null){
                            if(msg.getUser().getUserType().equals("server")){
                                if(msg.getMsg().equals("Login done")){
                                    System.out.println("Login successful");
                                    Main.isLoggedIn = true;
                                    Main.user = (User) objectInputStream.readObject();
                                    System.out.println(Main.user.getUserName());
                                } else if(msg.getMsg().equals("C Message")){
                                    System.out.println("Receiving CMessage...");
                                    CMessage cMessage = (CMessage)objectInputStream.readObject();
                                    handleCMessage(cMessage);
                                } else{
                                    System.out.println(msg.getMsg());
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

    private void handleCMessage(CMessage cMessage) {
        File file = new File(cMessage.getFileName());
        try{
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            DataInputStream dis = new DataInputStream(fis);

            try{
                dis.readFully(bytes);
                dis.close();
            } catch (IOException e){
                System.err.println("Unable to convert the file to byte array");
            }

            cMessage.setFile(bytes);

            try{
                objectOutputStream.writeObject(cMessage);
            } catch (IOException e){
                System.err.println("Unable to send CMessage");
                e.printStackTrace();
            }

        } catch (FileNotFoundException fnf){
            System.err.println("File not found");
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
