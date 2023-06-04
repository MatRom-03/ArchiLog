package documents.exceptions;

import java.io.Serial;

public class DocumentAlreadyBookedException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 1L;
	private final int numeroDocument;
	private final int duration;

	/**
	 * Generate a new DocumentNotAvailableException
	 * @param numeroDocument the document number
	 * @param duration the duration of the reservation
	 */
	public DocumentAlreadyBookedException(int numeroDocument, int duration) {
		this.duration = duration;
		this.numeroDocument = numeroDocument;
	}

	@Override
	public String toString() {
		return "Le document " + this.numeroDocument + " est reserve pendant encore " + this.duration + " secondes";
	}

}
