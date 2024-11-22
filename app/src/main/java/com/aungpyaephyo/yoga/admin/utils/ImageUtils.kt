//package com.aungpyaephyo.yoga.admin.utils
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.net.Uri
//import android.util.Base64
//import androidx.core.content.FileProvider
//import java.io.ByteArrayOutputStream
//import java.io.File
//import java.io.IOException
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//class ImageUtils(
//    private val context: Context
//) {
//    var currentPhotoUri: Uri? = null
//        private set
//
//    fun resetCurrentPhotoUri() {
//        currentPhotoUri = null
//    }
//
//    fun createImageUri(): Uri? {
//        val photoFile: File? = try {
//            createImageFile()
//        } catch (ex: IOException) {
//            null
//        }
//        currentPhotoUri = photoFile?.let {
//             FileProvider.getUriForFile(
//                context,
//                "com.myolwinoo.universalyoga.admin.fileprovider",
//                it
//            )
//        }
//        return currentPhotoUri
//    }
//
//    fun createBitmap(uri: Uri): Bitmap {
//        return BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
//    }
//
//    fun imageUriToBase64(imageUri: Uri): String? {
//        return try {
//            val inputStream = context.contentResolver.openInputStream(imageUri)
//            val bitmap = BitmapFactory.decodeStream(inputStream)
//            val byteArrayOutputStream = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
//            val byteArray = byteArrayOutputStream.toByteArray()
//            Base64.encodeToString(byteArray, Base64.DEFAULT)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    fun base64ToBitmap(base64String: String): Bitmap? {
//        return try {
//            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
//            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    @Throws(IOException::class)
//    private fun createImageFile(): File {
//        // Create an image file name
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
//            .format(Date())
//        val storageDir = context.filesDir.resolve("yoga-images")
//        storageDir.mkdirs()
//        return File.createTempFile(
//            "JPEG_${timeStamp}_", /* prefix */
//            ".jpg", /* suffix */
//            storageDir /* directory */
//        )
//    }
//}