package documents.exceptions;

import java.io.Serial;

public class DocumentAlreadyBorrowedException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 1L;
	private final int numeroDocument;

	/**
	 * Generate a new DocumentNotAvailableException
	 * @param numeroDocument the document number
	 */
	public DocumentAlreadyBorrowedException(int numeroDocument) {
		this.numeroDocument = numeroDocument;
	}

	@Override
	public String toString() {
		return "Le document " + this.numeroDocument + " est deja emprunte";
	}

}
