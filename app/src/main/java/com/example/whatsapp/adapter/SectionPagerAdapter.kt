package com.example.whatsapp.adapter

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.whatsapp.activity.MainActivity
import com.example.whatsapp.fragment.ChatsFragment
import com.example.whatsapp.fragment.StatusListFragment
import com.example.whatsapp.fragment.StatusUpdateFragment
import java.text.FieldPosition

//Class SectionPagerAdapter berfungsi untuk mengatur penempatan 3 halaman utama
// supaya dapat disesuaikan ke dalam ViewPager selain untuk mengatur posisi penempatan,
// adapter juga akan menyediakan akses ke item.
class SectionPagerAdapter(fm:FragmentManager):FragmentPagerAdapter(fm) {
    private val chatsFragment = ChatsFragment()
    private val statusUpdateFragment = StatusUpdateFragment()
    private val statusFragment = StatusListFragment()

    override fun getItem(position: Int):Fragment{
        return when (position){
            0 -> statusUpdateFragment
            1 -> chatsFragment
            2 -> statusFragment
            else -> chatsFragment
        }
    }

    override fun getCount():Int{
        return 3
    }
}