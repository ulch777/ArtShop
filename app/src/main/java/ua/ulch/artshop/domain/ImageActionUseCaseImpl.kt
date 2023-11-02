package ua.ulch.artshop.domain

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Parcelable
import android.provider.MediaStore
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import ua.ulch.artshop.R
import ua.ulch.artshop.presentation.model.ArtShopException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

private const val PNG_EXT = ".png"
private const val PNG_MIME_TYPE = "image/png"
private const val JPG_EXT = ".jpg"
private const val JPG_MIME_TYPE = "image/jpg"
private const val INTENT_IMAGE_TYPE = "image/*"
private const val INTENT_TEXT_TYPE = "text/plain"
private const val IMAGE_PREFIX = "IMG_"
private const val IMAGE_TITLE = "IMG"
private const val OF = "_of_"
private const val PROVIDER = ".provider"
private const val PATTERN = "yyyy.MM.dd 'at' HH:mm:ss z"
private const val FACEBOOK_KATANA = "com.facebook.katana"
private const val FACEBOOK_LITE = "com.facebook.lite"
private const val FACEBOOK_ANDROID = "com.facebook.android"
private const val FACEBOOK_EXAMPLE = "com.example.facebook"
private const val SHARE_APP_LINK = "https://play.google.com/store/apps/details?id=ua.ulch.artshop"


@Singleton
class ImageActionUseCaseImpl @Inject constructor(private val context: Context?) :
    ImageActionUseCase {

    override suspend fun getShareImageObject(
        text: String,
        imageUrl: String?,
        socialNetworks: SocialNetworks
    ): Parcelable? {
        return context?.let {
            val loader = ImageLoader(it)
            val request = ImageRequest.Builder(it).data(imageUrl)
                .allowHardware(false) // Disable hardware bitmaps.
                .build()
            val result = (loader.execute(request) as SuccessResult).drawable
            val bitmap = (result as BitmapDrawable).bitmap

            when (socialNetworks) {
                SocialNetworks.FACEBOOK -> shareFaceBook(bitmap, text)
                else -> createShareIntent(context, socialNetworks, bitmap, text)
            }
        }
    }

    override suspend fun downloadImage(url: String?): Boolean? {
        return context?.let {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context).data(url)
                .allowHardware(false) // Disable hardware bitmaps.
                .build()
            val result = (loader.execute(request) as SuccessResult).drawable
            val bitmap = (result as BitmapDrawable).bitmap
            val fileName = url?.substring(url.lastIndexOf("/") + 1)

            saveImage(
                fileName = fileName, bitmap = bitmap
            )
        }

    }

    override suspend fun createInvitation(socialNetworks: SocialNetworks): Intent? {
        when (socialNetworks) {
            SocialNetworks.NOT_SPECIFIED -> {
                context.apply {
                    val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
                    intent.type = INTENT_TEXT_TYPE
                    intent.putExtra(Intent.EXTRA_TEXT, SHARE_APP_LINK)
                    return intent
                }
            }

            else -> {
                val appName = socialNetworks.appPackage
                val isAppInstalled = isAppAvailable(appName)
                if (isAppInstalled) {
                    try {
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = INTENT_TEXT_TYPE
                        intent.setPackage(appName)
                        intent.putExtra(Intent.EXTRA_TEXT, SHARE_APP_LINK)

                        return Intent.createChooser(
                            intent,
                            context?.resources
                                ?.getString(R.string.invite_friend_via, socialNetworks.appName)
                        )
                    } catch (e: Exception) {
                        throw ArtShopException("${e.message}")
                    }
                } else {
                    throw ArtShopException(
                        "${socialNetworks.appName} " +
                                context?.getString(R.string.not_installed)
                    )
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun saveImage(
        bitmap: Bitmap?,
        fileName: String?,
    ): Boolean {
        var fos: OutputStream? = null
        val imageFile: File
        val imageUri: Uri?
        try {
            val imagesDir = File(
                Environment.getExternalStoragePublicDirectory(
                    DIRECTORY_PICTURES
                ).toString() + File.separator
            )
            if (!imagesDir.exists()) imagesDir.mkdir()
            imageFile = File(imagesDir, fileName + PNG_EXT)
            if (imageFile.exists()) return true

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context?.contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, PNG_MIME_TYPE)
                contentValues.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    DIRECTORY_PICTURES + File.separator
                )
                imageUri =
                    resolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                if (imageUri == null) throw IOException(context?.getString(R.string.media_store_error))
                fos = resolver?.openOutputStream(imageUri)
            } else {

                fos = FileOutputStream(imageFile)
            }
            bitmap?.let {
                if (!it.compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        fos!!
                    )
                ) throw IOException(context?.getString(R.string.save_bitmap_error))
            }
            fos?.flush()
        } finally {
            fos?.close()
        }
        return true
    }

    private fun shareFaceBook(bitmap: Bitmap, text: String): SharePhotoContent {
        if (!TextUtils.isEmpty(isFacebookAppInstalled())) {
            try {
                val photoBuilder = SharePhoto.Builder()
                if (text.isNotEmpty())
                    photoBuilder.setCaption(text)
                photoBuilder.setBitmap(bitmap).setCaption(text)
                val photo = photoBuilder.build()
                return SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build()
            } catch (e: Exception) {
                throw ArtShopException("${context?.resources?.getString(R.string.facebook_not_installed)}")
            }
        } else {
            throw ArtShopException("${context?.resources?.getString(R.string.facebook_not_installed)}")
        }
    }

    private fun createShareIntent(
        context: Context,
        socialNetworks: SocialNetworks,
        resource: Bitmap,
        text: String
    ): Intent {
        if (socialNetworks != SocialNetworks.NOT_SPECIFIED) {
            val appName = socialNetworks.appPackage
            val isAppInstalled =
                isAppAvailable(appName)
            if (isAppInstalled) {
                try {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = INTENT_IMAGE_TYPE
                    intent.setPackage(appName)
                    val screenshotUri = saveFile(context, resource)

                    intent.putExtra(Intent.EXTRA_STREAM, screenshotUri)
                    if (text.isNotEmpty())
                        intent.putExtra(Intent.EXTRA_TEXT, text)
                    return Intent.createChooser(
                        intent,
                        context.resources
                            .getString(R.string.share_image_via, socialNetworks.appName)
                    )
                } catch (e: Exception) {
                    throw ArtShopException("${e.message}")
                }
            } else {
                throw ArtShopException(
                    "${socialNetworks.appName} " +
                            context.getString(R.string.not_installed)
                )
            }
        } else {
            context.apply {
                val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
                intent.type = INTENT_IMAGE_TYPE
                val screenshotUri = saveFile(context, resource)
                val tmpList: ArrayList<Uri> = ArrayList()
                screenshotUri?.let {
                    tmpList.add(it)
                }
                intent.putExtra(Intent.EXTRA_STREAM, tmpList)
                if (text.isNotEmpty())
                    intent.putExtra(Intent.EXTRA_TEXT, text)
                return intent
            }
        }
    }

    private fun saveFile(context: Context, imageBitMap: Bitmap): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveImageInQ(context, imageBitMap)
        } else saveImageInLegacy(context, imageBitMap)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageInQ(context: Context, imageBitMap: Bitmap): Uri? {
        val filename = IMAGE_PREFIX + System.currentTimeMillis() + JPG_EXT
        var fos: OutputStream?
        var imageUri: Uri?
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, JPG_MIME_TYPE)
            put(MediaStore.MediaColumns.RELATIVE_PATH, DIRECTORY_PICTURES)
            put(MediaStore.Video.Media.IS_PENDING, 1)
        }

        val contentResolver = context.applicationContext.contentResolver

        contentResolver.also { resolver ->
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }

        fos?.use { imageBitMap.compress(Bitmap.CompressFormat.JPEG, 70, it) }

        contentValues.clear()
        contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
        imageUri?.let { contentResolver.update(it, contentValues, null, null) }

        return imageUri
    }

    private fun saveImageInLegacy(context: Context, bitmap: Bitmap): Uri? {
        val dateFormatter = SimpleDateFormat(
            PATTERN, Locale.getDefault()
        )
        val appContext = context.applicationContext
        val title = IMAGE_TITLE
        val filename = title + OF + dateFormatter.format(Date()) + PNG_EXT
        val directory = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)
        val file = File(directory, filename)
        val outStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        outStream.flush()
        outStream.close()
        MediaScannerConnection.scanFile(
            appContext, arrayOf(file.absolutePath),
            null, null
        )
        return FileProvider.getUriForFile(
            appContext, appContext.packageName + PROVIDER,
            file
        )
    }

    private fun isFacebookAppInstalled(): String? {
        return context?.let {
            val pm: PackageManager = it.packageManager
            var applicationInfo: ApplicationInfo
            try {
                applicationInfo = pm.getApplicationInfo(FACEBOOK_KATANA, 0)
                return if (applicationInfo.enabled) FACEBOOK_KATANA else ""
            } catch (ignored: Exception) {
            }
            try {
                applicationInfo = pm.getApplicationInfo(FACEBOOK_LITE, 0)
                return if (applicationInfo.enabled) FACEBOOK_LITE else ""
            } catch (ignored: Exception) {
            }
            try {
                applicationInfo = pm.getApplicationInfo(FACEBOOK_ANDROID, 0)
                return if (applicationInfo.enabled) FACEBOOK_ANDROID else ""
            } catch (ignored: Exception) {
            }
            try {
                applicationInfo = pm.getApplicationInfo(FACEBOOK_EXAMPLE, 0)
                return if (applicationInfo.enabled) FACEBOOK_EXAMPLE else ""
            } catch (ignored: java.lang.Exception) {
            }
            ""
        }
    }

    private fun isAppAvailable(appName: String?): Boolean {
        val pm = context?.packageManager
        return try {
            pm?.getPackageInfo(appName!!, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}
