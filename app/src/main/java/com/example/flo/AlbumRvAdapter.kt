package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding

class AlbumRvAdapter (private val albumList : ArrayList<Album>) : RecyclerView.Adapter<AlbumRvAdapter.ViewHolder>(){

    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position: Int)

    }

    // 외부의 리스너 객체를 저장할 변수 (클릭 리스너 선언)
    private lateinit var mItemClickListener : MyItemClickListener

    // 외부의 리스너 객체를 전달받는 함수 (클릭 리스너 등록 메소드 : 메인 액티비티에서 inner Class로 호출)
    fun setMyItemClickListener(itemClickListener : MyItemClickListener){
        mItemClickListener = itemClickListener
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AlbumRvAdapter.ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }


    // 뷰홀더에 Data를 binding 위아래로 스크롤할 때 마다 엄청나게 호출
    // 뷰홀더가 매개변수로 들어와서 자식 뷰에 접근 가능 -> 데이터 바인딩
    override fun onBindViewHolder(holder: AlbumRvAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(albumList[position])
        }
//        holder.binding.itemAlbumCoverTitleTv.setOnClickListener{mItemClickListener.onRemoveAlbum(position)}
    }

    fun addItems(albums: ArrayList<Album>) {
        albumList.clear()
        albumList.addAll(albums)
        notifyDataSetChanged()
    }

    fun addItem(album: Album) {
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItems() {
        albumList.clear()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        albumList.removeAt(position)
        notifyDataSetChanged()
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