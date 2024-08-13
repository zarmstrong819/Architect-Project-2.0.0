package edu.sdccd.cisc191;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ToDoListServer {
    private static final int port = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("ToDoListServer started.");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected to: " + clientSocket);

                Thread clientHandler = new Thread(new ClientHandler(clientSocket));
                clientHandler.start();
            }
        } catch (IOException e) {
            System.err.println("Couldn't Start Server: ");
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                 ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream())) {

                System.out.println("Client handler started:");

                while (true) {
                    Object receivedObject = inputStream.readObject();

                    if (receivedObject instanceof Task) {
                        Task receivedTask = (Task) receivedObject;
                        System.out.println("Received task: " + receivedTask.getDescription());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Couldn't Get Client Request ");
            }
        }
    }
}