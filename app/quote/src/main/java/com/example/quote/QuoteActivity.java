package com.example.quote;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuoteActivity extends AppCompatActivity {
    TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        textViewResult = findViewById(R.id.textViewResult);

            String quote = textViewResult.getText().toString();

            ApiThread thread = new ApiThread(quote, this);
            thread.start();
        }
    }

