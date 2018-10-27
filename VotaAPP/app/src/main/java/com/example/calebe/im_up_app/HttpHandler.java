package com.example.calebe.im_up_app;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHandler {
    public static String loginPost(String strUrl) {
        String response = null;
        OutputStream os;
        try {
            URL url = new URL(strUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            os = new BufferedOutputStream(urlConnection.getOutputStream());

            JSONObject loginJson = new JSONObject();

            loginJson.put("username", Controller.getInstance().getUserAuth());
            loginJson.put("password", Controller.getInstance().getUserPass());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(loginJson.toString());
            writer.flush();
            writer.close();
            os.close();

            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            response = convertStreamToString(new BufferedInputStream(urlConnection.getInputStream()));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String getCandidate(String strUrl) {
        String response = null;
        try {
            URL url = new URL(strUrl);
            int codeResponse;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            codeResponse = conn.getResponseCode();

            InputStream is = codeResponse < HttpURLConnection.HTTP_BAD_REQUEST ? new BufferedInputStream(conn.getInputStream()) : new BufferedInputStream(conn.getErrorStream());

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
            while ((line = reader.readLine()) != null) {
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
