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

// ce service envoie les questions au client
// on pourrait lors du premier envoi lister les cours ou il reste de la place
// mais il faudrait coder les \n en ##
// (et decoder du cote client)

public class ServiceBooking implements IService {

// **** ressources partagees : les Documents **************
	private static Media_library media_library = null;

	/**
	 * Set the media library
	 * @param media_library the media library to set
	 */
	public static void setDocument(Media_library media_library) {
		ServiceBooking.media_library = media_library;
	}

// ********************************************************

	private final Socket client;

	/**
	 * Constructor of the class
	 * @param socket the socket of the client
	 */
	public ServiceBooking(Socket socket) {
		this.client = socket;
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);

			// envoi du nombre de ligne du catalogue
			out.println(media_library.getNbLignesCatalogue());
			// envoi du catalogue
			out.println(media_library.getCatalogue());
			out.println("Bienvenue dans la bibliotheque, vous etes connecte au serveur de reservation");
			out.println("Tapez le numero du document souhaitees"); // first question
			int numeroDocument = Integer.parseInt(in.readLine());
			out.println("Tapez votre numero d'abonne"); // second question
			int numeroAbonne = Integer.parseInt(in.readLine());

			System.out.println("Requete de " + this.client.getInetAddress() + " Numero document : " + numeroDocument + " Numero abonne : " + numeroAbonne);

			if (media_library.abonneNotExist(numeroAbonne)) {
				System.out.println("L'abonne " + numeroAbonne + "n'existe pas");
				out.println("L'abonne n'existe pas");
				return;
			}
			if (media_library.documentNotExist(numeroDocument)) {
				System.out.println("Le document " + numeroDocument + " n'existe pas dans la mediatheque");
				out.println("Le document n'existe pas dans la mediatheque");
				return;
			}


			try {
				media_library.reservation(numeroDocument, numeroAbonne);
				System.out.println("Reservation reussie");
				out.println("Reservation reussie");
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} catch (DocumentNotAvailableException | SubscriberTooYoungException e) {
				System.out.println(e.toString());
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
