package server.services;

import protocols.Reader;
import protocols.Writer;
import server.log.LogError;
import server.logic.EmailManager;
import server.logic.Media_library;

import java.io.IOException;
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

			String message = "Bienvenue dans la bibliotheque, vous etes connecte au serveur de retour" +
					"\nTapez le numero du document souhaitees";
			Writer.writeString(client, message);

			int numeroDocument = Reader.readInt(client);

			System.out.println("=========================================");
			System.out.println("Requete de " + this.client.getLocalSocketAddress());

			if (Media_library.documentNotExist(numeroDocument)) {
				new LogError("Le document n'existe pas");
				Writer.writeString(client, "Le document n'existe pas");
				return;
			}

			if(Media_library.isNotBorrowed(numeroDocument)) {
				new LogError("Le document n'a pas été emprunté");
				Writer.writeString(client, "Le document n'a pas été emprunté");
				return;
			}

			try {
				Media_library.retour(numeroDocument);
				Writer.writeString(client, "Retour reussi");

				EmailManager.sendEmails(Media_library.getWaitingList(numeroDocument), numeroDocument);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}

		} catch (IOException e) {
			new LogError("Erreur lors de la communication avec le client");
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