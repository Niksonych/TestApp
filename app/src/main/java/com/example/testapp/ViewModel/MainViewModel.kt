package com.example.testapp.ViewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class MainViewModel: ViewModel() {
    private val remoteConfig = Firebase.remoteConfig

    fun fetchRemoteConfig(onSuccess: (Boolean, String) -> Unit, onError: () -> Unit) {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.setDefaultsAsync(
            mapOf(
                "game_pass" to false,
                "web_link" to "https://www.tinypng.com"
            )
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val gamePass = remoteConfig.getBoolean("game_pass")
                if (gamePass) {
                    onSuccess(true, "")
                } else {
                    val webLink = remoteConfig.getString("web_link")
                    onSuccess(false, webLink)
                }
            } else {
                onError()
            }
        }
    }
}
