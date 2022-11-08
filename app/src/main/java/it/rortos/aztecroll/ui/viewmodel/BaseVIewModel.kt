package it.rortos.aztecroll.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import it.rortos.aztecroll.AztecApp
import javax.inject.Inject

@HiltViewModel
open class BaseVIewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    protected val context get() = getApplication<AztecApp>()
}