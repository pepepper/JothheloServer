package JOthheloServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Game extends Thread {
	private String room;
	String	pass;
	private Socket host, guest;
	private BufferedReader hostreader, guestreader;
	private PrintWriter hostwriter, guestwriter;
	private int x, y;
	int locked = 0, ended = 0;

	Game(Socket sock, String string, String string2) {
		host = sock;
		x = Integer.parseInt(string);
		y = Integer.parseInt(string2);
		room = String.valueOf(System.currentTimeMillis());
		try {
			hostreader = new BufferedReader(new InputStreamReader(host.getInputStream()));
			hostwriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(host.getOutputStream())));
			hostwriter.print("SUCCESS " + room);
			hostwriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void ready() {
			hostwriter.print("READY");
			guestwriter.print("READY");
			hostwriter.flush();
			guestwriter.flush();
	}

	void login(Socket sock, String string) {
		if (locked == 1 && pass.compareTo(string) == 0) {
			guest = sock;
			try {
				guestreader = new BufferedReader(new InputStreamReader(guest.getInputStream()));
				guestwriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(guest.getOutputStream())));
				guestwriter.print("SUCCESS " + x + " " + y);
				guestwriter.flush();
				ready();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				sock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	void login(Socket sock) {
		if (locked == 0) {
			guest = sock;
			try {
				guestreader = new BufferedReader(new InputStreamReader(guest.getInputStream()));
				guestwriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(guest.getOutputStream())));
				guestwriter.print("SUCCESS " + x + " " + y);
				guestwriter.flush();
				ready();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				sock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void run() {
		String request;
		while (true) {
			try {
				request = hostreader.readLine();

				if (request.contains("SETPASSWORD")) {
					String requests[] = request.split(" ");
					pass = requests[1];
					locked = 1;
					hostwriter.print("SUCCESS");
					hostwriter.flush();
					continue;
				} else if (request.contains("CLOSED")) {
					guestwriter.print(request);
					guestwriter.flush();
					ended = 1;
					return;
				} else {
					guestwriter.print(request);
					guestwriter.flush();
					request = guestreader.readLine();
					hostwriter.print(request);
					hostwriter.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
				ended = 1;
				return;
			}
			try {
				request = guestreader.readLine();

				if (request.contains("CLOSED")) {
					hostwriter.print(request);
					hostwriter.flush();
					ended = 1;
					return;
				} else {
					hostwriter.print(request);
					hostwriter.flush();
					request = hostreader.readLine();
					guestwriter.print(request);
					guestwriter.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
				ended = 1;
				return;
			}

		}
	}
}
