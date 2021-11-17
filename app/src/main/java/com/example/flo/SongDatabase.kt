package com.example.flo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Room을 실질적으로 구현하는 부분

@Database(entities =  [Song::class, Album::class], version = 1)      // version = 1 : 스키마를 추출하지 않음. 버전은 Song을 변경할 때 migration 할 수 있는 기준이 됨.
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao                     // 미리 만들어놓은 SongDao에 접근할 수 있도록 abstract fun을 이용해서 contactsDao()를 만들어
    abstract fun albumDao() : AlbumDao

    companion object {                                  // 어디서든 접근 가능하고 중복 생성되지 않게 싱글톤으로 companion object
        private var instance:SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context) : SongDatabase? {
            if (instance == null) {
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"                   // 이 데이터베이스의 이름임. 다른 데이터베이스랑 이름이 겹치면 꼬인다.
                    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()          // UI Thread (MainThread)에서 접근 할 수 있도록 allowMainThreadQueries
                }                                               // 오래 걸리는 작업은 보통 WorkThread 에서 하지만 일단 이렇게 ㄱ
            }
            return instance
        }

    }

}