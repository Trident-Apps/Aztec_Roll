package it.rortos.aztecroll.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Aztec(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val savedUrl: String? = null,
    val isAppsStarted: Boolean
)