package pokemonbattleServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import pokemonFramework.Bundle;
import pokemonFramework.Profile;
import pokemonFramework.Server;

public class PKServer {
	// Managing Users Joining

	public static final String Gamemode = "none";
	public static final String Name = "Test Server Please Ignore";

	private static int ip = 4563;
	public static ArrayList<ClientServiceThread> map = new ArrayList<ClientServiceThread>(8);

	// public static Bundle Client0 = null;
	// public static Bundle Client1 = null;
	// public static Bundle Client2 = null;
	// public static Bundle Client3 = null;
	// public static Bundle Client4 = null;
	// public static Bundle Client5 = null;
	// public static Bundle Client6 = null;
	// public static Bundle Client7 = null;
	public static ArrayList<Bundle> data = new ArrayList<Bundle>(8);
	public static int[] Players = new int[2];
	public static int[] Spectators = new int[6];
	public static String Data0 = "";
	public static String Data1 = "";
	public static boolean both = false;
	// public static boolean S0 = false;
	// public static boolean S1 = false;

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		ServerLogicThread logicThread = new ServerLogicThread();
		logicThread.start();
		ServerSocket m_ServerSocket = new ServerSocket(ip);
		while (true) {
			System.out.println("Waiting for connections on port " + ip + " " + (map.size()));
			Socket clientSocket = m_ServerSocket.accept();
			ClientServiceThread cliThread = new ClientServiceThread(clientSocket);
			cliThread.start();
			map.add(map.size(), cliThread);
		}
	}
}

class ServerLogicThread extends Thread {
	// Manages Backend Logic

	boolean running = true;

	ServerLogicThread() {

	}

	public void run() {
		while (running) {
			if (PKServer.Data0 != "" && PKServer.Data1 != "") {
				PKServer.both = true;
				System.out.println("Both are heree");
			}
		}
	}

}

class ClientServiceThread extends Thread {
	// Manages Client-Server Connections
	Socket clientSocket;
	ObjectInputStream in;
	ObjectOutputStream out;
	int clientID = PKServer.map.size() - 1;
	boolean running = true;
	boolean updateClient = false;
	Profile clientProfile = null;

	ClientServiceThread(Socket s) {
		clientSocket = s;
		clientID = PKServer.map.size();
	}

	public void run() {
		System.out.println("Accepted Client: ID - " + clientID + " , Address - " + clientSocket.getInetAddress().getHostName());

		try {
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			in = new ObjectInputStream(clientSocket.getInputStream());

			while (running == true && !clientSocket.isClosed()) {
				out.reset();
				Object clientCommand = in.readObject();

				if (clientCommand != null) {

					if (clientProfile != null) {
						System.out.println("Client " + clientProfile.getName() + " Says :" + clientCommand.toString());
					} else
						System.out.println("Client " + clientID + " Says :" + clientCommand.getClass());

					// info
					if (clientCommand != null && clientCommand instanceof String) {
						String str = clientCommand.toString();
						if (str.equalsIgnoreCase("info")) out.writeObject(new Server(PKServer.Name, PKServer.Gamemode, InetAddress.getLocalHost()));
						if (str.equalsIgnoreCase("update")) {
							ArrayList<Profile> prof = new ArrayList<Profile>(PKServer.data.size());
							for (int i = 0; i < PKServer.data.size(); i++) {
								if (PKServer.data.get(i).getProfiles().size() != 0) prof.add(i, PKServer.data.get(i).getSenderProfile());
							}
							Bundle pBundle = new Bundle(prof);
							// System.out.println(pBundle.toString() + "BundleID");
							out.writeObject(pBundle);
						}
						out.flush();
						if (clientProfile != null) {
							System.out.println(str + "Given To " + clientProfile.getName());
						} else
							System.out.println(str + "Given To " + clientID);

					}

					// bundle
					/*
					 * if (clientCommand != null && clientCommand instanceof Bundle) { Bundle
					 * clientBundle = (Bundle) clientCommand;
					 * System.out.println(clientBundle.getSenderProfile().getName() +
					 * "Client Name"); clientProfile = clientBundle.getProfiles().get(0);
					 * PKServer.data.add(clientID, clientBundle); // got the profile // Confirmed
					 * Connection out.writeObject(new Server(PKServer.Name, PKServer.Gamemode,
					 * InetAddress.getLocalHost())); if (clientProfile != null) {
					 * System.out.println("Info Given To " + clientProfile.getName()); }else
					 * System.out.println("Info Given To " + clientID); // updateAll(); }
					 */

					// Profile
					if (clientCommand != null && clientCommand instanceof Profile) {
						Profile clientProfile = (Profile) clientCommand;
						System.out.println(clientProfile.getName() + "Client Name");
						this.clientProfile = clientProfile;
						PKServer.data.add(clientID, new Bundle(clientProfile));
						// got the profile
						// Confirmed Connection
						out.writeObject(new Server(PKServer.Name, PKServer.Gamemode, InetAddress.getLocalHost()));
						if (this.clientProfile != null) {
							System.out.println("Info Given To " + clientProfile.getName());
						} else
							System.out.println("Info Given To " + clientID);
						// updateAll();
					}

					// other
					/*
					 * if (clientID == 0) { if (clientCommand != "") { System.out.println("Data 1 "
					 * + clientCommand); // PKServer.S1 = false; }
					 * 
					 * if (PKServer.Data0 != "" && PKServer.Data1 != "") { PKServer.both = true; }
					 * 
					 * }
					 */

					// check for false both
					/*
					 * if (PKServer.S0 && PKServer.S1) { PKServer.both = false; }
					 * 
					 * if (PKServer.both) { switch (clientID) { case 0: if (!PKServer.S0) {
					 * out.writeObject(new String(PKServer.Data1)); out.flush();
					 * System.out.println("Sent " + PKServer.Data1 + " to 0"); PKServer.Data1 = "";
					 * PKServer.S0 = true; } break; case 1: if (!PKServer.S1) { out.writeObject(new
					 * String(PKServer.Data0)); out.flush(); System.out.println("Sent " +
					 * PKServer.Data1 + " to 1"); PKServer.Data0 = ""; PKServer.S1 = true; } break;
					 * 
					 * } }
					 */

					// update the client
					if (updateClient) {
						// updateAll();
						updateClient = false;
					}

				}
			}
			PKServer.map.remove(this);

		} catch (Exception e) {
			//e.printStackTrace();
			PKServer.map.remove(this);
			running = false;
			System.out.println("Client Disconnected: ID - " + clientID + " , Address - " + clientSocket.getInetAddress().getHostName());
		}
	}

	/*
	 * public void updateAll() { for (int i = 0;i < PKServer.map.size(); i++) { try
	 * { Profile[] prof = new Profile[PKServer.data.size()]; for (int j = 0; j <
	 * PKServer.data.size(); j++) { prof[j] = PKServer.data.get(i).getProfiles()[0];
	 * } out.writeObject(new Bundle(prof)); } catch (IOException e) {
	 * e.printStackTrace(); //TAG } }
	 * 
	 * }
	 */ // MAKE THIS WORK
}