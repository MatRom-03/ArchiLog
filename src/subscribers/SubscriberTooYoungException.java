package subscribers;

import java.io.Serial;

public class SubscriberTooYoungException extends Exception {
	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Generate a new SubscriberTooYoungException
	 */
	public SubscriberTooYoungException() {}

	@Override
	public String toString() {
		return "vous n’avez pas l’âge pour emprunter ce DVD";
	}

}
