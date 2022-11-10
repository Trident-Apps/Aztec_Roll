package it.rortos.realflig.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Aztec::class], version = 1)
abstract class AztecDatabase : RoomDatabase() {

    abstract val dao: AztecDao
}