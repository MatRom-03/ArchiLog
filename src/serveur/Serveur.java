package serveur;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;

public class Serveur implements Runnable {
	private final ServerSocket listen_socket;

	private final Class<? extends IService> SERVICE;

	// Cree un serveur TCP - objet de la classe ServerSocket
	public Serveur(int port, Class<? extends IService> service) throws IOException, NoSuchMethodException {
		listen_socket = new ServerSocket(port);
		this.SERVICE = service;
		System.out.println(service.getConstructor(Socket.class));
	}

	// Le serveur ecoute et accepte les connexions.
	// pour chaque connexion, il cree un ServiceInversion,
	// qui va la traiter, et le lance
	public void run() {
		try {
			System.err.println("Lancement du serveur au port " + this.listen_socket.getLocalPort());
			//System.out.println(this.SERVICE.getConstructor(Socket.class).newInstance(listen_socket));
			while (true)
				new Thread(this.SERVICE.getConstructor(Socket.class).newInstance(listen_socket.accept())).start(); //new Thread(new ServiceCours(listen_socket.accept())).start();
		} catch (IOException e) {
			try {
				this.listen_socket.close();
			} catch (IOException e1) {
			}
			System.err.println("Arret du serveur au port " + this.listen_socket.getLocalPort());
		} catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	// restituer les ressources --> finalize
	protected void finalize() throws Throwable {
		try {
			this.listen_socket.close();
		} catch (IOException e1) {
		}
	}
}
