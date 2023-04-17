package com.example.mp3playerpro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mp3playerpro.databinding.FragmentThreeBinding


class ThreeFragment : Fragment() {

    lateinit var binding: FragmentThreeBinding
    lateinit var dbOpenHelper: DBOpenHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentThreeBinding.inflate(inflater)
        dbOpenHelper = DBOpenHelper( binding.root.context, MainActivity.DB_NAME, MainActivity.VERSION)
        val dataList = dbOpenHelper.selectplaylist()
        binding.recyclerview3.adapter = MusicRecyclerAdapter(binding.root.context,dataList!!)
        binding.recyclerview3.layoutManager = LinearLayoutManager(binding.root.context)
        return binding.root
    }
}
