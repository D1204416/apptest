package com.example.a0814test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class TodoAdapter extends ArrayAdapter<Todo> {

    public TodoAdapter(@NonNull Context context, ArrayList<Todo> Todos) {
        super(context, 0, Todos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Todo todo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_view, parent, false);
        }

        TextView tv_title = (TextView) convertView.findViewById(R.id.listTvTitle);
        TextView tv_content = (TextView) convertView.findViewById(R.id.listTvContent);
        TextView tv_num = (TextView) convertView.findViewById(R.id.listTvNumValue);

        tv_title.setText(todo.getTitle());
        String contentText = String.valueOf(todo.getContent());
        tv_content.setText(contentText);
        tv_num.setText(String.valueOf(todo.getNum()));

        ImageView imageView = (ImageView) convertView.findViewById(R.id.listImg);
        String imgName = todo.getImgName();

        if (imgName != null && !imgName.isEmpty()) {
            int imgResId = getContext().getResources().getIdentifier(imgName, "drawable", getContext().getPackageName());
            if (imgResId != 0) {
                imageView.setImageResource(imgResId);
            } else {
                imageView.setImageResource(R.drawable.apple); // 預設圖片
            }
        } else {
            imageView.setImageResource(R.drawable.apple); // 預設圖片
        }

        return convertView;
    }
}


