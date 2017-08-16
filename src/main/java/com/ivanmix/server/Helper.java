package com.ivanmix.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Helper {
    private static String folderUrl = Server.class.getProtectionDomain().getCodeSource().getLocation().toString()
            .replace("file:/", "")
            .replace("http-static-server.jar","");

    private static String errorPage;

    public static String getBody(String name) throws IOException {
        Path pathToHtmlFile = Paths.get(getFolderUrl() + name);
        return new String(Files.readAllBytes(pathToHtmlFile));
    }

    public static String getRequestUrl(InputStream inputStream) throws IOException {
        String url = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while (true) {
            String s = br.readLine();
            if (s == null || s.trim().length() == 0) {
                break;
            }
            if (s.startsWith("GET")) {
                String requestUrl = s.replace("GET ", "").split(" HTTP")[0];
                if (requestUrl.equals("/")) {
                    url = "index.html";
                } else if (requestUrl.endsWith(".html")) {
                    url = requestUrl;
                }
                break;
            }
        }
        return url;
    }

    public static String getFolderUrl() {
        return folderUrl;
    }

    public static String getErrorPage() {
        if (errorPage == null) {
            try {
                errorPage = getBody("404.html");
            } catch (IOException e) {
                errorPage = "<html><body><h1>404 Not Found</h1></body></html>";
            }
        }
        return errorPage;
    }
}

