package server.services;

import documents.exceptions.DocumentAlreadyBookedException;
import documents.exceptions.DocumentAlreadyBorrowedException;
import protocols.Reader;
import protocols.Writer;
import server.log.LogError;
import server.logic.Media_library;
import subscribers.SubscriberTooYoungException;

import java.io.IOException;
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
			Writer.writeString(client, Media_library.getCatalogue());

			String message = "Bienvenue dans la bibliotheque, vous etes connecte au serveur d'emprunt" +
					"\nTapez le numero du document souhaitees";
			Writer.writeString(client, message);

			int numeroDocument = Reader.readInt(client);

			message = "Tapez votre numero d'abonne";
			Writer.writeString(client, message);

			int numeroAbonne = Reader.readInt(client);

			System.out.println("=========================================");
			System.out.println("Requete de " + this.client.getLocalSocketAddress());

			if (Media_library.documentNotExist(numeroDocument)) {
				new LogError("Le document " + numeroDocument + " n'existe pas dans la mediatheque");
				Writer.writeString(client, "Le document n'existe pas dans la mediatheque");
				return;
			}

			if (Media_library.abonneNotExist(numeroAbonne)) {
				new LogError("L'abonne " + numeroAbonne + " n'existe pas");
				Writer.writeString(client, "L'abonne n'existe pas");
				return;
			}

			try {
				Media_library.emprunt(numeroDocument, numeroAbonne);
				Writer.writeString(client,"Emprunt reussie");
			} catch (SQLException e) {
				throw new RuntimeException(e);
			} catch (DocumentAlreadyBookedException | SubscriberTooYoungException | DocumentAlreadyBorrowedException e) {
				new LogError(e.toString());
				Writer.writeString(client,e.toString());
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