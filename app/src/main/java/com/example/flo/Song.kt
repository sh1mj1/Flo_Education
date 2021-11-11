package com.example.flo

// 제목, 가수, 사진, 전체 재생시간, 현재 재생시간, isplaying(재생되고 있는지)
data class Song(
    var title : String = "",        // 음악 제목
    var music : String = "",        // 음악
    var singer : String = "",       // 가수
    var second : Int = 0,           // 현재 재생시간
    var playTime : Int = 0,         // 전체 재생시간
    var isPlaying : Boolean = false // 재생되고 있는지
)
