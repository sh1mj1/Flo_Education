package com.example.flo

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson
import java.util.*

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    // 데이터 렌더링을 위해서 Song 클래스를 전역변수로
    private val song: Song = Song()              // Song 이라는 데이터 클래스. 객체 이름은 song
    private lateinit var player: Player
    // private val handler = Handler(Looper.getMainLooper()) //Handler를 써서 WorkThread에서 뷰에 접근할 수 있지만 runOnUi를 써서 접근할 수도 있다.
    // 미디어 플레이어 객체 생성  // ?는 null 값이 들어올 수 있다는 뜻.
    private var mediaPlayer: MediaPlayer? = null
    // Gson 객체
    private var gson : Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()

        player = Player(song.playTime, song.isPlaying)
        player.start()
        Log.d("player.start", "player_start")


//반복재생 설정
        binding.songBtnRepeatOffIv.setOnClickListener {
            binding.songBtnRepeatOffIv.visibility = View.GONE
            binding.songBtnRepeatOnIv.visibility = View.VISIBLE
        }
        binding.songBtnRepeatOnIv.setOnClickListener {
            binding.songBtnRepeatOnIv.visibility = View.GONE
            binding.songBtnRepeatOneIv.visibility = View.VISIBLE
        }
        binding.songBtnRepeatOneIv.setOnClickListener {
            binding.songBtnRepeatOneIv.visibility = View.GONE
            binding.songBtnRepeatListIv.visibility = View.VISIBLE
        }
        binding.songBtnRepeatListIv.setOnClickListener {
            binding.songBtnRepeatListIv.visibility = View.GONE
            binding.songBtnRepeatOffIv.visibility = View.VISIBLE
        }

//랜덤재생 설정
        binding.songRandomOffIv.setOnClickListener {
            binding.songRandomOffIv.visibility = View.GONE
            binding.songRandomOnIv.visibility = View.VISIBLE
        }
        binding.songRandomOnIv.setOnClickListener {
            binding.songRandomOnIv.visibility = View.GONE
            binding.songRandomOffIv.visibility = View.VISIBLE
        }


//        미니플레이어 연동
        if (intent.hasExtra("isPlaying")) {
            song.isPlaying = intent.getBooleanExtra("isPlaying", false)
            setPlayerStatus(song.isPlaying)
        }

// SongActivity -> MainActivity
        binding.songBtnDownIb.setOnClickListener {
            onBackPressed()
        }

// SongActivity 재생, 일시정지 변경
        binding.songBtnPlayIv.setOnClickListener {
            player.isPlaying = true
            setPlayerStatus(true)
            mediaPlayer?.start()

        }
        binding.songBtnPauseIv.setOnClickListener {
            player.isPlaying = false
            setPlayerStatus(false)
            mediaPlayer?.pause()            // 처음에 MedioPlayer를 ?로 선언했으므로 계속 ? 붙여주어야 함(?: null 값 허용)
        }
    }

    private fun initSong() {

        if (
            intent.hasExtra("title") && intent.hasExtra("music")
            && intent.hasExtra("singer") && intent.hasExtra("second")
            && intent.hasExtra("playTime") && intent.hasExtra("isPlaying")
            && intent.hasExtra("isPlaying")
        )
        {
            song.title = intent.getStringExtra("title")!!
            song.music = intent.getStringExtra("music")!!
            song.singer = intent.getStringExtra("singer")!!
            song.second = intent.getIntExtra("second",0)
            song.playTime = intent.getIntExtra("playTime", 0)
            song.isPlaying = intent.getBooleanExtra("isPlaying", false)
            // music 이라는 String 만으로는 참조할 수 없어서 아래처럼 res의 이름을 가지고 노래파일에 uri 설정해줘
            val music = resources.getIdentifier(song.music, "raw", this.packageName)

            binding.songEndTimeTv.text =
                String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
            binding.songUpperTitleTv.text = song.title
            binding.songUpperSingerTv.text = song.singer
            binding.songPlayerSb.progress = song.second
            setPlayerStatus(song.isPlaying)
            mediaPlayer = MediaPlayer.create(this,music)    // 미디어 플레이어에 context 앞에서 설정한 uri로
        } else {
            Log.d("조건문 안 song.title", song.title)

        }
    }

    fun setPlayerStatus(isPlaying: Boolean) {
        if (isPlaying) {
            binding.songBtnPlayIv.visibility = View.GONE
            binding.songBtnPauseIv.visibility = View.VISIBLE
        } else {
            binding.songBtnPlayIv.visibility = View.VISIBLE
            binding.songBtnPauseIv.visibility = View.GONE
        }
    }

    // 스레드 클래스 생성자 playTime, isPlaying  (생성자는 클래스를 객체화하여 클래스의 매개변수로 사용하는 것.)
    inner class Player(private val playTime: Int, var isPlaying: Boolean) : Thread() {
        private var second = 0

        override fun run() {
            try {
                while (true) {
                    if (second >= playTime) {

                        break
                    }

                    if (isPlaying) {
                        sleep(1000)     // sleep은 Thread에서 쓸수있는 메소드
                        second++                // 1초마다 second가 1씩 더해짐.

                        // 여기서 그냥 binding을 쓰면 중첩 클래스가 됨. 내부클래스로 쓰려면 부모인 Player 클래스에 inner 써야해
                        // WorkThread(메인 스레드가 아닌 스레드) 에서는 뷰 랜더링을 할 수 없다.
                        //binding.songStartTimeTv.text = String.format("%02d:%02d",second/60, second%60)
                        runOnUiThread {
                            binding.songPlayerSb.progress = second * 1000 / playTime
                            binding.songStartTimeTv.text =
                                String.format("%02d:%02d", second / 60, second % 60)
                        }
                    }

                }
            } catch (e: InterruptedException) {
                Log.d("interrupt", "쓰레드가 종료되었습니다.")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()        // 미디어 플레이어 중지
        player.isPlaying = false    // 스레드 재생 중지
        song.isPlaying = false
        song.second = (binding.songPlayerSb.progress * song.playTime)/1000
        setPlayerStatus(false)      // 정지 상태일 때의 이미지로 전환.

        //getSharedPreferences : 간단한 설정값들을 저장해줄 때 유용하다. (서버에 넣어주기는 작은 데이터들)
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() // SharedPreference 는 바로 편집할 수 없어서 editor를 따로 선언해줘야
        /*
        editor.putString("title", song.title)
        editor.putString("title", song.music)
            ... 이렇게 하나하나 넣기는 데이터 양이 많을 때 번거롭다. ->> Json 이용하자!
        */

        // Gson 객체를 Json으로 바꾸어주고 Json을 객체로 바꾸어주는 중간다리 역할의 기능 라이브러리에 추가해야되
        val json = gson.toJson(song)
        editor.putString("song",json)

        editor.apply()      // Git 에서 put을 하고 apply를 하는 매커니즘과 비슷하다.
    }

    override fun onDestroy() {
        super.onDestroy()
        player.interrupt()
        mediaPlayer?.release()      // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null          // 미디어 플레이어 해제

    }
}



