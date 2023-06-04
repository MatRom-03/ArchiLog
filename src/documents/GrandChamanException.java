package documents;

import java.io.Serial;

public class GrandChamanException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 1L;
	private final int numeroDocument;

	/**
	 * Generate a new DocumentNotAvailableException
	 * @param numeroDocument the document number
	 */
	public GrandChamanException(int numeroDocument) {
		this.numeroDocument = numeroDocument;
	}

	@Override
	public String toString() {
		return "Le document " + this.numeroDocument + " est déjà reservé.\n" +
				"Merci de patienter, la réservation va se relancer dans moins d'une minute.\n" +
				"Pendant ce temps, profitez de la musique du grand chaman !";
	}

}
