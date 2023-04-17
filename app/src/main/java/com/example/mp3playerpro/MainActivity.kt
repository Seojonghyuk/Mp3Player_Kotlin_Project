package com.example.mp3playerpro

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mp3playerpro.databinding.ActivityMainBinding
import com.example.mp3playerpro.databinding.UsertabButtonBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    companion object{
        val REQUEST_CODE = 100
        val VERSION = 1
        val DB_NAME = "musicDB3"
    }
    //*********************************************************************************************
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    //데이타베이스 생성
    private val dbOpenHelper by lazy { DBOpenHelper(this, DB_NAME, VERSION) }
    val permission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    var musicDataList: MutableList<MusicData>? = mutableListOf<MusicData>()
    lateinit var customViewpagerAdapter : CustomViewpagerAdapter
    lateinit var musicRecyclerAdapter: MusicRecyclerAdapter

    //*********************************************************************************************

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        customViewpagerAdapter = CustomViewpagerAdapter(this)
        customViewpagerAdapter.addListFragment(OneFragment())
        customViewpagerAdapter.addListFragment(TwoFragment())
        customViewpagerAdapter.addListFragment(ThreeFragment())
        binding.viewpager2.adapter = customViewpagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewpager2) { tab, position ->
            tab.setCustomView(tabCustomView(position))
        }.attach()

        //외장메모리 읽기 승인
        var flag = ContextCompat.checkSelfPermission(this,permission[0])
        if(flag == PackageManager.PERMISSION_GRANTED){
            startProcess()
        }else{
            //승인요청
            ActivityCompat.requestPermissions(this, permission, REQUEST_CODE)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startProcess()
            }else{
                Toast.makeText(this, "권한승인을 해야만 앱을 사용할 수 있어요.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    private fun startProcess() {
        // 데이타베이스를 조회해서 음악파일이 있다면, 음원정보를 가져와서 데이타베이스 입력했음을 뜻함
        // 데이타베이스를 조회해서 음악파일이 없다면, 음원정보를 가져와서 데이타베이스 입력하지 않음을 뜻함.
        //1. 데이타베이스에서 음원파일을 가져온다.
        var musicDataDBList: MutableList<MusicData>? = mutableListOf<MusicData>()
        musicDataDBList = dbOpenHelper.selectAllMusicTBL()
        Log.e("MainActivity", "musicDataList.size = ${musicDataDBList?.size}")

        if(musicDataDBList == null || musicDataDBList!!.size <= 0){
            //start 음원정보를 가져옴 **********************************************
            val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION,
            )
            val cursor = contentResolver.query(musicUri, projection, null, null, null)

            if(cursor!!.count <= 0){
                Toast.makeText(this, "메모리에 음악파일에 없습니다. 다운받아주세요.", Toast.LENGTH_SHORT).show()
                finish()
            }
            while(cursor.moveToNext()){
                val id = cursor.getString(0)
                val title = cursor.getString(1).replace("'","")
                val artist = cursor.getString(2).replace("'","")
                val albumId = cursor.getString(3)
                val duration = cursor.getInt(4)
                val musicData = MusicData(id, title, artist, albumId, duration,0 , 0)
                musicDataList?.add(musicData)
            }
            Log.e("MainActivity", "2 musicDataList.size = ${musicDataList?.size}")
            //end 음원정보를 가져옴 **********************************************
            //음악테이블에 모든정보를 insert함
            var size = musicDataList?.size
            if (size != null) {
                for(index in 0..size -1){
                    val musicData = musicDataList!!.get(index)
                    dbOpenHelper.insertMusicTBL(musicData)
                }
            }

        }else{
            musicDataList = musicDataDBList
        }
    }
    fun tabCustomView(position:Int): View {
        val binding = UsertabButtonBinding.inflate(layoutInflater)
        when(position){
            0 -> binding.ivIcon.setImageResource(R.drawable.list_24)

            1 -> binding.ivIcon.setImageResource(R.drawable.baseline_favorite_full_24)

            2 -> binding.ivIcon.setImageResource(R.drawable.baseline_playlist_play_24)
        }
        return binding.root
    }
}