package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;
import multiplayer.*;

public class Chat extends Thread {
    Frame frame = new Frame("Application");
    
    Scanner scanner = new Scanner(System.in);
    
    Client client;
    
    public Chat(Client client) {
    	this.client = client;
    }
    
    public void run() {
	    
	    List chatList = new List(10, false);
	    TextField text = new TextField();
	    Button addText = new Button("Send Message");
	    
	    chatList.setBounds(20, 50, 460, 500);
	    addText.setBounds(20, 570, 100, 30);
	    text.setBounds(150, 570, 200, 30);
	    
	    frame.add(chatList);
	    frame.add(text);
	    frame.add(addText);
	    
	    addText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				text.setText("");
				if (client == null) return;
				String message = text.getText();
				client.write("CHAT:" + message);
			}
	    });
	    
    	frame.setTitle("Chat!");
	    frame.setSize(500, 630);
	    frame.setLocationRelativeTo(null);
	    
	    frame.setResizable(false);
	    
	    frame.setLayout(null);
	    
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
            }
        });
        
        frame.setVisible(true);
    }
    
    public void close() {
    	frame.dispose();
    }
}
