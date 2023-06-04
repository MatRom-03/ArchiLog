package server.services;

import documents.exceptions.DocumentAlreadyBookedException;
import documents.exceptions.DocumentAlreadyBorrowedException;
import documents.GrandChamanException;
import protocols.Reader;
import protocols.Writer;
import server.log.LogError;
import server.logic.Media_library;
import subscribers.SubscriberTooYoungException;

import java.io.*;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.sql.SQLException;


public class ServiceBooking implements IService {
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

			Writer.writeString(client, Media_library.getCatalogue());

			String message = "Bienvenue dans la bibliotheque, vous etes connecte au serveur de reservation\n" +
					"Tapez le numero du document souhaitees";
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
				Media_library.reservation(numeroDocument, numeroAbonne);
				Writer.writeString(client, "Reservation reussie");

				Writer.writeBoolean(client, false);
				Writer.writeBoolean(client, false);

			} catch (SQLException e) {
				throw new RuntimeException(e);
			} catch (GrandChamanException e) {

				new LogError(e.toString());
				Writer.writeString(client, e.toString());

				Writer.writeBoolean(client, true);
				Writer.writeFile(client, new File(FileSystems.getDefault().getPath("src/sqlAndData/RickRoll.mp3").normalize().toAbsolutePath().toString()));

				retryBooking(numeroDocument, numeroAbonne);

				Writer.writeBoolean(client, false);

			} catch (SubscriberTooYoungException | DocumentAlreadyBookedException e) {
				new LogError(e.toString());

				Writer.writeString(client, e.toString());
				Writer.writeBoolean(client, false);
				Writer.writeBoolean(client, false);
			} catch (DocumentAlreadyBorrowedException e) {
				new LogError(e.toString());
				Writer.writeString(client, e.toString());
				Writer.writeBoolean(client, false);
				Writer.writeBoolean(client, true);
				message = "Le document est deja emprunte, voulez-vous recevoir un alerte lorsqu'il sera disponible ? (O/N)";
				Writer.writeString(client, message);
				boolean alerte = Reader.readBoolean(client);
				if (alerte) {
					Media_library.addAlert(numeroDocument, numeroAbonne);
					Writer.writeString(client, "Alerte ajoutee");
				} else {
					Writer.writeString(client, "Alerte non ajoutee");
				}
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

	/**
	 * Retry the booking of a document
	 * @param numeroDocument the number of the document
	 * @param numeroAbonne the number of the subscriber
	 * @throws IOException if an I/O error occurs
	 */
	private void retryBooking(int numeroDocument, int numeroAbonne) throws IOException {
		int timer = Media_library.getRemainingReservationTime(numeroDocument) + 1;

		try {
			Thread.sleep(timer * 1000L);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Writer.writeString(client, "Nouvelle tentative de reservation");

		try {
			Media_library.reservation(numeroDocument, numeroAbonne);
			Writer.writeString(client, "Merci d'avoir patiente, la reservation a reussie !");
		} catch (SQLException e2) {
			throw new RuntimeException(e2);
		} catch (DocumentAlreadyBorrowedException | DocumentAlreadyBookedException e2) {
			new LogError(e2.toString());
			Writer.writeString(client, """
					Pas de chance pour ce document, il n'est plus disponible.
					Tu as quand meme benéficie d'un concert céleste gratuit.
					Tu aurais du faire une offrande plus importante au grand chaman !""");
		}
	}

	@Override
	public String toString() {
		return "Inversion de texte";
	}
}