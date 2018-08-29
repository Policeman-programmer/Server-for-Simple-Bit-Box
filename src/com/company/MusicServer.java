package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class MusicServer {

    ArrayList <ObjectOutputStream> clientOutputStreamList;
    //ArrayList clientObjectList;

    public class ClientHandler implements Runnable{
        ObjectInputStream in;
        Socket socket;
        public ClientHandler( Socket clientSoket) {
            try {
                in = new ObjectInputStream(clientSoket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            Object massage;
            Object sequance;
            try {
                while ((massage = in.readObject())!=null){
                    sequance = in.readObject();
                    System.out.println("Я получил: "+ massage);
                    tellEveryone(massage,sequance);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //создает сокет сервера
    public void go() {
        clientOutputStreamList = new ArrayList();
        try {
            ServerSocket serverSocket = new ServerSocket(4242);
            while (true) {
                System.out.println("жду подключения");
                Socket clientSoket = serverSocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(clientSoket.getOutputStream());
                clientOutputStreamList.add(out);

                Thread t = new Thread(new ClientHandler(clientSoket));
                t.start();
                System.out.println("Network Esteblished");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //отсылает каждому клиенту сообщение
    private void tellEveryone(Object massage, Object sequance) {
        Iterator it = clientOutputStreamList.iterator();
        while (it.hasNext()){
            try {
                ObjectOutputStream out = (ObjectOutputStream)it.next();
                out.writeObject(massage);
                out.writeObject(sequance);
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
       new MusicServer().go();
    }
}
