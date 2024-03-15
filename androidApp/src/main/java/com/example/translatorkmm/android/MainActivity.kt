package com.example.translatorkmm.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.translatorkmm.android.core.presentation.navigation.Routes
import com.example.translatorkmm.android.core.presentation.utils.rememberScreenInfo
import com.example.translatorkmm.android.core.theme.TranslatorKMMTheme
import com.example.translatorkmm.android.translate.presentation.AndroidTranslateViewModel
import com.example.translatorkmm.android.translate.presentation.TranslateScreen
import com.example.translatorkmm.translate.presentation.TranslateEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TranslatorKMMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TranslateRoot()
                }
            }
        }
    }
}

@Composable
fun TranslateRoot() {
    val screenInfo = rememberScreenInfo() // Device screen info => Screen size (width, height)
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.TRANSLATE) {
        composable(route = Routes.TRANSLATE) {
            val viewModel = hiltViewModel<AndroidTranslateViewModel>()
            val state by viewModel.state.collectAsState()
            TranslateScreen(
                state = state,
                onEvent = { event ->
                    when (event) {
                        is TranslateEvent.RecordAudio -> {
                            navController.navigate(Routes.VOICE_TO_TEXT + "/${state.fromLanguage.language.langCode}")
                        }
                        else -> viewModel.onEvent(event)
                    }
                },
                screenInfo = screenInfo
            )
        }
        // Arg ==> languageCode: Language in which user speak
        composable(
            route = Routes.VOICE_TO_TEXT + "/{languageCode}",
            arguments = listOf(
                navArgument(name = "languageCode") {
                    type = NavType.StringType
                    defaultValue = "en"
                }
            )
        ) {
            Text(text = "Voice to text")
            // TODO Implement screen
        }
    }
}
