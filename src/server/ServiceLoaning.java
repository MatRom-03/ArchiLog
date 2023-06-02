package server;

import documents.DocumentNotAvailableException;
import sqlData.Media_library;
import subscribers.SubscriberTooYoungException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class ServiceLoaning implements IService {
	private final Socket client;

	/**
	 * Constructor of the class
	 * @param socket the socket of the client
	 */
	public ServiceLoaning(Socket socket) {
		this.client = socket;
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);

			// envoi du nombre de ligne du catalogue
			out.println(Media_library.getNbLignesCatalogue());
			// envoi du catalogue
			out.println(Media_library.getCatalogue());

			out.println("Bienvenue dans la bibliotheque, vous etes connecte au serveur d'emprunt");
			out.println("Tapez le numero du document souhaitees"); // first question
			int numeroDocument = Integer.parseInt(in.readLine());
			out.println("Tapez votre numero d'abonne"); // second question
			int numeroAbonne = Integer.parseInt(in.readLine());

			System.out.println("=========================================");
			System.out.println("Requete de " + this.client.getLocalSocketAddress());

			if (Media_library.abonneNotExist(numeroAbonne)) {
				System.err.println("L'abonne n'existe pas");
				out.println("L'abonne n'existe pas");
				return;
			}
			if (Media_library.documentNotExist(numeroDocument)) {
				System.err.println("Le document n'existe pas");
				out.println("Le document n'existe pas");
				return;
			}

			try {
				Media_library.emprunt(numeroDocument, numeroAbonne);
				System.out.println("Emprunt reussie");
				out.println("Emprunt reussie");
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} catch (DocumentNotAvailableException | SubscriberTooYoungException e) {
				System.err.println(e.toString());
				out.println(e.toString());
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
