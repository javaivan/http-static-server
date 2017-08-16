package com.ivanmix.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.NoSuchFileException;

class SocketProcessor implements Runnable {
    private Socket s;
    private InputStream is;
    private OutputStream os;

    public SocketProcessor(Socket s) throws IOException {
        this.s = s;
        this.is = s.getInputStream();
        this.os = s.getOutputStream();
    }

    public void run() {
        try {
            writeResponse();
        } catch (Throwable t) {
            System.out.println(t);
        } finally {
            try {
                s.close();
            } catch (Throwable t) {
                System.out.println(t);
            }
        }
    }

    private void writeResponse() throws IOException {
        String requestUrl = Helper.getRequestUrl(is);
        if (!requestUrl.isEmpty()) {
            try {
                successfulWriteResponse(Helper.getBody(requestUrl));
            } catch (NoSuchFileException e) {
                System.out.println(e);
                unsuccessfulWriteResponse();
            }
        } else {
            unsuccessfulWriteResponse();
        }
    }

    private void successfulWriteResponse(String s) throws IOException {
        writeResponse(s, "HTTP/1.1 200 OK\r\n");
    }

    private void unsuccessfulWriteResponse() throws IOException {
        writeResponse(Helper.getErrorPage(), "HTTP/1.1 404 Not Found\r\n");
    }

    private void writeResponse(String s, String response) throws IOException {
        response = response + "Content-Type: text/html\r\n" +
                "Content-Length: " + s.length() + "\r\n" +
                "Connection: close\r\n\r\n";
        String result = response + s;
        os.write(result.getBytes());
        os.flush();
    }
}
