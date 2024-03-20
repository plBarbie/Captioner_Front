//package com.example.captioner;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.ListView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.captioner.model.PlayItem;
//
//import java.util.ArrayList;
//
//public class BookedActivity extends AppCompatActivity {
//
//    private ListView listView;
//    private BookedListAdapter adapter;
//    private ArrayList<PlayItem> playItems = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_booked);
//
//        listView = findViewById(R.id.listView);
//        adapter = new BookedListAdapter(this, playItems);
//        listView.setAdapter(adapter);
//
//        loadPlayItemsFromDatabase();
//
//        Button returnButton = findViewById(R.id.returnButton);
//        returnButton.setOnClickListener(v -> {
//            startActivity(new Intent(BookedActivity.this, HomeActivity.class));
//            finish();
//        });
//    }
//
//    private void loadPlayItemsFromDatabase() {
//        // 指定数据库文件的路径
//        String dbPath = "/data/data/" + getPackageName() + "/databases/your_database_name.db";
//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//
//        try {
//            // 打开数据库
//            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
//
//            // 执行查询
//            cursor = db.rawQuery("SELECT play_title, play_start_time FROM play", null);
//
//            while (cursor.moveToNext()) {
//                String title = cursor.getString(cursor.getColumnIndexOrThrow("play_title"));
//                String startTime = cursor.getString(cursor.getColumnIndexOrThrow("play_start_time"));
//                playItems.add(new PlayItem(title, startTime));
//            }
//        } catch (Exception e) {
//            // 处理错误情况
//            e.printStackTrace();
//        } finally {
//            // 关闭资源
//            if (cursor != null) {
//                cursor.close();
//            }
//            if (db != null) {
//                db.close();
//            }
//        }
//
//        adapter.notifyDataSetChanged();
//    }
//}
//
