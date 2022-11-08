package it.rortos.aztecroll.di.repository

import it.rortos.aztecroll.data.Aztec

interface DatabaseRepInt {

    fun insertUser(aztec: Aztec)

    fun getUser(): Aztec?
}