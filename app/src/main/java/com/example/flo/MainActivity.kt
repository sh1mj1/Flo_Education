package com.example.flo

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.SongActivity.Player
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var song: Song

    // 미디어 플레이어
    private var mediaPlayer: MediaPlayer? = null
    lateinit var player: Player

    // GSON 객체 선언
    private var gson: Gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("MainActivity","onCreate()")
        initNavigation()
        inputDummyAlbums()
        inputDummySongs()

        binding.mainPlayerLayout.setOnClickListener {
            Log.d("nowSongId", song.id.toString())
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit() // MODE_PRIVATE 은 이 앱에서만 SharedPreference에 접근한다는 뜻
            editor.putInt("songId", song.id)        // SharedPreferences의 데이터를 songId로 바꾸고 SongActivity 실행
            editor.apply()

            val intent = Intent(this@MainActivity, SongActivity::class.java)
            startActivity(intent)
        }

//        binding.mainMiniplayerBtn.setOnClickListener {
//            val intent = Intent(this, SongActivity::class.java)
//            intent.putExtra("isPlaying", true)
//            setMiniPlayerStatus(true)
//            setMiniPlayer(song)
////            startActivity(intent)
//        }
//        binding.mainPauseBtn.setOnClickListener {
//            val intent = Intent(this, SongActivity::class.java)
//            intent.putExtra("isPlaying", false)
//            setMiniPlayerStatus(false)
//            setMiniPlayer(song)
////            startActivity(intent)
//        }


    }

    override fun onStart() {
        super.onStart()

        val spf = getSharedPreferences("song", MODE_PRIVATE)              // spf라는 SharedPreferences 객체 생성
        Log.d("MainActivity_onstart()", spf.toString())
        val songId = spf.getInt("songId", 0)                                    // SharedPreferences 로 송id (Primary Key) 전달 받기
                                                                                // getInt의 0은 저장된 값이 없을 떄 기본값으로 가져온다는 뜻
        val songDB = SongDatabase.getInstance(this)!!
        song = if (songId == 0){                                                // songId가 0이면(제일 처음 실행 시) id가 1인 song으로.
            songDB.songDao().getSong(1)
        }else{
            songDB.songDao().getSong(songId)
        }

        Log.d("MainActivity_song ID", song.id.toString())
        Log.d("songData", song.toString())
        setMiniPlayer(song)





    }


    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

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

    private fun setMiniPlayer(song: Song) {

//        Log.d("song이 연동됬는지 확인하기 songsecond "," (song.second*1000 / song.playTime).toString():)
        Log.d("연동됬는지 확인song.second", (song.second*1000/ song.playTime).toString())
        binding.mainMiniPlayerTitleTv.text = song.title
        binding.mainMiniPlayerSingerTv.text = song.singer
        binding.mainPlayerSb.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)

        mediaPlayer = MediaPlayer.create(this, music)

        if (song.isPlaying) {    // 재생되고있으면 일시정지 버튼이 보임
//            mediaPlayer?.start()
            binding.mainPauseBtn.visibility = View.VISIBLE
            binding.mainMiniplayerBtn.visibility = View.GONE
        } else {
//            mediaPlayer?.pause()
            binding.mainPauseBtn.visibility = View.GONE
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
        }

    }


    //ROOM_DB 에 songs dummy 데이터를 삽입

    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if (albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                1,
                "IU 5th Album 'LILAC'", "아이유 (IU)", R.drawable.img_album_exp2
            )
        )

        songDB.albumDao().insert(
            Album(
                2,
                "Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "iScreaM Vol.10 : Next Level Remixes", "에스파 (AESPA)", R.drawable.img_album_exp3
            )
        )

        songDB.albumDao().insert(
            Album(
                4,
                "MAP OF THE SOUL : PERSONA", "방탄소년단 (BTS)", R.drawable.img_album_exp4
            )
        )

        songDB.albumDao().insert(
            Album(
                5,
                "GREAT!", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5
            )
        )

    }

    //ROOM_DB 에 songs dummy 데이터를 삽입
    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getAllSongs()

        if (songs.isNotEmpty()) return  // 데이터베이스에 더미데이터가 이미 들어있다면 안넣기

        songDB.songDao().insert(        // 데이터베이스에 데이터 넣기
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,1
            )
        )
        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,1
            )
        )
        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_lilac",
                R.drawable.img_album_exp,
                false,2
            )
        )
        songDB.songDao().insert(
            Song(
                "Butter (Hotter Remix)",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_lilac",
                R.drawable.img_album_exp,
                false,2
            )
        )
        songDB.songDao().insert(
            Song(
                "Butter (Sweeter Remix)",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_lilac",
                R.drawable.img_album_exp,
                false,2
            )
        )
        songDB.songDao().insert(
            Song(
                "Next Level",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "music_lilac",
                R.drawable.img_album_exp3,
                false,3
            )
        )
        songDB.songDao().insert(
            Song(
                "Next Level (IMLAY Remix)",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "music_lilac",
                R.drawable.img_album_exp3,
                false,3
            )
        )
        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "방탄소년단 (BTS)",
                0,
                230,
                false,
                "music_lilac",
                R.drawable.img_album_exp4,
                false,4
            )
        )
        songDB.songDao().insert(
            Song(
                "소우주 (Mikrokosmos)",
                "방탄소년단 (BTS)",
                0,
                230,
                false,
                "music_lilac",
                R.drawable.img_album_exp4,
                false,4
            )
        )
        songDB.songDao().insert(
            Song(
                "Make It Right",
                "방탄소년단 (BTS)",
                0,
                230,
                false,
                "music_lilac",
                R.drawable.img_album_exp4,
                false,4
            )
        )
        songDB.songDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "music_lilac",
                R.drawable.img_album_exp5,
                false,5
            )
        )
        songDB.songDao().insert(
            Song(
                "궁금해",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "music_lilac",
                R.drawable.img_album_exp5,
                false,5
            )
        )
        val _songs = songDB.songDao().getAllSongs()
        Log.d("DB DATA", _songs.toString())
    }

//    fun setMiniPlayerStatus(isPlaying: Boolean) {
//        if (isPlaying) {
//            binding.mainMiniplayerBtn.visibility = View.GONE
//            binding.mainPauseBtn.visibility = View.VISIBLE
//            mediaPlayer?.start()
//
//        } else {
//            binding.mainMiniplayerBtn.visibility = View.VISIBLE
//            binding.mainPauseBtn.visibility = View.GONE
//            mediaPlayer?.pause()
//        }
//    }




}






