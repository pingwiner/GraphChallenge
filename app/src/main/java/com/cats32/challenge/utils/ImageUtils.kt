package com.cats32.challenge.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream



suspend fun saveImageToGallery(bitmap: Bitmap, context: Context): Boolean = withContext(Dispatchers.IO) {
    val TAG = "ImageUtils"

    if (Build.VERSION.SDK_INT >= 29) {
        val values = contentValues()
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Graph")
        values.put(MediaStore.Images.Media.IS_PENDING, true)

        val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
            values.put(MediaStore.Images.Media.IS_PENDING, false)
            val updated = context.contentResolver.update(uri, values, null, null)
            return@withContext updated > 0
        } else {
            Log.e(TAG, "saveImageToGallery has failed")
        }
        return@withContext false
    } else {
        val directory = File(Environment.getExternalStorageDirectory().toString() + "/Graph")

        if (!directory.exists()) {
            directory.mkdirs()
        }
        val fileName = System.currentTimeMillis().toString() + ".png"
        val file = File(directory, fileName)
        saveImageToStream(bitmap, FileOutputStream(file))
        val values = contentValues()
        values.put(MediaStore.Images.Media.DATA, file.absolutePath)
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        return@withContext true
    }
}

private fun contentValues() : ContentValues {
    val values = ContentValues()
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
    values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
    return values
}

private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
    if (outputStream != null) {
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

