package multiplayer;

import java.util.ArrayList;
import java.lang.Boolean;

public class CommandParser {
	ArrayList<String> chat = new ArrayList<>();
	
	Client client;
	
	public String[] currentMove = null;
	
	public String currentTurn = "";
	
	Boolean moveValidated = null;
	
	public CommandParser(Client client) {
		this.client = client;
	}
	
	public void Parse() {
		if (client == null) return;
		
		for (String command : client.popFullStack()) {
			String[] splitCommand = command.split(":", 1);
			
			switch (splitCommand[0]) {
			case "CHAT":
				System.out.println(splitCommand[1]);
				break;
			case "SERVER_MOVE":
				currentMove = splitCommand[1].split(",");
				break;
			case "BOARD":
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
	}
}
