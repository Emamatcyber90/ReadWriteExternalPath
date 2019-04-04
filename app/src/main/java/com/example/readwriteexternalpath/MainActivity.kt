package com.example.readwriteexternalpath

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*


class MainActivity : AppCompatActivity() {
    private val filename = "SampleFile.txt"
    private val filepath = "MyFileStorage"
    var myExternalFile: File? = null
    var myData = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        saveExternalStorage.setOnClickListener {
            try {
                val fos = FileOutputStream(myExternalFile)
                fos.write(myInputText.text.toString().toByteArray())
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            myInputText.setText("")
            response.text = "SampleFile.txt saved to External Storage..."
        }

        getExternalStorage.setOnClickListener {
            try {
                val fis = FileInputStream(myExternalFile)
                val inP = DataInputStream(fis)
                val br = BufferedReader(InputStreamReader(inP))
                var strLine: String?
                myData = ""
                strLine = br.readLine()
                while (strLine != null) {
                    myData += strLine + "\n"
                    strLine = br.readLine()
                }

                inP.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            myInputText.setText(myData)
            response.text = "SampleFile.txt data retrieved from Internal Storage..."
        }
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            saveExternalStorage.isEnabled = false
        } else {
            myExternalFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                var localDirs = getExternalFilesDirs(filepath)
                File(localDirs.last(), filename)
            } else {
                File(getExternalFilesDir(filepath), filename)
            }
        }
    }

    private fun isExternalStorageReadOnly(): Boolean {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState
    }

    private fun isExternalStorageAvailable(): Boolean {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == extStorageState
    }

}
