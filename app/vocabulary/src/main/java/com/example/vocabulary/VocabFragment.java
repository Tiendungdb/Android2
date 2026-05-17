package com.example.vocabulary;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class VocabFragment extends Fragment {


   Vocab vocab;


   public VocabFragment() {
   }

   public VocabFragment(Vocab vocab){
       this.vocab = vocab;
       Bundle args = new Bundle();
       args.putSerializable("vocab", vocab);
       this.setArguments(args);
   }

   @Override
   public void onCreate(@Nullable Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       if (getArguments() != null) {
           this.vocab = (Vocab) getArguments().getSerializable("vocab");
       }
   }

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.vocab_layout, container, false);
       if (vocab != null) {
           TextView defTextView = rootView.findViewById(R.id.defTextView);
           defTextView.setText(vocab.def);

           defTextView.setOnClickListener(v -> {
               new AlertDialog.Builder(getContext())
                       .setMessage("Bạn đang xem nghĩa của từ " + vocab.term)
                       .setPositiveButton("OK", null)
                       .show();
           });

           TextView ipaTextView = rootView.findViewById(R.id.ipaTextView);
           ipaTextView.setText(vocab.ipa);
       }
       return rootView;
   }
}
