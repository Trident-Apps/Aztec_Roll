package it.rortos.aztecroll.di.repository

import it.rortos.aztecroll.data.Aztec
import it.rortos.aztecroll.data.AztecDao
import javax.inject.Inject

//class DatabaseRepository @Inject constructor(private val aztecDao: AztecDao) {
//    suspend fun getAll(): Aztec? = aztecDao.getAll()
//
//    fun insertIntoDatabase(aztec: Aztec) = aztecDao.insertIntoDb(aztec)
//}

class DatabaseRepository @Inject constructor(val dao: AztecDao) : DatabaseRepInt {

    @Inject
    lateinit var aztecDao: AztecDao
    override fun insertUser(aztec: Aztec) = aztecDao.insertUser(aztec)

    override fun getUser(): Aztec? = aztecDao.getUser()


}