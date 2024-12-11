package jp.ac.meijou.android.mobileappdeva_final;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper {
    private SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        MyOpenHelper helper = new MyOpenHelper(context);
        this.database = helper.getReadableDatabase();
    }

    /**
     * ランダムなデータを取得する
     * @return ランダムな質問と回答の配列 [質問, 回答]
     */
    public String[] getRandomData() {
        Cursor cursor = database.rawQuery("SELECT que, ans FROM myPasstb ORDER BY RANDOM() LIMIT 1", null);
        String[] result = new String[2];

        if (cursor.moveToFirst()) {
            result[0] = cursor.getString(0); // 質問
            result[1] = cursor.getString(1); // 回答
        } else {
            result[0] = "データが存在しません";
            result[1] = "";
        }

        cursor.close();
        return result;
    }
}
