package com.example.filemanagement

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class FileViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_viewer)

        val filePath = intent.getStringExtra("filePath") ?: return
        val file = File(filePath)

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = file.readText()
    }
}
