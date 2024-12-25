package com.example.filemanagement

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_PERMISSION = 1001
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var fileAdapter: FileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION
            )
        } else {
            // Chạy nếu quyền đã được cấp
            loadFiles(Environment.getExternalStorageDirectory())
        }

    }

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                loadFiles(Environment.getExternalStorageDirectory())
            } else {
                Toast.makeText(this, "Quyền truy cập bị từ chối!", Toast.LENGTH_SHORT).show()
            }
        }

    private fun loadFiles(directory: File) {
        // Kiểm tra nếu thư mục không tồn tại hoặc không phải thư mục
        if (!directory.exists() || !directory.isDirectory) {
            Toast.makeText(this, "Thư mục không tồn tại hoặc không hợp lệ!", Toast.LENGTH_SHORT).show()
            Log.e("FileManager", "Thư mục không hợp lệ: ${directory.path}")
            return
        }

        val files = directory.listFiles()
        if (files == null) {
            Toast.makeText(this, "Không thể lấy danh sách file!", Toast.LENGTH_SHORT).show()
            Log.e("FileManager", "listFiles() trả về null cho thư mục: ${directory.path}")
            return
        }

        val fileList = files.toList()
        if (fileList.isEmpty()) {
            Toast.makeText(this, "Thư mục trống!", Toast.LENGTH_SHORT).show()
        } else {
            fileAdapter = FileAdapter(fileList, ::onFileClick)
            recyclerView.adapter = fileAdapter
        }
    }




    private fun onFileClick(file: File) {
        if (file.isDirectory) {
            val intent = Intent(this, DirectoryActivity::class.java)
            intent.putExtra("directoryPath", file.absolutePath)
            startActivity(intent)
        } else if (file.extension == "txt") {
            val intent = Intent(this, FileViewerActivity::class.java)
            intent.putExtra("filePath", file.absolutePath)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Không thể mở file này!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền được cấp
                loadFiles(Environment.getExternalStorageDirectory())
            } else {
                // Quyền bị từ chối
                Toast.makeText(this, "Quyền truy cập bị từ chối!", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
