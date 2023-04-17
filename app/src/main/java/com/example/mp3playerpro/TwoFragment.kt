package com.example.mp3playerpro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mp3playerpro.databinding.FragmentTwoBinding


class TwoFragment : Fragment() {
    lateinit var binding: FragmentTwoBinding
    lateinit var dbOpenHelper: DBOpenHelper

    override fun onResume() {
        super.onResume()
        refreshItem()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTwoBinding.inflate(inflater)
       refreshItem()
        return binding.root
    }
    fun refreshItem(){
        dbOpenHelper = DBOpenHelper( binding.root.context, MainActivity.DB_NAME, MainActivity.VERSION)
        val musicDataLikeList = dbOpenHelper.selectMusicLike()
        binding.recyclerview2.adapter = TwoFragmentAdapter(binding.root.context,this, musicDataLikeList!!)
        binding.recyclerview2.layoutManager = LinearLayoutManager(binding.root.context)
    }
}

