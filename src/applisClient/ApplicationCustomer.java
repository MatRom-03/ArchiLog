package applisClient;

import applisClient.templates.BookingTemplate;
import applisClient.templates.LoaningTemplate;
import applisClient.templates.ReturningTemplate;

import java.io.*;
import java.net.Socket;

public class ApplicationCustomer {

	private final static int PORTBOOKING 	= 1000;
	private final static int PORTLOANING	= 1001;
	private final static int PORTRETURNING 	= 1002;

	public static void main(String[] args) {
		BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
		final int PORT = Integer.parseInt(args[1]);
		final String HOST = args[0];

		Socket clientSocket = null;
		try {
			clientSocket = new Socket(HOST, PORT);

			System.out.println("Connecte au serveur " + clientSocket.getInetAddress() + ":"+ clientSocket.getPort());

			if (PORT==PORTBOOKING) {
				BookingTemplate.booking(clientSocket, clavier);
			} else if (PORT==PORTLOANING) {
				LoaningTemplate.loaning(clientSocket, clavier);
			} else if (PORT==PORTRETURNING) {
				ReturningTemplate.returning(clientSocket, clavier);
			}

		}
		catch (IOException e) { System.err.println("Fin du service !"); }
		// Refermer dans tous les cas la socket
		try { if (clientSocket != null) clientSocket.close(); }
		catch (IOException e2) { }
	}
}