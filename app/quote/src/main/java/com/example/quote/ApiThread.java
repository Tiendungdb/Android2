package com.example.quote;

import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiThread extends Thread {

    String quote;
    QuoteActivity activity;

    public ApiThread(String quote, QuoteActivity activity) {
        this.quote = quote;
        this.activity = activity;
    }

    @Override
    public void run() {
        try {

            String apiUrl =
                    "https://dummyjson.com/quotes/random";

            URL url = new URL(apiUrl);

            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream)
            );

            StringBuilder response = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null){
                response.append(line);
            }

            reader.close();
            Log.d("API Response", response.toString()); // Check logcat thấy cấu trúc json có quote và author


            JSONObject jsonObject = new JSONObject(response.toString());
            String quote = jsonObject.getString("quote"); // Chỉ lấy quote vì tác giả không biết có cần không :))

            String result =
                    quote;

            TextView textViewResult = activity.findViewById(R.id.textViewResult);
            activity.runOnUiThread(() -> {
                textViewResult.setText(result);
            });

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}