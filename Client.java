package com.codejava.networking.chat.gui;
//included imports needed

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {
//made my gui parts along with the bufferedReader, Print Writer and Socket and initalizing variable for username
    private static JTextField tf = new JTextField(20);
    private  static PrintWriter pw = null;
    protected static String username;
    private static BufferedReader br=null;
    private  static Socket cSocket;
    
//main method which runs the client class 
    public static void main(String[] args) throws Exception {

    	//I set my socket to take in a host and specific port number to run on.
    	//I then set my printWriter to get the sockets output stream.
    	//Then i set br to be my buffered Reader that takes in the sockets input stream
        cSocket = new Socket("127.0.0.1", 6060);
        pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(cSocket.getOutputStream())), true);
        br = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
        
    //This is all my gui code to create the look and feel of the client
    // I made buttons and text feilds to set profile pictures and send messgaes along with changing the messageBox colour.
        JFrame mainFrame = new JFrame("Client Chat Room");
        final JTextArea messageBox = new JTextArea(50, 50);
        final JTextField typeUsername = new JTextField(10);
        final JButton sendButton = new JButton("Send");
        final JButton usernameButton = new JButton("Set");
        final JLabel userName = new JLabel("Username:");
        final JButton logout = new JButton("Logout");
        JPanel usernamePanel = new JPanel();
        JPanel messagePanel = new JPanel();
        JPanel profilePanel = new JPanel();
        JLabel profilePic = new JLabel("");
        JButton changePic = new JButton("Change picture");
        JButton changeColour = new JButton("Change colour");
        
      //set all false so they cant be used before the username is set
      //Then set the size of messageBox and chatFrame
        sendButton.setEnabled(false);
        messageBox.setEditable(false);
        tf.setEditable(false);
        logout.setVisible(false);

        messageBox.setSize(500, 100);
        mainFrame.setSize(500, 400);
        
        //I added the profilePic label , changePic button and changeColour button to the top profile panel
        profilePanel.add(profilePic, BorderLayout.WEST);
        profilePanel.add(changePic, BorderLayout.CENTER);
        profilePanel.add(changeColour, BorderLayout.EAST);

        //I added the usernName label, typeUsername field, username button and logout button to the username panel.
        usernamePanel.add(userName);
        usernamePanel.add(typeUsername);
        usernamePanel.add(usernameButton);
        usernamePanel.add(logout);
        
        //I added the text field to write messages and the send message button to the messagePanel
        messagePanel.add(tf);
        messagePanel.add(sendButton);

        //Then i added all the panels at different areas using borderLayout to the chatFrame along with the messageBox to see the messages
        mainFrame.add(messagePanel, BorderLayout.SOUTH);
        mainFrame.add(messageBox, BorderLayout.CENTER);
        mainFrame.add(usernamePanel, BorderLayout.EAST);
        mainFrame.add(profilePanel, BorderLayout.NORTH);
        
        //This lets the colour button change the messageBox to cyan.
        changeColour.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//sets background
						messageBox.setBackground(Color.CYAN);
					}
				});
        
     //This lets us use mouse listener to let us pick a picture file and save it and place in client chat room as a profile pic.
    	//extension is set to a file extension with either "JPEG", "jpg" or "jpeg" to let us choose a file with this extention
    	//fileChooser is set to a new file chooser. This is then used to set the extension specified in "filter".
    	//if the file choosen is allowed then the name is set to the selected files path name and this then shows a message
    	//with the files name.
    	//An image icon of this image is now created and the profilePic label now holds the image choosen but resized to fit.
    	//else a message "Look later is shown
        changePic.addMouseListener(new MouseAdapter() {

        	public void mouseClicked(MouseEvent arg0) {
                FileNameExtensionFilter extension = new FileNameExtensionFilter("JPEG file", "jpg", "jpeg");
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(extension);
                int response = fileChooser.showOpenDialog(null);
                try {
                    if (response == JFileChooser.APPROVE_OPTION) {
                        String name = fileChooser.getSelectedFile().getPath();
                        JOptionPane.showMessageDialog(null, name);
                        ImageIcon icon = new ImageIcon(name);
                        profilePic.setIcon(ResizeImage(icon));
                        }
                    
                     else {
                        JOptionPane.showMessageDialog(null, " Look Later");
                    }
                } catch (Exception e) {
                  
                    e.printStackTrace();
                }
            }
        	//This method takes an ImageIcon and resizes it.
        	//An image image is set to the icon passed into the method. A newImg is then set to this img but scaled.
        	//Another imageIcon newIconImage is now this new resized image and this image is returned
        	 public ImageIcon ResizeImage(ImageIcon icon)
        	    {
        	        
        	        Image imgage = icon.getImage();
        	        Image newImg = imgage.getScaledInstance(40, 50,Image.SCALE_SMOOTH);
        	        ImageIcon newIconImage = new ImageIcon(newImg);
        	        return newIconImage;
        	    }
          
        });

//this allows our send button to send the message that was typed in the tf text field. It send it along with the username
 //this is then printed to the printWriter and the text field is set back to ""
        sendButton.addActionListener(
        		new ActionListener () {
        		public void actionPerformed(ActionEvent e) {
            String sMsg = tf.getText() +" - " +username;
            pw.println(sMsg);
            tf.setText("");
        		}
        });
 //This lets the text field do the same thing as send button if user wanted it sends message written with username and print it 
        tf.addActionListener(
        		new ActionListener () {
        		public void actionPerformed(ActionEvent e) {
            String sMsg = tf.getText() +" - " +username;
            pw.println(sMsg);
            tf.setText("");
        		}
        });
   //This allows us to set username. The username is gotten from the text typed in the typeUsername field. This then cant
   //be changed once submitted. But now the text field to type messages can be used along with the send button and the logout.
   //else a message to enter a username pops up.
        usernameButton.addActionListener(new ActionListener() {

            
            public void actionPerformed(ActionEvent e) {
                if (typeUsername.getText().length() != 0) {
                    username = typeUsername.getText();
                    typeUsername.setEditable(false);
                    typeUsername.setEnabled(false);
                    tf.setEditable(true);
                    messageBox.setText("");
                    sendButton.setEnabled(true);
                    usernameButton.setEnabled(false);
                    usernameButton.setVisible(false);
                    logout.setVisible(true);
                    pw.println(username +"  " +cSocket+ " is Now in chat");
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter username now", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        //Logout button logs a user out. First we print using pw to the messageBox that the user is loged out and a bye message
        //then we close the print wirter and the buffered Reader and the socket and exit the whole thing.
        
        logout.addActionListener(
        		new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    pw.println("Is gone"+ " - "+username );
                    pw.println("GoodBye");
                    pw.close();
                    br.close();
                    cSocket.close();
                    System.exit(0);
                } catch (Exception ex) {
              ex.printStackTrace();
                }
            }
        });

        //set frame as visable
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

        //String s is set to read bufferedReader 
        //while this doesnt equal "Goodbye" we append the line to show in messageBox
        //we set s to read line again and if it ends with the username thewn we replace it with "User -"
        String s = br.readLine();
        while (!s.equals("GoodBye")) {
            messageBox.append(s + "\n");
            s = br.readLine();
            if (s.endsWith(" - " +username)) {
                s = s.replace(username + " - ", "User - ");
            }
        }

   
    }
}