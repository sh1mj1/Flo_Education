package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var albumDatas  = ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
//        binding.homeAlbumNew1Ll.setOnClickListener {
//            (context as MainActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.main_frm, AlbumFragment())
//                .commitAllowingStateLoss()
//        }

    // 데이터 리스트 만들기 더미데이터를 추가해 줬다.


    albumDatas.apply {
        add(Album("Butter", "방탄소년단(BTS)", R.drawable.img_album_exp))
        add(Album("LILAC", "아이유(IU)", R.drawable.img_album_exp2))
        add(Album("Next Level", "에스파", R.drawable.ic_my_like_off))
        add(Album("Burn", "Tuesday Club", R.drawable.ic_my_like_on))
        add(Album("원해", "Tuesday Club", R.drawable.main_btm_home_selector))
        add(Album("망상", "Tuesday Club", R.drawable.btn_playlist_select_off))
        }




        // 어댑터 객체 설언
        val albumRVAdapter = AlbumRvAdapter(albumDatas)
        // 리사이클러뷰랑 어댑터 객체 연결
        binding.homeTodayMusicRv.adapter = albumRVAdapter

        albumRVAdapter.setMyItemClickListener(object : AlbumRvAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)

//                (context as AlbumFragment).childFragmentManager.beginTransaction()
//                    .replace(R.id.album_info_tracks_rv, SongFragment().apply {
//                        arguments = Bundle().apply {
//                            val gson = Gson()
//                            val trackJson = gson.toJson(album)
//                            putString("album",trackJson)
//                        }
//                    }).commitAllowingStateLoss()


            }
//            override fun onRemoveAlbum(position: Int) {
//                albumRVAdapter.removeItem(position)
//            }
            // 등 등


        })


        // 레이아웃 매니저 설정

        binding.homeTodayMusicRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)

        val bannerAdapter = BannerViewPagerAdapter(this)

        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        // ViewPager와 Fragment를 연결해주는 작업.
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return binding.root
    }

    // 앨범 클릭 -> Home_frag 에서 Album_frag 로 이동할 때 Album_Frag 바꾸기
    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }


}