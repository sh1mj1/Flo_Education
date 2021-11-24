package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding
    private var gson: Gson = Gson()

    // 탭 레이아웃에 들어갈 List
    val information = arrayListOf("수록곡", "상세정보", "영상")
    // Song이 좋아요가 되어있는지 아닌지를 Boolean으로 갖는 변수
    private var isLiked:Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        // HomeFragment 에서 넘어온 데이터 받아오기
        val albumData = arguments?.getString("album")
        val album = gson.fromJson(albumData, Album::class.java)

        // HomeFrag에서 넘어온 데이터를 반영
        setInit(album)
        setClickListeners(album)

        //ROOM_DB
        val songs = getSongs(album.id) //앨범안에 있는 수록곡들을 불러옵니다.
        // 이 다음에 수록곡 프래그먼트에 songs을 전달해주는 식으로 사용하자

        // 뒤로가기 버튼
        binding.albumBtnBackIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        // 뷰페이저 (수록곡, 상세정보, 영상)
        val albumAdapter = AlbumViewPagerAdapter(this)

        binding.albumAlbumInfoVp.adapter = albumAdapter

        //그리고 TalLayout을 ViewPager에 연결해주기, 탭에 텍스트 지정해주기
        TabLayoutMediator(binding.albumInfoTl, binding.albumAlbumInfoVp){
            tab, position ->
            tab.text = information[position]
        }.attach()


        return binding.root


    }

    // AlbumFragment의 앨범커버사진, 앨범이름, 가수 설정.
    private fun setInit(album: Album) {
        binding.albumCoverIv.setImageResource(album.coverImg!!)
        binding.albumTitleTv.text = album.title.toString()
        binding.albumSingerTv.text = album.singer.toString()

        if (isLiked){
            binding.albumBtnLikeOffIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.albumBtnLikeOffIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }
    // 앨범 좋아요
    private fun setClickListeners(album : Album){
        val userId : Int = getJwt()

        binding.albumBtnLikeOffIv.setOnClickListener {
            if(isLiked){
                binding.albumBtnLikeOffIv.setImageResource(R.drawable.ic_my_like_off)
                disLikeAlbum(userId, album.id)
            }else{
                binding.albumBtnLikeOffIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
            }
        }
    }
    // 사용자가 좋아요를 누른 앨범이 있으면 그 앨범의 데이터
    private fun likeAlbum(userId:Int, albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum(like)

    }
    private fun isLikeAlbum(albumId:Int):Boolean{
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        val likeId:Int? = songDB.albumDao().isLikeAlbum(userId, albumId)

        return likeId != null
    }

    private fun disLikeAlbum(userId: Int,albumId:Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        songDB.albumDao().disLikeAlbum(userId, albumId)


    }

    // jwt를 사용하여 userid 값을 가져오기
    private fun getJwt():Int{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

        return spf!!.getInt("jwt",0)
    }


    //ROOM_DB
    private fun getSongs(albumIdx: Int): ArrayList<Song>{
        val songDB = SongDatabase.getInstance(requireContext())!!

        val songs = songDB.songDao().getSongsInAlbum(albumIdx) as ArrayList

        return songs
    }

}
