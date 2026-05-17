package com.example.vocabulary;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Vocabulary extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vocabulary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Chuyen che do ngang doc man hinh khong bi loi hien thi
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Vocab> listVocab = new ArrayList<>();
        listVocab.add(new Vocab("Cat", "Mèo", "[kæt]"));
        listVocab.add(new Vocab("Tiger", "Hổ", "[ˈtaɪɡər]"));
        listVocab.add(new Vocab("Fish", "Cá", "[fɪʃ]"));
        listVocab.add(new Vocab("Bird", "Chim", "[bɜːrd]"));
        listVocab.add(new Vocab("Elephant", "Voi", "[ˈɛlɪfənt]"));
        listVocab.add(new Vocab("Snake", "Rắn", "[sneɪk]"));
        listVocab.add(new Vocab("Monkey", "Khỉ", "[ˈmʌŋki]"));
        listVocab.add(new Vocab("Bear", "Gấu", "[bɛr]"));
        listVocab.add(new Vocab("Cow", "Bò", "[kaʊ]"));
        listVocab.add(new Vocab("Horse", "Ngựa", "[hɔːrs]"));
        listVocab.add(new Vocab("Duck", "Vịt", "[dʌk]"));
        listVocab.add(new Vocab("Chicken", "Gà", "[ˈtʃɪkɪn]"));
        listVocab.add(new Vocab("Sheep", "Cừu", "[ʃiːp]"));
        listVocab.add(new Vocab("Goat", "Dê", "[ɡoʊt]"));
        listVocab.add(new Vocab("Wolf", "Sói", "[wʊlf]"));
        listVocab.add(new Vocab("Fox", "Cáo", "[fɑːks]"));
        listVocab.add(new Vocab("Rabbit", "Thỏ", "[ˈræbɪt]"));
        listVocab.add(new Vocab("Frog", "Ếch", "[frɔːɡ]"));
        listVocab.add(new Vocab("Ant", "Kiến", "[ænt]"));
        listVocab.add(new Vocab("Bee", "Ong", "[biː]"));

        // Khởi tạo Adapter và đặt Adapter cho RecyclerView
        MyAdapter adapter = new MyAdapter(this, listVocab);
        recyclerView.setAdapter(adapter);
    }
}
