package com.example.lab07

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView)

        imageView.setOnClickListener {
            val popupMenu = PopupMenu(this, imageView)

            popupMenu.menu.add(1, 1, 1, "slot1")
            popupMenu.menu.add(1, 2, 2, "slot2")

            popupMenu.setOnMenuItemClickListener {
                textView.text = getString(R.string.selected_item, it.title)
                true
            }

            popupMenu.show()
        }
    }
}