package com.example.whatsapp.listener

interface ChatClickListener : FailureCallback {
    fun onChatClicked(name: String?,otherUserId:String?,chatsImageUrl:String?,chatsName:String?)
}