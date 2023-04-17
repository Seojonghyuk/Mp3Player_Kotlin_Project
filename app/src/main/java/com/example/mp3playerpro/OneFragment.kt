package com.example.mp3playerpro

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mp3playerpro.databinding.FragmentOneBinding


class OneFragment : Fragment() {
    lateinit var mainActivity: MainActivity
    lateinit var binding: FragmentOneBinding
    lateinit var dbOpenHelper: DBOpenHelper
    lateinit var dataList : MutableList<MusicData>
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        refreshItem()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOneBinding.inflate(inflater)
        refreshItem()
        return binding.root
    }
    fun refreshItem(){
        dataList = mutableListOf()
        dbOpenHelper = DBOpenHelper( binding.root.context, MainActivity.DB_NAME, MainActivity.VERSION)
        dataList = dbOpenHelper.selectAllMusicTBL()!!
        binding.recyclerview.adapter = MusicRecyclerAdapter(binding.root.context, dataList)
        binding.recyclerview.layoutManager = LinearLayoutManager(binding.root.context)
    }
}