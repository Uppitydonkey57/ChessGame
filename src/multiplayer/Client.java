package multiplayer;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
	private Socket s;
	private boolean isDebug;

	private BufferedReader reader;
	private PrintWriter writer;
	
	private boolean shouldStop = false;

	ArrayList<String> commands = new ArrayList<String>();

	public Client(boolean isDebug) throws UnknownHostException, IOException {
		this.isDebug = isDebug;
		if (isDebug)
			return;

		s = new Socket("localhost", 9999);
		InputStreamReader in = new InputStreamReader(s.getInputStream());
		reader = new BufferedReader(in);
		writer = new PrintWriter(s.getOutputStream());
	}
	
	

	public String read() {
		String str = "";
		try {
			str = reader.readLine();
		} catch (IOException e) {
			System.out.println(e);
			return e.getMessage();
		}
		return str;
	}

	String popCommand() {
		String buffer = commands.get(commands.size());
		commands.remove(commands.size() - 1);
		return buffer;
	}
	
	public ArrayList<String> getFullStack() {
		ArrayList<String> currentCommands = commands;
		return currentCommands;
	}

	public void write(String message) {
		writer.println(message);
		writer.flush();
	}



	@Override
	public void run() {
		while (!shouldStop) {
			String str = "";
			try {
				str = reader.readLine();
				if (str != null) {
					commands.add(str);
				}
			} catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
	}
	
	public void end() {
		shouldStop = true;
	}
}
