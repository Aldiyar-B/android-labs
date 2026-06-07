package com.example.lab13;

import android.text.format.Formatter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileItem {

    private final File file;

    public FileItem(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return file.getName();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public int getIconResId() {
        if (file.isDirectory()) {
            return R.drawable.ic_folder;
        }
        String name = file.getName().toLowerCase(Locale.ROOT);
        if (name.endsWith(".jpg") || name.endsWith(".jpeg")
                || name.endsWith(".png") || name.endsWith(".gif")
                || name.endsWith(".webp") || name.endsWith(".bmp")) {
            return R.drawable.ic_image;
        }
        if (name.endsWith(".txt") || name.endsWith(".csv")
                || name.endsWith(".json") || name.endsWith(".html")
                || name.endsWith(".doc") || name.endsWith(".docx")
                || name.endsWith(".pdf")) {
            return R.drawable.ic_text;
        }
        return R.drawable.ic_file;
    }

    public String getInfo(android.content.Context context) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            int count = children == null ? 0 : children.length;
            return "Папка • элементов: " + count;
        }
        String size = Formatter.formatShortFileSize(context, file.length());
        String date = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                .format(new Date(file.lastModified()));
        return size + " • " + date;
    }
}
