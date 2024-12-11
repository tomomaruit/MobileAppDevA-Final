package jp.ac.meijou.android.mobileappdeva_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import jp.ac.meijou.android.mobileappdeva_final.databinding.ActivityAnswerBinding;
import jp.ac.meijou.android.mobileappdeva_final.DatabaseHelper;

public class Answer extends AppCompatActivity {
    private ActivityAnswerBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnswerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        // ランダムデータを取得
        TextView questionText = findViewById(R.id.questionText);
        TextView answerText = findViewById(R.id.answerText);
        String[] randomData = dbHelper.getRandomData();
        questionText.setText(randomData[0]);
        answerText.setText(randomData[1]);

        binding.answerText.setVisibility(View.INVISIBLE);
        binding.returnButton.setVisibility(View.INVISIBLE);

        binding.answerButton.setOnClickListener(view -> {
            binding.answerText.setVisibility(View.VISIBLE);
            binding.returnButton.setVisibility(View.VISIBLE);
        });

        binding.returnButton.setOnClickListener(view -> {
            var intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}
