package com.qw.ffmpeg

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.coder.ffmpeg.jni.FFmpegCommand.setDebug
import com.coder.ffmpeg.jni.FFmpegCommand.runCmd
import com.coder.ffmpeg.call.CommonCallBack
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        setDebug(true)
    }

    fun concatVideo(inputFile: String?, targetFile: String?): Array<String?> {
        // ffmpeg -f concat -i inputs.txt -c copy output.flv
        var command = "ffmpeg -f concat -i %s -c copy %s"
        command = String.format(command, inputFile, targetFile)
        return command.split(" ") //以空格分割为字符串数组
            .toTypedArray()
    }

    //ffmpeg -i input1.mp4 -i input2.webm -i input3.avi -filter_complex
    // '[0:0] [0:1] [1:0] [1:1] concat=n=3:v=1:a=1 [v] [a]' -map '[v]' -map '[a]' <编码器选项> output.mkv

    fun merge(view: View?) {
        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
        println("qinwei$downloadDir")
        val content = "$downloadDir/content.mp4"
        val append = "$downloadDir/logo.mp4"
        val input = FileUtils.createInputFile(downloadDir, arrayOf(content, append))
        val output = "$downloadDir/merge.mp4"
        val comm = concatVideo(input, output)
        thread {
            println("qinwei runCmd")
            runCmd(comm, object : CommonCallBack() {
                override fun onStart() {
                    println("qinwei onStart")
                }

                override fun onComplete() {
                    println("qinwei onComplete")
                }

                override fun onProgress(progress: Int, pts: Long) {
                    super.onProgress(progress, pts)
                    println("qinwei onProgress $progress $pts")
                }
            })
        }
    }
}