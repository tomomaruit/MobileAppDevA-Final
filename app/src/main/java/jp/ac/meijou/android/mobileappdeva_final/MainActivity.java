package jp.ac.meijou.android.mobileappdeva_final;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Build;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

import jp.ac.meijou.android.mobileappdeva_final.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    ListView myListview;

    private static final String PREFS_NAME = "NotificationPrefs";
    private static final String KEY_NOTIFICATION_SENT = "notification_sent";
    private static final int REQUEST_CODE = 100;
    private static final int REQUEST_CODE_SCHEDULE_EXACT_ALARM = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_main);

        // 通知を制御する
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "MobileAppDevA-Final",importance);
            channel.setDescription("アプリケーションの動作に応じた通知やリマインダーの送信に使用");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }

        // 通知テスト
        notifyTest();

        // 定時通知を設定
        setDailyNotification();


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

        /*
        binding.addButton.setOnClickListener(view -> {
            var intent = new Intent(this, Register.class);
            startActivity(intent);
        });
        */

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    private void setDailyNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());


        // 時間をランダムに設定
        /*
        int startHour = 9;
        int endHour = 18;
        int randomHour = startHour + (int)(Math.random() * ((endHour - startHour) + 1));
        int randomMinute = (int)(Math.random() * 60);
        calendar.set(Calendar.HOUR_OF_DAY, randomHour);
        calendar.set(Calendar.MINUTE, randomMinute);
        calendar.set(Calendar.SECOND, 0);
        */

        // デバッグ用
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 37);
        calendar.set(Calendar.SECOND, 0);

        if (alarmManager != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                    Intent permissionIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivityForResult(permissionIntent, REQUEST_CODE_SCHEDULE_EXACT_ALARM);
                    return;
                }
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } catch (SecurityException e) {
                Log.e("MainActivity", "SecurityException: " + e.getMessage());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCHEDULE_EXACT_ALARM) {
            // ユーザーが許可を与えたかどうかを確認
            setDailyNotification();
        }
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

        // 権限を確認
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE);
        }
    }

    // 登録ボタンを押したときは新規登録画面へ遷移
    public void Entry(View view) {
        Intent intent = new Intent(getApplication(),Register.class);
        // モード指定　空は新規
        intent.putExtra("KBN", "");

        // 行く
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 権限が付与された場合の処理
                notifyTest();
            } else {
                // 権限が拒否された場合の処理
            }
        }
    }

    // 単発通知テスト
    public void notifyTest() {
        // 権限を確認
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE);
            return; // 権限がない場合はリクエストして終了
        }

        // 通知を作成
        Intent fullScreenIntent = new Intent(this, MainActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setSmallIcon(android.R.drawable.ic_menu_info_details)
                .setContentTitle("お疲れ様です❗")
                .setContentText("今日もおつかれさま！仲間たちはどんどん覚えているようです！頑張れ！")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setFullScreenIntent(fullScreenPendingIntent, true); // フルスクリーンインテントを設定

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
}