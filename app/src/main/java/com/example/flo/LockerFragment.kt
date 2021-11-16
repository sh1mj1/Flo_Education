package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    private val information = arrayListOf("저장한 곡", "음악파일")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerViewpagerAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter
        TabLayoutMediator(binding.lockerContentTb,binding.lockerContentVp){
            tab,position ->
            tab.text = information[position]
        }.attach()


//        lockerSongDatas.apply {
//
//            add(Locker_song("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
//            add(Locker_song("라일락", "아이우 (IU)", R.drawable.img_album_exp2))
//            add(Locker_song("원헤", "Tuesday Club", R.drawable.btn_player_random_on_light))
//            add(Locker_song("Burn", "Tuesday Club", R.drawable.btn_playlist_select_on))
//            add(Locker_song("SlUG", "Tuesday Club", R.drawable.ic_main_twitter_btn))
//            add(Locker_song("Bottom", "Tuesday Club", R.drawable.ic_bottom_my_no_select))
//            add(Locker_song("FridayShop", "Tuesday Club", R.drawable.btn_panel_play_large))
//            add(Locker_song("미로", "Tuesday Club", R.drawable.btn_nugu))
//            add(Locker_song("미로", "Tuesday Club", R.drawable.btn_nugu))
//            add(Locker_song("미로", "Tuesday Club", R.drawable.btn_nugu))
//            add(Locker_song("미로", "Tuesday Club", R.drawable.btn_nugu))
//            add(Locker_song("미로", "Tuesday Club", R.drawable.btn_nugu))
//            add(Locker_song("미로", "Tuesday Club", R.drawable.btn_nugu))
////        }
//
//        // 어댑터 설정
//        val lockerRvAdapter = LockerRvAdapter(lockerSongDatas)
//        // 리사이클러뷰에 어댑터를 연결
//        binding.lockerSongsRv.adapter = lockerRvAdapter
//
//        lockerRvAdapter.setMyItemMoreClickListener(object : LockerRvAdapter.MyItemClickListener {
//            override fun onRemoveSong(position: Int) {
//                lockerRvAdapter.removeItem(position)
//            }
//        })
//
//
//        // 레이아웃 매니저 설정  어댑터의 현재 내용을 넣고, 수직 Linear로 넣겠다!
//        binding.lockerSongsRv.layoutManager =
//            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//



        return binding.root


    }


}