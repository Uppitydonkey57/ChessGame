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

	ArrayList<String> commands;

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
			System.out.println(str);
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
	
	public ArrayList<String> popFullStack() {
		ArrayList<String> currentCommands = commands;
		commands.clear();
		return currentCommands;
	}

	public void write(String message) {
		writer.println(message);
		writer.flush();
	}



	@Override
	public void run() {
		while (true) {
			String message = read();
			if (message != null) {
				commands.add(message);
			}
		}
	}
}
