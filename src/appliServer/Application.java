package appliServer;

import server.*;
import sqlData.Media_library;

class Application {
	private final static int PORTBOOKING = 1000;
	private final static int PORTLOAN = 1001;
	private final static int PORTRETURN = 1002;

	public static void main(String[] args) throws Exception {
		Media_library media_library = new Media_library();
		ServiceBooking.setDocument(media_library);
		ServiceLoaning.setDocument(media_library);
		ServiceReturning.setDocument(media_library);

		new Thread(new Serveur(PORTBOOKING, ServiceBooking.class)).start();
		System.out.println("Serveur lance sur le port " + PORTBOOKING);
		new Thread(new Serveur(PORTLOAN, ServiceLoaning.class)).start();
		System.out.println("Serveur lance sur le port " + PORTLOAN);
		new Thread(new Serveur(PORTRETURN, ServiceReturning.class)).start();
		System.out.println("Serveur lance sur le port " + PORTRETURN);
	}
}
