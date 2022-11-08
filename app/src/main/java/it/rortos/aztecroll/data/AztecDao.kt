package it.rortos.aztecroll.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AztecDao {
    @Query("SELECT * FROM aztec LIMIT 1")
    fun getUser(): Aztec?

    @Insert
    fun insertUser(aztecEntity: Aztec)
}
