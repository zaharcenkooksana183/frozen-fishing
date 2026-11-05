package com.Tonkapa7.Piwpul

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.Tonkapa7.Piwpul.core.di.ServiceLocator
import com.Tonkapa7.Piwpul.core.theme.AppTheme
import com.Tonkapa7.Piwpul.presentation.app.AppRoot

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        ServiceLocator.init(applicationContext)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                AppRoot()
            }
        }
    }
}
