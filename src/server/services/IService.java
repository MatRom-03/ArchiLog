package server.services;

public interface IService extends Runnable {
    void run();
    String toString();
}
