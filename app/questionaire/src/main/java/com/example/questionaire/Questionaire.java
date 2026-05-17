package com.example.questionaire;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Questionaire extends AppCompatActivity {

    // Khai báo View
    private TextView questionTextView;
    private Button optionButton1, optionButton2, optionButton3, optionButton4;
    
    // Khai báo list để chứa các câu hỏi tải ra từ db
    private List<Question> questions = new ArrayList<>();

    private int currentIndex = 0;
    private Button backButton, nextButton;

    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_questionaire);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Liên kết với View trong Layout
        questionTextView = findViewById(R.id.questionTextView);
        optionButton1 = findViewById(R.id.optionButton1);
        optionButton2 = findViewById(R.id.optionButton2);
        optionButton3 = findViewById(R.id.optionButton3);
        optionButton4 = findViewById(R.id.optionButton4);

        // 2 nút Next, Back
        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);

        nextButton.setOnClickListener(v -> {
            if (currentIndex < questions.size() - 1) {
                currentIndex = currentIndex + 1;
                showQuestion(questions.get(currentIndex));
            }
        });

        backButton.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex = currentIndex - 1;
                showQuestion(questions.get(currentIndex));
            }
        });

        // Các nút chọn phương án
        View.OnClickListener optionListener = view -> {
            Button b = (Button) view;
            String answer = b.getText().toString();
            if (answer.equals(questions.get(currentIndex).answer)) {
                score++;
            }
            if (currentIndex < questions.size() - 1) {
                currentIndex++;
                showQuestion(questions.get(currentIndex));
            } else {
                questionTextView.setText("Quiz Finished! Your score: " + score);
                optionButton1.setVisibility(View.GONE);
                optionButton2.setVisibility(View.GONE);
                optionButton3.setVisibility(View.GONE);
                optionButton4.setVisibility(View.GONE);
                nextButton.setVisibility(View.GONE);
                backButton.setVisibility(View.GONE);
            }
        };

        // Áp sự kiện cho các options
        optionButton1.setOnClickListener(optionListener);
        optionButton2.setOnClickListener(optionListener);
        optionButton3.setOnClickListener(optionListener);
        optionButton4.setOnClickListener(optionListener);

        // Đọc toàn bộ câu và đưa vào questions và hiển thị câu số đầu tiên
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            questions.addAll(db.questionDao().getAll());
            
            if (!questions.isEmpty()) {
                runOnUiThread(() -> showQuestion(questions.get(0)));
            }
        }).start();
    }

    private void showQuestion(Question question) {
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> options = new Gson().fromJson(question.options, type);

        optionButton1.setVisibility(View.VISIBLE);
        optionButton2.setVisibility(View.VISIBLE);
        optionButton3.setVisibility(View.VISIBLE);
        optionButton4.setVisibility(View.VISIBLE);

        questionTextView.setText(question.content);
        optionButton1.setText(options.get(0));
        optionButton2.setText(options.get(1));
        optionButton3.setText(options.get(2));
        optionButton4.setText(options.get(3));

        // Trường hợp đang ở câu đầu ẩn nút back
        if (currentIndex > 0) {
            backButton.setVisibility(View.VISIBLE);
        } else {
            backButton.setVisibility(View.GONE);
        }

        // Trường hợp đang ở câu cuối ẩn nút next
        if (currentIndex < questions.size() - 1) {
            nextButton.setVisibility(View.VISIBLE);
        } else {
            nextButton.setVisibility(View.GONE);
        }
    }
}
