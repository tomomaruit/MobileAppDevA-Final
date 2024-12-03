package jp.ac.meijou.android.mobileappdeva_final;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import jp.ac.meijou.android.mobileappdeva_final.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    ListView myListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_main);

        // リストビュー
        myListview = findViewById(R.id.Listview);
        // db
        MyOpenHelper myOpenHelper = new MyOpenHelper(this);
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();

        // select
        Cursor c = db.rawQuery("select * from myPasstb", null);


        //adapterの準備
        //表示するカラム名
        String[] from = {"_id","que"};

        // バインドするViewリソース
        int[] to = {android.R.id.text1,android.R.id.text2};

        //adapterの生成
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,c,from,to,0);

        //バインドして表示
        myListview.setAdapter(adapter);

        //リストビューをタップした時の各行のデータを取得
        myListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                //各要素を取得
                //_id
                String s1 = ((TextView)view.findViewById(android.R.id.text1)).getText().toString();
                //que
                // String s2 = ((TextView)view.findViewById(android.R.id.text2)).getText().toString();

                //参照更新へ
                Intent intent = new Intent(getApplication(), Register.class);

                //モード指定 _idを渡す
                intent.putExtra("KBN",s1);

                startActivity(intent);
            }
        });

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

//    protected void onStart(){
//        super.onStart();
//        binding.correctText.setText("現在の連続正解数：" + Answer.correctAnswerNum + "問");
//    }

    //リターン時
    @Override
    protected void onRestart() {
        super.onRestart();
        reload();
    }

    private void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0,0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
    }

    // 登録ボタンを押したときは新規登録画面へ遷移
    public void Entry(View view) {
        Intent intent = new Intent(getApplication(),Register.class);
        // モード指定　空は新規
        intent.putExtra("KBN", "");

        // 行く
        startActivity(intent);
    }
}