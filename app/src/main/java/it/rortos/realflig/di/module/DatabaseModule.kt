package it.rortos.realflig.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import it.rortos.realflig.data.AztecDao
import it.rortos.realflig.data.AztecDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAztecDatabase(
        @ApplicationContext app: Context
    ): AztecDatabase {
        return Room.databaseBuilder(
            app,
            AztecDatabase::
            class.java,
            "aztec_db"
        ).allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun provideAztecDao(db: AztecDatabase): AztecDao {
        return db.dao
    }
}
