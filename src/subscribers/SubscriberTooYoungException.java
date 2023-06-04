package subscribers;

import java.io.Serial;

public class SubscriberTooYoungException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Generate a new SubscriberTooYoungException
	 */
	public SubscriberTooYoungException() {}

	@Override
	public String toString() {
		return "Vous n’avez pas l’âge pour emprunter ce DVD";
	}

}
