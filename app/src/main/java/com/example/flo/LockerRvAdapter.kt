package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemSongsInLockerBinding

class LockerRvAdapter(private val lockerSongsList: ArrayList<Locker_song>) :
    RecyclerView.Adapter<LockerRvAdapter.ViewHolder>() {

    // 클릭 인터페이스 정의
    interface MyItemClickListener {
//        fun onItemClick(position: Int)

        fun onRemoveSong(position: Int)
    }

    // 리스너 객체를 저정할 변수, 전달받는 함수
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemMoreClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    fun removeItem(position: Int) {
        lockerSongsList.removeAt(position)
        notifyDataSetChanged()  // 아이템이 바뀌었다는것을 알려주어야 해
    }

    // 뷰 홀더를 생성해줘야 할 때 호출되는 함수 => 아이템 뷰 객체를 만들어서 뷰 홀더에 던져줌.
    // 리스트뷰에서는 뷰를 재활용하지 않지만 리사이클러뷰는 재활용하기 때문에 ViewHolder 가 필요하다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LockerRvAdapter.ViewHolder {
        val binding: ItemSongsInLockerBinding =
            ItemSongsInLockerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    // 뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출됨
    override fun onBindViewHolder(holder: LockerRvAdapter.ViewHolder, position: Int) {
        holder.bind(lockerSongsList[position])
//        holder.itemView.setOnClickListener { mItemMoreMoreClickListener.onItemRemoveClick(lockerSongsList[position]) }
        // More버튼 누르면 삭제되도록 만들기
        holder.binding.lockerSongMoreBtn.setOnClickListener {
            mItemClickListener.onRemoveSong(position)
        }
    }

    // 데이터 세트 크기를 알려주는 함수 => 리사이클러뷰가 마지막이 언제인지를 알게 된다.
    override fun getItemCount(): Int {
        return lockerSongsList.size
    }

    // 뷰홀더 만들기
    inner class ViewHolder(val binding: ItemSongsInLockerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(songs: Locker_song) {

            binding.itemLockerSongTitleTv.text = songs.title
            binding.itemLockerSongSingerTv.text = songs.singer
            binding.itemLockerCoverImgIv.setImageResource(songs.coverImg!!)

        }
    }

}


