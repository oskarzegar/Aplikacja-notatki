package com.example.aplikacjanotatki.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aplikacjanotatki.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase: RoomDatabase(){
    abstract fun getNoteDao(): NoteDao

    companion object{
        @Volatile
        private var instance: NoteDatabase? = null
        private val LOCK = Any()

        operator  fun invoke(context: Context) = instance ?:
        synchronized(LOCK){
            instance ?:
            createDataBase(context).also{
                instance = it
            }
        }
        private fun createDataBase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "NoteDatabase",
            ).build()
    }
}
