package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding

class AlbumRvAdapter (private val albumList : ArrayList<Album>) : RecyclerView.Adapter<AlbumRvAdapter.ViewHolder>(){

    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onItemClick(album: Album)
//        fun addItem(position: Int)
    }

    // 외부의 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener : MyItemClickListener
    // 외부의 리스너 객체를 전달받는 함수
    fun setMyItemClickListener(itemClickListener : MyItemClickListener){
        mItemClickListener = itemClickListener
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRvAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    // 보통 리사이클러뷰에 표시될 아이템을 추가, 삭제, 수정할 때는 Adapter에서 보통 관리를 한다.
//    fun addItem(album: Album){
//        albumList.add(album)
//        notifyDataSetChanged()  // 아이템이 바뀌었다는것을 알려주어야 해
//    }
//    fun removeItem(position: Int){
//        albumList.removeAt(position)
//        notifyDataSetChanged()  // 아이템이 바뀌었다는것을 알려주어야 해
//    }

    // 뷰 홀더에 데이터를 바인딩해야 할 때마다 호출되는 함수 => 엄청 많이 호출될 것이다.
    override fun onBindViewHolder(holder: AlbumRvAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(albumList[position])
        }
//        holder.binding.itemAlbumCoverTitleTv.setOnClickListener{mItemClickListener.onRemoveAlbum(position)}
    }
    // 데이터 set 크기를 알려주는 함수 => 리사이클러뷰가 마지막이 언제인지를 알게 된다.
    override fun getItemCount(): Int {
        return albumList.size
    }


    // ViewHolder 만들기
    inner class ViewHolder(val binding : ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(album: Album){
            binding.itemAlbumCoverTitleTv.text = album.title
            binding.itemAlbumCoverSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }
    }


}