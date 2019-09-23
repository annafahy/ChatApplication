package com.codejava.networking.chat.gui;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
//Server Class 

public class Server implements Runnable {
	//Set an queue list of strings 
	//Set an arrayList of print writers.
	protected static Queue<String> msgQ = new Queue<String>();
	protected static ArrayList<PrintWriter> pWri = new ArrayList<PrintWriter>();

	//intialize socket, printWriter and BufferedReader
    private Socket socket;
    private static PrintWriter printW;
    private BufferedReader b;

 //constructor takes in the socket and sets socket , printwriter and bufferedReader
 //arraylist of printWriters add the initalized printW to the list.
    public Server(Socket socket) throws Exception {
        this.socket = socket;
        this.printW = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        pWri.add(this.printW);
        this.b = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    //runs the server class
    //this sets st to read the lines in bufferedReader and if this doesnt equal goodbye from the client side we 
    ///put the string into the Queue and set st to read another line.
    //then the pWri array list trys to remove this socket from the list.
    //finally if not the bufferedReader is closed and so is the printWriter and the socket.
 
    public void run() {
        try {
            try {
                String st = b.readLine();

                while (!st.equals("GoodBye")) {

                    msgQ.enqueue(st);
                    st = b.readLine();
                }
            } catch (Exception e) {
            	e.printStackTrace();
            }
            pWri.remove(this.socket);
        } catch (Exception ex) {
        	ex.printStackTrace();
   } 
        finally {
            try {
               b.close();
               printW.close();
               this.socket.close();
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
    }
 
//main method for server
    public static void main(String[] args) throws Exception {

//creates a new thread
//starts the thread
    
       ChatThread msgD = new ChatThread();
        msgD.start();
//new server socket is created and Socket, Thread and server are set the null.
        ServerSocket sSocket = new ServerSocket(6060);
        Socket s = null;
        Thread thread = null;
        Server c = null;
//for the amount of clients less than 5 the socket is set to the serverSocket
 //this then accepts and Server class creates a new Server object with this serverSocket.
 //new thread is created with this server object and its started
        for (int i = 0; i < 5; i++) {
        	
            s = sSocket.accept();
            System.out.println("Client" +Client.username + " " + "is connected"); 
            c = new Server(s);
            thread = new Thread(c);
            thread.start();
        }
    }
    
}

//Thread class 
class ChatThread extends Thread {
 
   //to run the class 
    public void run() {
       
 //infinate loop that dequeues the Queue list and iterates through the printWriter arrayList and then prints out the messages being dequed from Queue.
        while (true) {
 
            String msgTh = Server.msgQ.dequeue();
 
            for (PrintWriter pw : Server.pWri) {
                pw.println(msgTh);
            }
        }
    }
}