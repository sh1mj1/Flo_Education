package com.example.flo

import androidx.room.*

//Database Access Object : 데이터에 접근할 수 있는 메서드를 정의해 놓은 인터페이스
// 테이블인 Entity Song에 쿼리로 접근하는 인터페이스를 만들어주는 부분
// 간단히 어노테이션으로만 인서트, 딜리트, 쿼리를 할 수 있다.
@Dao
interface SongDao {
    @Insert
    fun insert(song:Song)

    @Update
    fun update(song:Song)

    @Delete
    fun delete(song:Song)

    @Query("SELECT * FROM SongTable")                        // 테이블의 모든 값을 가져와라
    fun getAllSongs(): List<Song>                                     // 리스트에 반환

    @Query("SELECT * FROM SongTable WHERE id = :id")         // id에 맞는 테이블의 데이터 가져오기기
    fun getSong(id : Int) : Song

    @Query("UPDATE SongTable SET isLike= :isLike WHERE id = :id")
    fun updateIsLikeById(isLike :Boolean,id: Int)

    @Query("SELECT * FROM SongTable WHERE isLike = :isLike")
    fun getLikedSongs(isLike: Boolean):List<Song>

    @Query("SELECT * FROM SongTable WHERE albumidx = :albumIdx")
    fun getSongsInAlbum(albumIdx: Int) : List<Song>
}