package com.codejava.networking.chat.gui;
import java.util.ArrayList;

//Queue class to do enqueue and dequeue methods in synchronization
public class Queue<T> {
 
	//Setd an array list
    ArrayList<T> queueMessage = new ArrayList<T>();
 
    //synchronized enqueue method to take in a message and add to the arrayList and then notify other threads once this is done.
    public synchronized void enqueue(T message) {
        this.queueMessage.add(message);
        notify();
    }
 
    //synchronized dequeue method , while the arrayList is empty wait for threads, Else remove the from the queue.
    public synchronized T dequeue() {
        while (queueMessage.isEmpty()) {
            try {
                wait();
            } catch (Exception ex) {
            		ex.printStackTrace();
            }
        }
      
        return queueMessage.remove(0);
    }
}