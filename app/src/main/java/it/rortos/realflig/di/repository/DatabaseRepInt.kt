package it.rortos.realflig.di.repository

import it.rortos.realflig.data.Aztec

interface DatabaseRepInt {

    fun insertUser(aztec: Aztec)

    fun getUser(): Aztec?
}