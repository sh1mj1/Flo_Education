package com.example.flo

data class Album(

    var title: String? = "",
    var singer: String? = "",
    var coverImg:Int? = null,
    var tracks :ArrayList<Song>? = null

)