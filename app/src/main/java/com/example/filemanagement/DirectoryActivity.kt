package com.example.filemanagement

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class DirectoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fileAdapter: FileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val directoryPath = intent.getStringExtra("directoryPath") ?: return
        val directory = File(directoryPath)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val files = directory.listFiles()?.toList() ?: emptyList()
        fileAdapter = FileAdapter(files, ::onFileClick)
        recyclerView.adapter = fileAdapter
    }

    private fun onFileClick(file: File) {
        // Tương tự như MainActivity
        if (file.isDirectory) {
            val intent = Intent(this, DirectoryActivity::class.java)
            intent.putExtra("directoryPath", file.absolutePath)
            startActivity(intent)
        } else if (file.extension == "txt") {
            val intent = Intent(this, FileViewerActivity::class.java)
            intent.putExtra("filePath", file.absolutePath)
            startActivity(intent)
        }
    }
}
