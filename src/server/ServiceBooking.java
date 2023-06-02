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

			System.out.println("=========================================");
			System.out.println("Requete de " + this.client.getLocalSocketAddress() + " Numero document : " + numeroDocument + " Numero abonne : " + numeroAbonne);

			if (media_library.documentNotExist(numeroDocument)) {
				System.err.println("Le document " + numeroDocument + " n'existe pas dans la mediatheque");
				out.println("Le document n'existe pas dans la mediatheque");
				return;
			}
			if (media_library.abonneNotExist(numeroAbonne)) {
				System.err.println("L'abonne " + numeroAbonne + " n'existe pas");
				out.println("L'abonne n'existe pas");
				return;
			}



			try {
				media_library.reservation(numeroDocument, numeroAbonne);
				System.out.println("Reservation reussie");
				out.println("Reservation reussie");
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
