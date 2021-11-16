package com.example.flo

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson
import java.util.concurrent.TimeUnit

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    // 미디어 플레이어 객체 생성  // ?는 null 값이 들어올 수 있다는 뜻.
    private var mediaPlayer: MediaPlayer? = null
    lateinit var player: Player

    private var songs = ArrayList<Song>()
    private var nowPos = 0
    private lateinit var songDB: SongDatabase

//    private val song: Song = Song()

    // private val handler = Handler(Looper.getMainLooper()) //Handler를 써서 WorkThread에서 뷰에 접근할 수 있지만 runOnUi를 써서 접근할 수도 있다.



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlaylist()
        initSong()
        initClickListener()

        setRepeatBtn()
        setRandomBtn()

    }

    override fun onPause() {
        super.onPause()

        songs[nowPos].second = (songs[nowPos].playTime * binding.songPlayerSb.progress) / 1000
        songs[nowPos].isPlaying = false
        setPlayerStatus(false)

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("songId", songs[nowPos].id)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.interrupt()          // 스레드 해제
        mediaPlayer?.release()      // 미디어 플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null          // 미디어 플레이어 해제
    }

    // Room_DB의 더미데이터를 모두 받아오기
    private fun initPlaylist() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getAllSongs())
    }

    private fun startTimer() {
        player = Player(songs[nowPos].playTime, songs[nowPos].isPlaying)
        player.start()
    }

    // 현재 보여지고 있는 song의 id 값을 받아와서 nowPos를 찾아줌
    private fun initSong() {

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)            // spf의 songId의 값이 없으면 기본값으로 0

        nowPos = getPlayingSongPosition(songId)
        Log.d("now Song ID", songs[nowPos].id.toString())

        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition (songId:Int) : Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i                    // 여기서 반환되는 i 가 위 initSong에서의 nowPos id 값이 됨.(위
            }
        }
        return 0
    }

    private fun setPlayer(song: Song) {
        val music = resources.getIdentifier(song.music, "raw", this.packageName)

        binding.songUpperTitleTv.text = song.title
        binding.songUpperSingerTv.text = song.singer
        binding.songStartTimeTv.text =
            String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songEndTimeTv.text =
            String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songAlbumCoverIv.setImageResource(song.coverImg!!)
        binding.songPlayerSb.progress = (song.second * 1000 / song.playTime)

        setPlayerStatus(song.isPlaying)

        if (song.isLike) {
            binding.songLike.setImageResource(R.drawable.ic_my_like_on)
        } else {
            binding.songUnlikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        mediaPlayer = MediaPlayer.create(this, music)
    }

    private fun setRandomBtn() {
        binding.songRandomOffIv.setOnClickListener {
            binding.songRandomOffIv.visibility = View.GONE
            binding.songRandomOnIv.visibility = View.VISIBLE
        }
        binding.songRandomOnIv.setOnClickListener {
            binding.songRandomOnIv.visibility = View.GONE
            binding.songRandomOffIv.visibility = View.VISIBLE
        }
    }

    private fun setRepeatBtn() {
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
    }


    private fun initClickListener() {

        binding.songBtnDownIb.setOnClickListener { finish() }
        binding.songBtnPlayIv.setOnClickListener { setPlayerStatus(true) }
        binding.songBtnPauseIv.setOnClickListener { setPlayerStatus(false) }
        binding.songBtnPreviousIv.setOnClickListener { moveSong(-1) }
        binding.songBtnNextIv.setOnClickListener { moveSong(+1) }
        binding.songLike.setOnClickListener { setLike(songs[nowPos].isLike) }

//        binding.songBtnPreviousIv.setOnClickListener {
//            if(nowPos>0){
//                nowPos--
//                player.interrupt()
//                startTimer()
//                mediaPlayer?.release()      // 미디어플레이어가 가지고 있던 리소스 해제
//                mediaPlayer = null          // 미디어 플레이어 해제
//                setPlayer(songs[nowPos])    // 변경된 nowPos로 다시 setPlayer
//            }
//        }
    }

    private fun moveSong(direct: Int) {
        if (nowPos + direct < 0) {
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size) {
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }
        nowPos += direct
        player.interrupt()              // 쓰레드 멈춰주기
        startTimer()

        mediaPlayer?.release()          // 미디어플레이어가 가지고 있던 리소스 해제
        mediaPlayer = null              // 미디어 플레이어 해제
        setPlayer(songs[nowPos])        // 변경된 nowPos로 다시 setPlayer
    }

    // songs의 현재 위치의 데이터의 Like를 반대로
    private fun setLike(isLike: Boolean) {
        songs[nowPos].isLike = !isLike                              // songs 리스트의 isLike 업데이트
        songDB.songDao().updateIsLikeById(isLike, songs[nowPos].id) // Room_DB의 isLike 업데이트

        if (isLike) {
            binding.songLike.setImageResource(R.drawable.ic_my_like_off)
        } else {
            binding.songLike.setImageResource(R.drawable.ic_my_like_on)
        }
    }

    fun setPlayerStatus(isPlaying: Boolean) {
        player.isPlaying = isPlaying
        songs[nowPos].isPlaying = isPlaying

        if (isPlaying) {
            binding.songBtnPlayIv.visibility = View.GONE
            binding.songBtnPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else {
            binding.songBtnPlayIv.visibility = View.VISIBLE
            binding.songBtnPauseIv.visibility = View.GONE
            mediaPlayer?.pause()
        }
    }

    // 스레드 클래스 생성자 playTime, isPlaying  (생성자는 클래스를 객체화하여 클래스의 매개변수로 사용하는 것.)
    inner class Player(private val playTime: Int = 0, var isPlaying: Boolean = false) : Thread() {
        private var second: Long = 0

        @SuppressLint("SetTextI18n")
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
                            binding.songPlayerSb.progress = (second * 1000 / playTime).toInt()
                            binding.songStartTimeTv.text =
                                String.format(
                                    "%02d:%02d",
                                    TimeUnit.SECONDS.toMinutes(second),
                                    second % 60
                                )
                        }
                    }

                }
            } catch (e: InterruptedException) {
                Log.d("interrupt", "쓰레드가 종료되었습니다. ${e.message}")
            }
        }
    }
}



