package com.example.a0814test;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ArrayList<Todo> todoArrayList = new ArrayList<Todo>();
    private ActivityResultLauncher<Intent> intentActivityResultLanucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intentActivityResultLanucher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if(o.getData() != null && o.getResultCode() == Activity.RESULT_OK){
                            String action = o.getData().getStringExtra("ACTION");

                            if(action.equals("new")){
                                // 處理新增待辦事項
                                String newTitle = o.getData().getStringExtra("TITLE");
                                String newContent = o.getData().getStringExtra("CONTENT");
                                int newNum = o.getData().getIntExtra("NUM", 0); // 獲取數量，預設為 0
                                String imgName = o.getData().getStringExtra("IMG_NAME"); // 獲取圖片名稱

                                Todo newData = new Todo(newTitle, newContent, newNum, imgName);
                                todoArrayList.add(newData);
                            } else if (action.equals("edit")) {
                                // 處理編輯待辦事項
                                String index = o.getData().getStringExtra("INDEX");
                                String newTitle = o.getData().getStringExtra("TITLE");
                                String newContent = o.getData().getStringExtra("CONTENT");
                                int newNum = o.getData().getIntExtra("NUM", 0); // 獲取數量，預設為 0
                                String imgName = o.getData().getStringExtra("IMG_NAME"); // 獲取圖片名稱

                                Todo existingTodo = todoArrayList.get(Integer.parseInt(index));
                                existingTodo.setTitle(newTitle);
                                existingTodo.setContent(newContent);
                                existingTodo.setNum(newNum); // 更新數量
                                existingTodo.setImgName(imgName); // 更新圖片名稱
                            } else{
                                // 處理刪除待辦事項
                                int removeIndex = o.getData().getIntExtra("INDEX", 0);
                                todoArrayList.remove(removeIndex);
                            }
                        }
                        refreshPartListView(MainActivity.this);
                    }
                }
        );

        refreshPartListView(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        refreshPartListView(this);
    }

    public void newButtonClick(View view){
        Intent intent = new Intent(this, TodoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("ACTION", "new");
        intent.putExtras(bundle);
        intentActivityResultLanucher.launch(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Todo item = todoArrayList.get(i);
        String title = item.getTitle();
        String content = item.getContent();
        int num = item.getNum(); // 獲取當前項目的數量

        Intent intent = new Intent(this, TodoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("ACTION", "edit");
        bundle.putString("TITLE", title);
        bundle.putString("CONTENT", content);
        bundle.putInt("NUM", num); // 將數量傳遞到編輯活動
        bundle.putInt("INDEX", i);

        intent.putExtras(bundle);
        intentActivityResultLanucher.launch(intent);
    }

    public void refreshPartListView(Context context) {
        ListView partListView = (ListView) findViewById(R.id.todoListView);

        // 判斷 ArrayList 是否為空
        if (todoArrayList.isEmpty()) {
            // 當 ArrayList 為空時，顯示提示訊息
            Toast.makeText(context, "目前沒有任何資料", Toast.LENGTH_SHORT).show();
            ArrayList<String> empty = new ArrayList<String>();
            empty.add("目前無任何料件！");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, empty);
            partListView.setAdapter(adapter);
        } else {
            TodoAdapter adapter = new TodoAdapter(this, todoArrayList);
            partListView.setAdapter(adapter);
            partListView.setOnItemClickListener(this);

            // 當 ArrayList 不為空時，設定 ListView 的長按事件來進行刪除
            partListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    String titleText = "確定刪除？";
                    String contentText = "確定刪除這個項目？";
                    dialog.setTitle(titleText);
                    dialog.setMessage(contentText);
                    dialog.setCancelable(true);

                    // 設定 "確定" 按鈕的事件
                    dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            todoArrayList.remove(itemIndex);
                            refreshPartListView(context);  // 刷新 ListView
                        }
                    });

                    // 設定 "取消" 按鈕的事件
                    dialog.setNeutralButton("取消", null);
                    dialog.show();
                    return true;
                }
            });
        }
    }
}
