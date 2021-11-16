package com.example.flo

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.flo.databinding.SnackbarLikeCustomBinding
import com.google.android.material.snackbar.Snackbar

class CustomSnackBar (view: View, private val message: String){

    companion object {

        fun putInLikeSongs(view:View, message:String) = CustomSnackBar(view, message)
    }

    private val context = view.context
    private val snackbar = Snackbar.make(view,"",5000)
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    private val inflater = LayoutInflater.from(context)
    private val snackbarBinding: SnackbarLikeCustomBinding = DataBindingUtil.inflate(inflater, R.layout.snackbar_like_custom, null, false)

    init {
        initView()
        initData()
    }

    private fun initView() {
        with(snackbarLayout) {
            removeAllViews()
            setPadding(0, 0, 0, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackbarBinding.root, 0)
        }
    }

    private fun initData() {
        snackbarBinding.putLikesTv.text = message
//        snackbarBinding.btnSample.setOnClickListener {
//            // 버튼 넣으려면 넣고 그에대한 이벤트 넣으면 될듯
//        }
    }

    fun show() {
        snackbar.show()
    }
}

