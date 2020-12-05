package com.example.whatsapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class Model(

    val info: String? = "",
    val phone: String? = "",
    val name: String? = "",
    val imageUrl: String? = "",
    val status: String? = "",
    val statusUrl: String? = "",
    val statusTime: String? = ""

)

data class Contact(

    val name:String?,
    val phone: String?
)

data class Chat(
    val chatParticipants: ArrayList<String>
)

data class Message(

    val sentBy: String? = "",
    val message: String? = "",
    val messageTime: Long? = 0

)

@Parcelize
data class StatusListElement(
    val userName: String?,
    val userUrl: String?,
    val status: String?,
    val statusUrl: String?,
    val statusTime: String?
): Parcelable
