package server;

import sqlData.Media_library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class ServiceReturning implements IService {
	private final Socket client;

	/**
	 * Constructor of the class
	 * @param socket the socket of the client
	 */
	public ServiceReturning(Socket socket) {
		this.client = socket;
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);

			// envoi du nombre de ligne du catalogue
			out.println(0);

			out.println("Bienvenue dans la bibliotheque, vous etes connecte au serveur de retour");
			out.println("Tapez le numero du document souhaitees"); // first question
			int numeroDocument = Integer.parseInt(in.readLine());

			System.out.println("=========================================");
			System.out.println("Requete de " + this.client.getLocalSocketAddress());

			if (Media_library.documentNotExist(numeroDocument)) {
				System.err.println("Le document n'existe pas");
				out.println("Le document n'existe pas");
				return;
			}

			if(Media_library.isNotBorrowed(numeroDocument)) {
				System.err.println("Le document n'a pas été emprunté");
				out.println("Le document n'a pas été emprunté");
				return;
			}

			try {
				Media_library.retour(numeroDocument);
				System.out.println("Retour reussie");
				out.println("Retour reussi");
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

		} catch (IOException e) {
			// Fin du service d'inversion
		}

		try {
			client.close();
		} catch (IOException e2) {
		}
	}

	@Override
	public String toString() {
		return "Inversion de texte";
	}
}
