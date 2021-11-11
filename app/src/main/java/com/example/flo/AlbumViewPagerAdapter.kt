package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {        // case 문법과 비슷.
            0 -> SongFragment()
            1 -> DetailFragment()
            else -> MediaFragment() // 어떤 앨범에서는 영상 부분이 없을 수도 있다.
        }
    }
}