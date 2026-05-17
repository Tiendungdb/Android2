package com.example.note;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class Note extends AppCompatActivity {

    CalendarView calendarView;
    EditText noteEditText;
    Button saveButton;
    String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        calendarView = findViewById(R.id.calendarView);
        noteEditText = findViewById(R.id.noteEditText);
        saveButton = findViewById(R.id.saveButton);

        // Thiet lap lai theo gia tri da luu
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        int savedYear = pref.getInt("year", 0);
        if (savedYear != 0) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, savedYear);
            cal.set(Calendar.MONTH, pref.getInt("month", 0));
            cal.set(Calendar.DAY_OF_MONTH, pref.getInt("dayOfMonth", 0));
            calendarView.setDate(cal.getTimeInMillis());

            // Thiet lap lai fileName theo gia tri da luu
            fileName = String.format("%02d_%02d_%04d", pref.getInt("dayOfMonth", 0), pref.getInt("month", 0) + 1, savedYear);
        }

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Luu ngay da chon
            SharedPreferences p = getPreferences(MODE_PRIVATE);
            p.edit().putInt("year", year).apply();
            p.edit().putInt("month", month).apply();
            p.edit().putInt("dayOfMonth", dayOfMonth).apply();

            fileName = String.format("%02d_%02d_%04d", dayOfMonth, month + 1, year);
            noteEditText.setText("");
            try {
                FileInputStream fis = openFileInput(fileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                fis.close();
                noteEditText.setText(sb);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        saveButton.setOnClickListener(view -> {
            if (fileName == null) return;
            // Luu note
            String noteContent = noteEditText.getText().toString();
            try {
                // Ghi nội dung vào file
                FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
                fos.write(noteContent.getBytes());
                fos.close();
                Toast.makeText(this, "Đã lưu ghi chú", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi lưu ghi chú", Toast.LENGTH_LONG).show();
            }

        });
    }

}
