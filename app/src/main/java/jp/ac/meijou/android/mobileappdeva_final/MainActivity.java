package jp.ac.meijou.android.mobileappdeva_final;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import jp.ac.meijou.android.mobileappdeva_final.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PrefDataStore prefDataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefDataStore = PrefDataStore.getInstance(this);

        binding.addButton.setOnClickListener(view -> {
            var intent = new Intent(this, Register.class);
            startActivity(intent);
        });

        prefDataStore.getString("Q")
                        .ifPresent(Q -> binding.question1.setText(Q));
        prefDataStore.getString("A")
                        .ifPresent(A -> binding.answer1.setText(A));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    protected void onStart(){
        super.onStart();
        binding.correctText.setText("現在の連続正解数：" + Answer.correctAnswerNum + "問");
    }
}