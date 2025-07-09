package com.example.notificationmanager

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ApplicationViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Look for a constructor that takes an Application as the only parameter
        return try {
            modelClass.getConstructor(Application::class.java).newInstance(application)
        } catch (e: NoSuchMethodException) {
            throw IllegalArgumentException("ViewModel class ${modelClass.name} must have a constructor with Application parameter", e)
        }
    }
}
