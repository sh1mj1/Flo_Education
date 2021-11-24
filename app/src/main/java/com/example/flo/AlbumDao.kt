package com.example.flo

import androidx.room.*

@Dao
interface AlbumDao {
    @Insert
    fun insert(album: Album)

    @Update
    fun update(album: Album)

    @Delete
    fun delete(album: Album)

    @Query("SELECT * FROM AlbumTable") // 테이블의 모든 값을 가져와라
    fun getAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    fun getAlbum(id: Int): Album

    @Insert
    fun likeAlbum(like: Like)

    // 사용자가 '좋아요'한 userId와 albumId가 일치하는 것이 있으면 id 값을 가져오기
    @Query("SELECT id FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun isLikeAlbum(userId: Int, albumId: Int): Int?

    // 일치하는 것이 있으면 삭제하기
    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun disLikeAlbum(userId: Int, albumId: Int)

    // as 는 LikeTable을 LT라고 부르겠다
    // 왼쪽에 있는 LikeTable을 기준으로 AlbumTable과 userid와 albumid 값이 같으면 가져온다.
    @Query("SELECT AT.* FROM LikeTable as LT LEFT JOIN AlbumTable as AT ON LT.albumId = AT.id WHERE LT.userId = :userId")
    fun getLikedAlbums(userId: Int): List<Album>

    @Query("UPDATE LikeTable SET userId = :isLike WHERE userid = :id")
    fun updateIsLikeById(isLike :Boolean,id: Int)

}