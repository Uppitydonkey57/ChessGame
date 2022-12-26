package multiplayer;

import org.joml.*;

import java.util.ArrayList;
import java.lang.Boolean;

public class CommandParser {
	ArrayList<String> chat = new ArrayList<String>();
	
	Client client;
	
	public Vector2f startPosition = null;
	public Vector2f currentPosition = null;
	
	public String currentTurn = null;
	
	public Boolean moveValidated = null;
	
	public CommandParser(Client client) {
		this.client = client;
	}
	
	public void Parse() {
		if (client == null) return;
		for (String command : client.getFullStack()) {;
			String[] splitCommand = command.split(":");
			
			switch (splitCommand[0]) {
			case "CHAT":
				String message = splitCommand[1] + ": " + splitCommand[2];
				System.out.println(message);
				chat.add(message);
				break;
			case "INT_MOVE":
				String[] parts = splitCommand[1].split(",");
				startPosition = new Vector2f(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]));
				currentPosition = new Vector2f(Float.parseFloat(parts[2]), Float.parseFloat(parts[0]));
				break;
			case "BOARD":
				System.out.println(splitCommand[1]);
				break;
			case "VALID_MOVE":
				moveValidated = true;
				break;
			case "INVALID_MOVE":
				moveValidated = false;
				break;
			case "TURN":
				moveValidated = null;
				currentTurn = splitCommand[1];
				break;
			default:
				System.err.println("Server Error!!!! the command:" + splitCommand[0] + " does not exist!!! inform Nate of this herecy!!!!");
			}
		}
		
		client.commands.clear();
	}
}
