package br.com.ccortez.seniorstarterkitapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import br.com.ccortez.seniorstarterkitapplication.presentation.screen.CountriesRoute
import br.com.ccortez.seniorstarterkitapplication.presentation.viewmodel.CountriesViewModel
import br.com.ccortez.seniorstarterkitapplication.ui.theme.SeniorStarterKitApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val countriesViewModel: CountriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeniorStarterKitApplicationTheme {
                CountriesRoute(viewModel = countriesViewModel)
            }
        }
    }
}
