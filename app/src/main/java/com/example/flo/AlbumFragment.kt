package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding
    private var gson: Gson = Gson()

    // 탭 레이아웃에 들어갈 List
    val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        // HomeFragment 에서 넘어온 데이터 받아오기
        val albumData = arguments?.getString("album")
        val album = gson.fromJson(albumData, Album::class.java)
        // Home 에서 넘어온 데이터를 반영
        setInit(album)

        binding.albumBtnBackIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

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
    }

}
