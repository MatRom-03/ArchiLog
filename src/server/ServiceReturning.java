package server;

import sqlData.Media_library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

// ce service envoie les questions au client
// on pourrait lors du premier envoi lister les cours ou il reste de la place
// mais il faudrait coder les \n en ##
// (et decoder du cote client)

public class ServiceReturning implements IService {

// **** ressources partagees : les Documents **************
	private static Media_library media_library = null;

	/**
	 * Set the media library
	 * @param media_library the media library to set
	 */
	public static void setDocument(Media_library media_library) {
		ServiceReturning.media_library = media_library;
	}

// ********************************************************

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

			out.println("Bienvenue dans la bibliotheque, vous etes connecte au serveur de retour");
			out.println("Tapez le numero du document souhaitees"); // first question
			int numeroDocument = Integer.parseInt(in.readLine());

			System.out.println("Requete de " + this.client.getInetAddress() + " Numero document : " + numeroDocument);

			if (media_library.documentNotExist(numeroDocument)) {
				System.out.println("Le document n'existe pas");
				out.println("Le document n'existe pas");
				return;
			}



			try {
				media_library.retour(numeroDocument);
				System.out.println("Retour reussie");
				out.println("Retour reussie");
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
