package pokemonbattleServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class PKServer {

	private static int ip = 4563;
	public static ArrayList<ClientServiceThread> map = new ArrayList<ClientServiceThread>(2);

	public static String Data0 = "";
	public static String Data1 = "";
	public static boolean both = false;
	public static boolean S0 = false;
	public static boolean S1 = false;
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		ServerSocket m_ServerSocket = new ServerSocket(ip);
		int id = 0;
		while (true) {
			System.out.println("Waiting for connections on port " + ip);
			Socket clientSocket = m_ServerSocket.accept();
			ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id);
			cliThread.start();
			map.add(id, cliThread);
			id++;
		}
	}
}

class ClientServiceThread extends Thread {
	Socket clientSocket;
	BufferedReader in;
	PrintWriter out;
	int clientID = 0;
	boolean running = true;

	ClientServiceThread(Socket s, int i) {
		clientSocket = s;
		clientID = i;
	}

	public void run() {
		System.out.println(
				"Accepted Client : ID - " + clientID + " : Address - " + clientSocket.getInetAddress().getHostName());
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

			while (running == true) {

				if (in.ready()) {
					String clientCommand = in.readLine();
					System.out.println("Client Says :" + clientCommand);

					if (clientID == 0) {
						if (clientCommand != "") {
							PKServer.Data0 = clientCommand;
							System.out.println("Data 0 " + PKServer.Data0);
							PKServer.S0 = false;
						}

						if (PKServer.Data0 != "" && PKServer.Data1 != "") {
							PKServer.both = true;
						}

					} else if (clientID == 1) {
						if (clientCommand != "") {
							PKServer.Data1 = clientCommand;
							System.out.println("Data 1 " + PKServer.Data1);
							PKServer.S1 = false;
						}

						if (PKServer.Data0 != "" && PKServer.Data1 != "") {
							PKServer.both = true;
						}

					}
					System.out.println(PKServer.both + " 0 = " + PKServer.Data0 + " 1 = " + PKServer.Data1);

				}
				// check for false both
				if (PKServer.S0 && PKServer.S1) {
					PKServer.both = false;
				}

				if (PKServer.both) {
					switch (clientID) {
					case 0:
						if (!PKServer.S0) {
							out.println(PKServer.Data1);
							out.flush();
							System.out.println("Sent " + PKServer.Data1 + " to 0");
							PKServer.Data1 = "";
							PKServer.S0 = true;
						}
						break;
					case 1:
						if (!PKServer.S1) {
							out.println(PKServer.Data0);
							out.flush();
							System.out.println("Sent " + PKServer.Data1 + " to 1");
							PKServer.Data0 = "";
							PKServer.S1 = true;
						}
						break;

					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}