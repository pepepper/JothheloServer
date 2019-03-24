import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class Acception extends Thread {
	Socket sock;
	ArrayList<Game> game;

	public Acception(Socket sc, ArrayList<Game> g) {
		sock = sc;
		game = g;
	}

	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String command = reader.readLine();
			String[] commands = command.split(" ");
			if (command.contains("ROOM")) {
				Game temp = new Game(sock, commands[1], commands[2]);
				temp.start();
				game.add(temp);
			} else if (command.contains("LOGIN")) {
				for (Game g : game) {
					if (g.room.compareTo(commands[1])==0&&g.ended==0) {
						if (command.contains("PASSWORD")) {
							g.login(sock, commands[3]);
						} else {
							g.login(sock);
						}
					}
				}
			}
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
