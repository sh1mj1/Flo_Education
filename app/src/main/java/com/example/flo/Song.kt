package com.example.flo

import androidx.room.Entity
import androidx.room.PrimaryKey

// 제목, 가수, 사진, 전체 재생시간, 현재 재생시간, isplaying(재생되고 있는지)
@Entity(tableName = "SongTable")
data class Song(
    var title: String = "",
    var singer: String = "",
    var second: Int = 0,
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    var music: String = "",
    var coverImg: Int? = null,
    var isLike : Boolean = false,
    var albumidx : Int = 0          // 이 song이 어떤 앨범에 담겨 있는지를 가리키는 변수 (foreign key(외래키) 역할)
    ){
    @PrimaryKey(autoGenerate = true) var id : Int = 0   // Song이 생길 때마다 id가 자동으로 생성되게 만들기.
}


//data class Song(
//    var title : String = "",        // 음악 제목
//    var music : String = "",        // 음악
//    var singer : String = "",       // 가수
//    var second : Int = 0,           // 현재 재생시간
//    var playTime : Int = 0,         // 전체 재생시간
//    var isPlaying : Boolean = false // 재생되고 있는지
//)
