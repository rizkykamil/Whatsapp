package com.example.whatsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsapp.R
import com.example.whatsapp.model.Message
import kotlinx.android.extensions.LayoutContainer


class ConversationAdapter(private var messages: ArrayList<Message>, val userId: String?) :
    RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {

    companion object {
        val MESSAGE_CURRENT_USER = 1
        val MESSAGE_OTHER_USER = 2
    }

    fun addMessage(message: Message) {
        messages.add(message)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        if (viewType == MESSAGE_CURRENT_USER) {
            return ConversationViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_current_user_message, parent, false)
            )
        } else {
            return ConversationViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_other_user_message, parent, false)
            )
        }
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bindItem(messages[position])
    }

    override fun getItemViewType(position: Int): Int {
        if (messages[position].sentBy.equals(userId)) {
            return MESSAGE_CURRENT_USER
        } else {
            return MESSAGE_OTHER_USER
        }
    }

    class ConversationViewHolder( override val containerView: View?) : RecyclerView.ViewHolder(
        containerView!!
    ), LayoutContainer {

        fun bindItem(message: Message) {
            if (containerView != null) {
                containerView.findViewById<TextView>(R.id.txt_message).text = message.message
            }
        }

    }
}