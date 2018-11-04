package com.example.thecobra.votaapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.thecobra.votaapp.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
//    //Retorna um candidato para usuário
//    public Candidate getInfo(String end){
//        String json;
//        Candidate candidate;
//        json = HttpHandler.makeServiceCall(end);
//        Log.i("Result", json);
//        candidate = parseJson(json);
//
//        return candidate;
//    }
//
//    //Transformar json em objeto candidato
//    private Candidate parseJson(String json) {
//        try {
//            Candidate candidate = new Candidate();
//
//            JSONObject jsonObject = new JSONObject(json);
//            JSONArray jsonArray = jsonObject.getJSONArray("results");
//
//            JSONObject objectArray = jsonArray.getJSONObject(0);
//
//            JSONObject object = objectArray.getJSONObject("user");
//
//            //Atribui os objetos
//            candidate.setName(object.getString("name"));
//            candidate.setParty(object.getString("party"));
//
//            //Imagem é um objeto
//            JSONObject photo = object.getJSONObject("picture");
//            candidate.setPhoto(downloadImage(photo.getString("large")));
//
//            return candidate;
//        } catch (JSONException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private Bitmap downloadImage(String large) {
//        try {
//            URL url = new URL(large);
//            InputStream is = url.openStream();
//            Bitmap photo = BitmapFactory.decodeStream(is);
//
//            is.close();
//
//            return photo;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
