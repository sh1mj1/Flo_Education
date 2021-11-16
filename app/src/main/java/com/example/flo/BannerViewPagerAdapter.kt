package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

//FragmentStateAdapter(fregment) 를 상속받기
class BannerViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

// ctrl + O -> override 함수 불러오기

    // fragment를 담아서 보내줄 list 선언. private은 이 클래스 내에서만 사용할 수 있다는 뜻.
    private val fragmentlist: ArrayList<Fragment> = ArrayList()

    // 전달할 item의 갯수는 fragmentlist의 크기만큼.
    override fun getItemCount(): Int = fragmentlist.size

    override fun createFragment(position: Int): Fragment = fragmentlist[position]

    fun addFragment(fragment: Fragment) {
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size - 1)

        // Notify any registered observers that the item reflected at position has been newly inserted.
        // 이전 문장에서 fragment를 추가했으니 추가된 위치(position)은 fragmentlist.size-1

        // 0 1 2 와 같이 3개의 프래그먼트가 list에 있는데 외부에서 0 1 2 3 포지션으로는 3 인덱스에 있음.
        // 이걸 어댑터에게 새로 추가된 아이템이 있으니 다시 viewpager에게 알려주어라라는 것으로
        // 추가된 곳의 인덱스를 인자로 넘겨 notifyItemInserted를 호출한다.

    }
}