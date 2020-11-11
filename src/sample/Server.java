package sample;

import netUtil.NetworkUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(9990);

            while (true) {
                Socket socket = serverSocket.accept();
                NetworkUtil networkUtil = new NetworkUtil(socket);
                new ReadServer(networkUtil);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}