package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private var song: Song = Song()
//    private lateinit var player: Player
    // 미디어 플레이어 객체
    private var mediaPlayer: MediaPlayer? = null
    // GSON 객체 선언
    private var gson: Gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()

        //val song = Song(binding.mainMiniPlayerTitleTv.text.toString(), binding.mainMiniPlayerSingerTv.text.toString())
        val song = Song("가을 망상", "season_mangsang", "Tuesday Club", 0, 218, false)
        val music = resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer = MediaPlayer.create(this,music)
        setMiniPlayer(song)

//        player = Player(song.playTime, song.isPlaying)
//        player.start()

        binding.mainPlayerLayout.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("music", song.music)
            intent.putExtra("singer", song.singer)
            intent.putExtra("second", song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            startActivity(intent)
        }

        binding.mainMiniplayerBtn.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("isPlaying", true)
            setMiniPlayerStatus(true)
            setMiniPlayer(song)
//            startActivity(intent)
        }
        binding.mainPauseBtn.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("isPlaying", false)
            setMiniPlayerStatus(false)
            setMiniPlayer(song)
//            startActivity(intent)
        }



        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }

    }
//
//    inner class Player(private val playTime: Int, var isPlaying: Boolean) : Thread() {
//        private var second = 0
//
//        override fun run() {
//            try {
//                while (true) {
//                    if (second >= playTime) {
//
//                        break
//                    }
//
//                    if (isPlaying) {
//                        sleep(1000)     // sleep은 Thread에서 쓸수있는 메소드
//                        second++                // 1초마다 second가 1씩 더해짐.
//
//                        // 여기서 그냥 binding을 쓰면 중첩 클래스가 됨. 내부클래스로 쓰려면 부모인 Player 클래스에 inner 써야해
//                        // WorkThread(메인 스레드가 아닌 스레드) 에서는 뷰 랜더링을 할 수 없다.
//                        //binding.songStartTimeTv.text = String.format("%02d:%02d",second/60, second%60)
//                        runOnUiThread {
//                            binding.mainPlayerSb.progress = second * 1000 / playTime
//
//                        }
//                    }
//
//                }
//            } catch (e: InterruptedException) {
//                Log.d("interrupt", "쓰레드가 종료되었습니다.")
//            }
//        }
//    }


    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

    }

    fun setMiniPlayerStatus(isPlaying: Boolean) {
        if (isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
            mediaPlayer?.start()

        } else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
            mediaPlayer?.pause()
        }
    }

    fun setMiniPlayer(song: Song) {
        binding.mainMiniPlayerTitleTv.text = song.title
        binding.mainMiniPlayerSingerTv.text = song.singer
        binding.mainPlayerSb.progress = song.second * 1000


    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        // MODE_PRIVATE 은 이 앱에서만 sharedPreference에 접근한다는 뜻
        val jsonSong = sharedPreferences.getString("song", null)
        song = if (jsonSong == null) {
            Song("가을 망상", "season_mangsang", "Tuesday Club", 0, 218, false)
        } else {
            gson.fromJson(jsonSong, Song::class.java)
        }
        setMiniPlayer(song)

    }



}




