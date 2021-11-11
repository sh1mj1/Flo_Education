package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentDetailBinding
import com.example.flo.databinding.FragmentSongBinding

class SongFragment: Fragment() {
    lateinit var binding : FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)





//        //토스트메세지
//        binding.albumBtnSongPlayIv.setOnClickListener {
//            val tMessage = Toast.makeText(requireActivity(), "한곡이 재생목록에 담겼습니다\n중복곡은 제외됩니다", Toast.LENGTH_SHORT)
//            tMessage.show()
//        }

        return binding.root

    }


}
