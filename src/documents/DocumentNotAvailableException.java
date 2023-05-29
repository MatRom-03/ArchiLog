package documents;

import java.io.Serial;

public class DocumentNotAvailableException extends Exception {
	@Serial
	private static final long serialVersionUID = 1L;
	private final int numeroDocument;
	private final int duration;
	private final boolean reserved;

	/**
	 * Generate a new DocumentNotAvailableException
	 * @param duration the duration of the reservation
	 * @param reserved true if the document is reserved
	 */
	public DocumentNotAvailableException(int numeroDocument, int duration, boolean reserved) {
		this.duration = duration;
		this.reserved = reserved;
		this.numeroDocument = numeroDocument;
	}

	@Override
	public String toString() {
		if (this.reserved)
			return "Le document " + this.numeroDocument + " est réservé prendant encore " + this.duration + " minutes";
		return "Ce document est déjà emprunté";
	}

}
