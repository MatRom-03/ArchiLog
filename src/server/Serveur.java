package server;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;

public class Serveur implements Runnable {
	private final ServerSocket listen_socket;

	private final Class<? extends IService> SERVICE;

	public Serveur(int port, Class<? extends IService> service) throws IOException {
		listen_socket = new ServerSocket(port);
		this.SERVICE = service;
	}

	public void run() {
		try {
			System.err.println("Lancement du serveur au port " + this.listen_socket.getLocalPort());
			while (true)
				new Thread(this.SERVICE.getConstructor(Socket.class).newInstance(listen_socket.accept())).start();
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
}
