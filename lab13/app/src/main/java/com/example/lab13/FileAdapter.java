package com.example.lab13;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Кастомный адаптер для отображения файлов и папок с иконками,
 * размером и датой изменения (усовершенствование по п.2 задания).
 */
public class FileAdapter extends ArrayAdapter<File> {

    private final LayoutInflater inflater;
    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    public FileAdapter(Context context, List<File> files) {
        super(context, 0, files);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item_file, parent, false);
        }

        ImageView icon = view.findViewById(R.id.itemIcon);
        TextView name = view.findViewById(R.id.itemName);
        TextView info = view.findViewById(R.id.itemInfo);

        File file = getItem(position);
        if (file == null) return view;

        name.setText(file.getName());

        if (file.isDirectory()) {
            icon.setImageResource(R.drawable.ic_folder);
            File[] children = file.listFiles();
            int count = children != null ? children.length : 0;
            info.setText("Папка • элементов: " + count);
        } else {
            icon.setImageResource(getIconForFile(file.getName()));
            String size = Formatter.formatShortFileSize(getContext(), file.length());
            String date = dateFormat.format(new Date(file.lastModified()));
            info.setText(size + " • " + date);
        }
        return view;
    }

    // Подбор иконки по расширению файла
    private int getIconForFile(String fileName) {
        String lower = fileName.toLowerCase(Locale.getDefault());
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                || lower.endsWith(".png") || lower.endsWith(".gif")
                || lower.endsWith(".webp") || lower.endsWith(".bmp")) {
            return R.drawable.ic_image;
        }
        if (lower.endsWith(".txt") || lower.endsWith(".doc")
                || lower.endsWith(".docx") || lower.endsWith(".pdf")) {
            return R.drawable.ic_text;
        }
        return R.drawable.ic_file;
    }
}
