package com.chockqiu.util

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleCoroutineScope
import com.chockqiu.usualktx.CoroutineScopeKtx.launchIOCatch
import java.io.*
import java.nio.file.Files

/**
 * 保存视频/图片到相册工具类
 */
object SaveUtils {

    private const val TAG = "SaveUtils"
    private const val glodName = ""

    /**
     * 将图片文件保存到系统相册
     */
    fun saveImgFileToAlbum(
        lifecycle: LifecycleCoroutineScope,
        context: Context,
        imageFilePath: String?
    ) {
        lifecycle.launchIOCatch {
            val bitmap = BitmapFactory.decodeFile(imageFilePath)
            saveBitmapToAlbum(context, bitmap)
        }
    }

    /**
     * 将bitmap保存到系统相册
     */
    fun saveBitmapToAlbum(context: Context, bitmap: Bitmap?): Boolean {
        if (bitmap == null) {
            return false
        }
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            saveBitmapToAlbumBeforeQ(context, bitmap)
        } else {
            saveBitmapToAlbumAfterQ(context, bitmap)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun saveBitmapToAlbumAfterQ(context: Context, bitmap: Bitmap): Boolean {
        val contentUri: Uri =
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else {
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            }
        val contentValues = getImageContentValues(context)
        val uri = context.contentResolver.insert(contentUri, contentValues) ?: return false
        var os: OutputStream? = null
        return try {
            os = context.contentResolver.openOutputStream(uri)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os)
            //            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                Files.copy(bitmapFile.toPath(), os);
//            }
            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            context.contentResolver.update(uri, contentValues, null, null)
            toastCallback?.invoke("保存成功")
            true
        } catch (e: Exception) {
            context.contentResolver.delete(uri, null, null)
            e.printStackTrace()
            toastCallback?.invoke("保存失败，请重试~")
            false
        } finally {
            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun saveBitmapToAlbumBeforeQ(context: Context, bitmap: Bitmap): Boolean {
        val picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val destFile = File(picDir, glodName + File.separator + System.currentTimeMillis() + ".jpg")
        //            FileUtils.copy(imageFile, destFile.getAbsolutePath());
        var os: OutputStream? = null
        var result = false
        try {
            if (!destFile.exists()) {
                destFile.parentFile.mkdirs()
                destFile.createNewFile()
            }
            os = BufferedOutputStream(FileOutputStream(destFile))
            result = bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os)
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
            toastCallback?.invoke("保存成功")
        } catch (e: IOException) {
            e.printStackTrace()
            toastCallback?.invoke("保存失败，请重试~")
        } finally {
            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        MediaScannerConnection.scanFile(
            context, arrayOf(destFile.absolutePath), arrayOf("image/*")
        ) { path: String, uri: Uri -> Log.d(TAG, "saveImgToAlbum: $path $uri") }
        return result
    }

    /**
     * 获取图片的ContentValue
     *
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    fun getImageContentValues(context: Context): ContentValues {
        val contentValues = ContentValues()
        contentValues.put(
            MediaStore.Images.Media.DISPLAY_NAME,
            System.currentTimeMillis().toString() + ".jpg"
        )
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/*")
        contentValues.put(
            MediaStore.Images.Media.RELATIVE_PATH,
            Environment.DIRECTORY_DCIM + File.separator + context.packageName
        )
        contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1)
        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        contentValues.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis())
        contentValues.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
        return contentValues
    }

    /**
     * 将视频保存到系统相册
     */
    fun saveVideoToAlbum(context: Context, videoFile: String): Boolean {
        Log.d(TAG, "saveVideoToAlbum() videoFile = [$videoFile]")
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            saveVideoToAlbumBeforeQ(context, videoFile)
        } else {
            saveVideoToAlbumAfterQ(context, videoFile)
        }
    }

    private fun saveVideoToAlbumAfterQ(context: Context, videoFile: String): Boolean {
        return try {
            val contentResolver = context.contentResolver
            val tempFile = File(videoFile)
            val contentValues = getVideoContentValues(context, tempFile, System.currentTimeMillis())
            val uri =
                contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
            copyFileAfterQ(context, contentResolver, tempFile, uri)
            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            context.contentResolver.update(uri!!, contentValues, null, null)
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun saveVideoToAlbumBeforeQ(context: Context, videoFile: String): Boolean {
        val picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val tempFile = File(videoFile)
        val destFile = File(picDir, glodName + File.separator + tempFile.name)
        var ins: FileInputStream? = null
        var ous: BufferedOutputStream? = null
        return try {
            ins = FileInputStream(tempFile)
            ous = BufferedOutputStream(FileOutputStream(destFile))
            var nread = 0L
            val buf = ByteArray(1024)
            var n: Int
            while (ins.read(buf).also { n = it } > 0) {
                ous.write(buf, 0, n)
                nread += n.toLong()
            }
            MediaScannerConnection.scanFile(
                context, arrayOf(destFile.absolutePath), arrayOf("video/*")
            ) { path: String, uri: Uri -> Log.d(TAG, "saveVideoToAlbum: $path $uri") }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            try {
                ins?.close()
                ous?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @Throws(IOException::class)
    private fun copyFileAfterQ(
        context: Context,
        localContentResolver: ContentResolver,
        tempFile: File,
        localUri: Uri?
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            context.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.Q
        ) {
            //拷贝文件到相册的uri,android10及以上得这么干，否则不会显示。可以参考ScreenMediaRecorder的save方法
            val os = localContentResolver.openOutputStream(localUri!!)
            Files.copy(tempFile.toPath(), os)
            os!!.close()
            tempFile.delete()
        }
    }

    /**
     * 获取视频的contentValue
     */
    private fun getVideoContentValues(
        context: Context,
        paramFile: File,
        timestamp: Long
    ): ContentValues {
        val localContentValues = ContentValues()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            localContentValues.put(
                MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM
                        + File.separator + context.packageName
            )
        }
        localContentValues.put(MediaStore.Video.Media.TITLE, paramFile.name)
        localContentValues.put(MediaStore.Video.Media.DISPLAY_NAME, paramFile.name)
        localContentValues.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
        localContentValues.put(MediaStore.Video.Media.DATE_TAKEN, timestamp)
        localContentValues.put(MediaStore.Video.Media.DATE_MODIFIED, timestamp)
        localContentValues.put(MediaStore.Video.Media.DATE_ADDED, timestamp)
        localContentValues.put(MediaStore.Video.Media.SIZE, paramFile.length())
        return localContentValues
    }

    private var toastCallback: ((String) -> Unit)? = null

    fun setToastCallback(callback: ((String) -> Unit)?) {
        toastCallback = callback
    }
}