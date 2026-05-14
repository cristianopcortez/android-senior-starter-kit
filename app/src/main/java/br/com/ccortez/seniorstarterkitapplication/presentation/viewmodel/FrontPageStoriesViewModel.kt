package br.com.ccortez.seniorstarterkitapplication.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.ccortez.seniorstarterkitapplication.domain.model.Resource
import br.com.ccortez.seniorstarterkitapplication.domain.model.Story
import br.com.ccortez.seniorstarterkitapplication.domain.usecase.FrontPageStoriesParams
import br.com.ccortez.seniorstarterkitapplication.domain.usecase.GetFrontPageStoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FrontPageStoriesViewModel @Inject constructor(
    private val getFrontPageStories: GetFrontPageStoriesUseCase,
) : BaseViewModel<List<Story>>() {

    init {
        loadFrontPage()
    }

    fun reload() {
        loadFrontPage()
    }

    private fun loadFrontPage() {
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            _uiState.value = getFrontPageStories(
                FrontPageStoriesParams(hitsPerPage = FrontPageStoriesParams.DEFAULT_HITS_PER_PAGE),
            )
        }
    }
}
