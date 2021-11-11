package com.example.flo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo.databinding.FragmentMediaBinding


class MediaFragment : Fragment() {
    lateinit var binding : FragmentMediaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }


}