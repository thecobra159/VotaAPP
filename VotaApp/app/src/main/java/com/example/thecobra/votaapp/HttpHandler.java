package com.example.thecobra.votaapp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHandler {
    public static String makeServiceCall(String strUrl){ //strUrl = endereço do web service
        String response = null;
        try {
            URL url = new URL(strUrl);
            int codeResponse;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            codeResponse = conn.getResponseCode();

            //Verifica se o valor de response é menor que 400
            InputStream is  = codeResponse < HttpURLConnection.HTTP_BAD_REQUEST ? new BufferedInputStream(conn.getInputStream()) : new BufferedInputStream(conn.getErrorStream());

            response = convertStreamToString(is);
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
