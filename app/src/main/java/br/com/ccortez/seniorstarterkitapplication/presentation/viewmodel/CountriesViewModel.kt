package br.com.ccortez.seniorstarterkitapplication.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.ccortez.seniorstarterkitapplication.domain.model.Country
import br.com.ccortez.seniorstarterkitapplication.domain.model.Resource
import br.com.ccortez.seniorstarterkitapplication.domain.usecase.GetCountriesUseCase
import br.com.ccortez.seniorstarterkitapplication.domain.usecase.NoParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val getCountries: GetCountriesUseCase,
) : BaseViewModel<List<Country>>() {

    init {
        loadCountries()
    }

    fun reload() {
        loadCountries()
    }

    private fun loadCountries() {
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            _uiState.value = getCountries(NoParams)
        }
    }
}
