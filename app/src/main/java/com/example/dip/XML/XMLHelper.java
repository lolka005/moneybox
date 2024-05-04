package com.example.dip.XML;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class XMLHelper extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
        String answer = "";
        try {
            answer = downloadFile(urls[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return answer;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private String downloadFile(String url) throws InterruptedException {
        String myText = "";
        try {
            URL myUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
            conn.setConnectTimeout(60000);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String str;
            while ((str = in.readLine()) != null) {
                myText += str;
            }
            in.close();

        } catch (Exception e) {
            Log.d("MyTag", e.toString());
        }
        return myText;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}