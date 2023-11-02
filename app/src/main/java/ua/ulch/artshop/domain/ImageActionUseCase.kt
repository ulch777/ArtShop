package ua.ulch.artshop.domain

import android.content.Intent
import android.os.Parcelable

interface ImageActionUseCase {
    suspend fun getShareImageObject(text: String, imageUrl: String?, socialNetworks: SocialNetworks)
            : Parcelable?

    suspend fun downloadImage(url: String?): Boolean?

    suspend fun createInvitation(socialNetworks: SocialNetworks): Intent?
}

enum class SocialNetworks(val appPackage: String, val appName: String) {
    FACEBOOK("facebook", "facebook"),
    TELEGRAM("org.telegram.messenger", "Telegram"),
    VIBER("com.viber.voip", "Viber"),
    WHATSAPP("com.whatsapp", "Whatsapp"),
    PINTEREST("com.pinterest", "Pinterest"),
    TWITTER("com.twitter.android", "Twitter"),
    INSTAGRAM("com.instagram.android", "Instagram"),
    NOT_SPECIFIED("", "");

}