package com.ventoray.projectmanager.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Environment
import android.util.Log

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.HttpURLConnection
import java.net.URL
import android.R.attr.path
import java.nio.file.DirectoryNotEmptyException
import java.nio.file.Files.delete



/**
 * Created by Nick on 2/6/2018.
 */

object FileManager {

    val LOG_TAG = "FileManager"

    interface OnBitmapRetrievedListener {
        fun onBitmapRetrieved(bitmap: Bitmap)
    }

    fun readBitmapFromFile(context: Context, fileName: String): Bitmap? {
        val mediaStorageDir = File(
            Environment.getExternalStorageDirectory().toString()
                    + "/Android/data/"
                    + context.applicationContext.packageName
                    + "/Files"
        )
        var bitmap: Bitmap? = null


        bitmap = BitmapFactory.decodeFile(mediaStorageDir.path + File.separator + fileName)
        return bitmap
    }


    /**
     * This came from a stackoverflow post/answer by GoCrazy
     *
     * Create a File for saving an image or video  */
    fun getOutputMediaFile(context: Context, fileName: String): File? {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        val mediaStorageDir = File(
            Environment.getExternalStorageDirectory().toString()
                    + "/Android/data/"
                    + context.applicationContext.packageName
                    + "/Files"
        )

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        // Create a media file name
        val mediaFile: File
        mediaFile = File(mediaStorageDir.path + File.separator + fileName)
        return mediaFile
    }


    fun deleteBitmapFile(context: Context, fileName: String): Boolean {
        val mediaStorageDir = File(
            Environment.getExternalStorageDirectory().toString()
                    + "/Android/data/"
                    + context.applicationContext.packageName
                    + "/Files"
        )

        val file = File(mediaStorageDir.path + File.separator + fileName)
        return file.delete()
    }


    fun writeBitmapToFile(context: Context, bitmap: Bitmap?, fileName: String) {
        var fileOutputStream: FileOutputStream? = null
        val pictureFile = getOutputMediaFile(context, fileName)
        if (pictureFile == null) {
            Log.d(
                LOG_TAG,
                "Error creating media file, check storage permissions: "
            )// e.getMessage());
            return
        }
        try {
            fileOutputStream = FileOutputStream(pictureFile)
            if (bitmap == null) {
                Log.e(LOG_TAG, "Could not write null bitmap object to file")
                return
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)

        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error: " + e.message)
        } finally {
            try {
                fileOutputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun writeObjectToFile(context: Context, o: Any?, fileName: String) {

        val outputStream: FileOutputStream
        val objectOutputStream: ObjectOutputStream

        if (o == null) return

        try {

            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            objectOutputStream = ObjectOutputStream(outputStream)
            objectOutputStream.writeObject(o)
            objectOutputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            Log.e(
                LOG_TAG, "Error: $e"
            )
        }

    }


    fun readObjectFromFile(context: Context, fileName: String): Any? {
        var `object`: Any? = null
        val fileInputStream: FileInputStream
        val objectInputStream: ObjectInputStream

        try {
            fileInputStream = context.openFileInput(fileName)
            objectInputStream = ObjectInputStream(fileInputStream)
            `object` = objectInputStream.readObject()
            objectInputStream.close()
            fileInputStream.close()

        } catch (e: FileNotFoundException) {
            Log.e(LOG_TAG, e.localizedMessage)
            return null
        } catch (e: IllegalStateException) {
            Log.e(LOG_TAG, e.localizedMessage)
            return null
        } catch (e: IOException) {
            Log.e(LOG_TAG, e.localizedMessage)
            return null
        } catch (e: ClassNotFoundException) {
            Log.e(LOG_TAG, e.localizedMessage)
            return null
        } catch (e: java.lang.Exception) {
            Log.e(LOG_TAG, e.localizedMessage)
        }

        return `object`
    }

    /**
     * @context: this should be an application context associated with an android application
     * @path: the private file path of the file to be deleted
     */
    fun deleteFile(context: Context, path: String): Boolean {
        try {
            val result: Boolean = context.deleteFile(path)
            return result
        } catch (x: NoSuchFileException) {
            System.err.format("%s: no such" + " file or directory%n", path)
            return false
        } catch (x: IOException) {
            // File permission problems are caught here.
            System.err.println(x)
            return false
        }

    }


    fun getBitmapFromURL(resource: String): Bitmap? {
        var bitmap: Bitmap? = null
        Log.d(LOG_TAG, "Bitmap URL: $resource")
        try {
            val url = URL(resource)
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.doInput = true
            httpURLConnection.connect()
            Log.d(LOG_TAG, httpURLConnection.responseMessage)

            val inputStream = httpURLConnection.inputStream
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            if (e == null) return null
            //            Log.d(LOG_TAG, e.getMessage());
        }

        return bitmap
    }


}
