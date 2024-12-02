package jp.ac.meijou.android.mobileappdeva_final;

import static androidx.datastore.core.StorageConnectionKt.readData;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultRegistry;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import jp.ac.meijou.android.mobileappdeva_final.databinding.ActivityRegisterBinding;

public class Register extends AppCompatActivity {

    private ActivityRegisterBinding binding;
//    private PrefDataStore prefDataStore;
    private MyOpenHelper helper;
    String kbn = "";
    String toastMessage = "登録しました．戻るを押してください．";
    String toastMessage2 = "登録するものがありません．";
//    String toastMessage3 = "更新しました．戻るを押してください．";
//    String toastMessage4 = "更新するものがありません．";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // DB作成
        helper = new MyOpenHelper(getApplicationContext());

        // データを受け取る
        Intent intent = getIntent();
        String KBN = intent.getStringExtra("KBN");

        Button button = findViewById(R.id.button2);
        View view = findViewById(R.id.Layout);

        if (KBN.length() != 0) {
            //参照
//            kbn = KBN;
//
//            // ボタンテキスト変更
//            view.setBackgroundColor(Color.YELLOW);
//
//            //既存データ参照
//            readData(KBN);
        } else {
            // 新規登録
            kbn = "登録";

            // ボタンテキスト
            button.setText("登録");
            // 背景色変更
            view.setBackgroundColor(Color.CYAN);
        }

//        binding.buttonRegister.setOnClickListener(view -> {
//            var question = binding.edittextQuestion.getText().toString();
//            prefDataStore.setString("Q", question);
//            binding.edittextQuestion.getText().clear();
//            var answer = binding.edittextAnswer.getText().toString();
//            prefDataStore.setString("A", answer);
//            binding.edittextAnswer.getText().clear();
//        });
//
//        binding.buttonReturn.setOnClickListener(view -> {
//            var intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        });

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    public void onClose(View view) {
        finish(); // 画面を閉じる
    }

//    private void readData(String read) {
//        SQLiteDatabase db = helper.getReadableDatabase();
//
//        EditText text1 = findViewById(R.id.editTextText);
//        EditText text2 = findViewById(R.id.editTextText2);
//
//        Cursor cursor = db.query(
//                "myPasstb",
//                new String[]{"que","ans"},
//                "que = ?",
//                new String[]{read},
//                null,null,null
//        );
//        cursor.moveToFirst();
//
//        for (int i = 0; i < cursor.getCount(); i++) {
//            text1.setText(cursor.getString(0));
//            text2.setText(cursor.getString(1));
//        }
//
//        cursor.close();
//    }

    /**
     * データを保存する
     * @param view
     */
    public void saveData(View view) {
        SQLiteDatabase db = helper.getWritableDatabase();

        EditText txt1 = findViewById(R.id.editTextText);
        EditText txt2 = findViewById(R.id.editTextText2);

        String que = txt1.getText().toString();
        String ans = txt2.getText().toString();

        ContentValues values = new ContentValues();
        values.put("que", que);
        values.put("ans", ans);

        //ボタンが登録の場合
        if (kbn == "登録") {
            if (que.length() != 0) {
                // 新規登録
                db.insert("myPasstb", null, values);
                // トースト表示
                toastMake(toastMessage, 0, +350);
            } else {
                // トースト表示
                toastMake(toastMessage2, 0, +350);
            }
            // ボタンが更新の場合
        }
//        else {
//            if (que.length() != 0) {
//                // 更新
//                UPData(kbn);
//                //トースト表示
//                toastMake(toastMessage3, 0, +350);
//            } else {
//                //トースト表示
//                toastMake(toastMessage4, 0, +350);
//            }
//        }
    }

    /**
     * データ更新
     * @param read
     */
//    private void UPData(String read) {
//        SQLiteDatabase db = helper.getReadableDatabase();
//
//        EditText txt1 = findViewById(R.id.editTextText);
//        EditText txt2 = findViewById(R.id.editTextText2);
//
//        String que = txt1.getText().toString();
//        String ans = txt2.getText().toString();
//
//        ContentValues upvalue = new ContentValues();
//        upvalue.put("que",que);
//        upvalue.put("ans",ans);
//
//        db.update("myPasstb",upvalue,"_id=?",new String[]{read});
//
//
//    }

    private void toastMake(String message, int x, int y) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        // 位置調整
        toast.setGravity(Gravity.CENTER, x, y);
        toast.show();
    }
}

