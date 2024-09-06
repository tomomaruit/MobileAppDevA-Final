package jp.ac.meijou.android.mobileappdeva_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import jp.ac.meijou.android.mobileappdeva_final.databinding.ActivityAnswerBinding;

public class Answer extends AppCompatActivity {

    private ActivityAnswerBinding binding;
    public static int correctAnswerNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnswerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.answerText.setVisibility(View.INVISIBLE);
        binding.returnButton.setVisibility(View.INVISIBLE);

        binding.answerButton.setOnClickListener(view -> {
            binding.answerText.setVisibility(View.VISIBLE);
            binding.returnButton.setVisibility(View.VISIBLE);
        });

        // 戻るボタンを押すと
        binding.returnButton.setOnClickListener(view -> {
            correctAnswerNum += 1;      // 正解なら+1
            var intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}