package main;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.List;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import multiplayer.Client;

public class Launcher {
	public Client client = null;
	private boolean isOnline;
	
	public ActionListener launchAction;
	
	public Launcher() {
		
		Frame frame = new Frame();  
	        
        // frame size 300 width and 300 height    
        frame.setSize(400,300);  
        
        Button startGame = new Button("Launch");
        
        startGame.addActionListener(launchAction);
        
        startGame.setBounds(320, 260, 70, 20);
        
        frame.add(startGame);
        
        // setting the title of frame  
        frame.setTitle("Launcher");   
            
        // no layout  
        frame.setLayout(null);   
    
        // setting visibility of frame  
        frame.setVisible(true);
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose(); // Releases native screen resources
            }
        });
        
        frame.setVisible(true);
	        
	}
	
	void setup() throws UnknownHostException, IOException {
		if (isOnline) {
			client = new Client(false);
		}
	}
	
}
