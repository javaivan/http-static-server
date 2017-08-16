package com.ivanmix.server;

import java.io.IOException;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        int post = 8080;
        if (args.length > 1 && args[0].equals("-p")) {
            try {
                post = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
            }
        }

        ServerSocket ss = new ServerSocket(post);
        while (true) {
            Socket s = ss.accept();
            new Thread(new SocketProcessor(s)).start();
        }
    }
}
