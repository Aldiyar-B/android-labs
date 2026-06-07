package com.example.lab13;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FileAdapter extends ArrayAdapter<FileItem> {

    private final LayoutInflater inflater;

    public FileAdapter(Context context, List<FileItem> items) {
        super(context, 0, items);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_file, parent, false);
            holder = new ViewHolder();
            holder.icon = convertView.findViewById(R.id.itemIcon);
            holder.name = convertView.findViewById(R.id.itemName);
            holder.info = convertView.findViewById(R.id.itemInfo);
            holder.action = convertView.findViewById(R.id.itemAction);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FileItem item = getItem(position);
        if (item != null) {
            holder.icon.setImageResource(item.getIconResId());
            holder.name.setText(item.getName());
            holder.info.setText(item.getInfo(getContext()));
            holder.action.setText(item.isDirectory() ? "Перейти" : "Открыть");
        }
        return convertView;
    }

    private static class ViewHolder {
        ImageView icon;
        TextView name;
        TextView info;
        TextView action;
    }
}
