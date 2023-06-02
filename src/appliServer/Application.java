package appliServer;

import server.*;
import sqlData.Media_library;

class Application {
	private final static int PORTBOOKING = 1000;
	private final static int PORTLOAN = 1001;
	private final static int PORTRETURN = 1002;

	public static void main(String[] args) throws Exception {
		Media_library.initMedia_library();

		new Thread(new Serveur(PORTBOOKING, ServiceBooking.class)).start();
		new Thread(new Serveur(PORTLOAN, ServiceLoaning.class)).start();
		new Thread(new Serveur(PORTRETURN, ServiceReturning.class)).start();
	}
}
