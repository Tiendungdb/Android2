package com.example.reader;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class Reader extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TextView btnPrev, btnNext, tvPageInfo, tvPageIndicator;
    private SeekBar seekBar;
    private List<Page> pageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reader);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initData();
        initViews();
        setupViewPager();
        setupListeners();
        updatePageIndicators(0);
    }

    private void initData() {
        pageList = new ArrayList<>();
        String content = "Alice was beginning to get very tired of sitting by her sister on the bank, and of having nothing to do: once or twice she had peeped into the book her sister was reading, but it had no pictures or conversations in it, \"and what is the use of a book,\" thought Alice, \"without pictures or conversation?\"";
        
        pageList.add(new Page("Classic Tales Collection", content, android.R.drawable.ic_menu_gallery));
        pageList.add(new Page("Chapter 2", "So she was considering in her own mind (as well as she could, for the hot day made her feel very sleepy and stupid), whether the pleasure of making a daisy-chain would be worth the trouble of getting up and picking the daisies...", android.R.drawable.ic_menu_gallery));
        pageList.add(new Page("Chapter 3", "Suddenly a White Rabbit with pink eyes ran close by her. There was nothing so VERY remarkable in that; nor did Alice think it so VERY much out of the way to hear the Rabbit say to itself, 'Oh dear! Oh dear! I shall be late!'", android.R.drawable.ic_menu_gallery));
        // Add more pages as needed to simulate 150 pages
        for (int i = 4; i <= 150; i++) {
            pageList.add(new Page("Chapter " + i, "Content for page " + i + "...", android.R.drawable.ic_menu_gallery));
        }
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        tvPageInfo = findViewById(R.id.tvPageInfo);
        tvPageIndicator = findViewById(R.id.tvPageIndicator);
        seekBar = findViewById(R.id.seekBar);

        seekBar.setMax(pageList.size() - 1);
    }

    private void setupViewPager() {
        PageAdapter adapter = new PageAdapter(pageList);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updatePageIndicators(position);
            }
        });
    }

    private void setupListeners() {
        btnPrev.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current > 0) viewPager.setCurrentItem(current - 1);
        });

        btnNext.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current < pageList.size() - 1) viewPager.setCurrentItem(current + 1);
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    viewPager.setCurrentItem(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updatePageIndicators(int position) {
        int pageNum = position + 1;
        int total = pageList.size();
        tvPageInfo.setText("Page " + pageNum + " of " + total);
        tvPageIndicator.setText(pageNum + " / " + total);
        seekBar.setProgress(position);
    }
}