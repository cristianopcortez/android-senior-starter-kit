package br.com.ccortez.seniorstarterkitapplication.domain.usecase

import br.com.ccortez.seniorstarterkitapplication.domain.model.Resource

/**
 * Contrato base para casos de uso (uma ação por use case).
 * [P] = parâmetros de entrada, [R] = tipo de retorno.
 *
 * Para use cases sem parâmetros, use [NoParams]:
 * ```
 * class GetItemsUseCase(private val repository: ItemRepository) : UseCase<NoParams, List<Item>> {
 *     override suspend fun invoke(params: NoParams): Resource<List<Item>> = repository.getItems()
 * }
 * ```
 *
 * No ViewModel:
 * ```
 * viewModelScope.launch {
 *     _uiState.value = Resource.Loading
 *     _uiState.value = getItemsUseCase(NoParams)
 * }
 * ```
 */
interface UseCase<in P, out R> {

    suspend operator fun invoke(params: P): Resource<R>
}

/**
 * Use em use cases que não precisam de parâmetros.
 * Ex.: GetItemsUseCase(invoke(NoParams)), RefreshUseCase(invoke(NoParams))
 */
object NoParams
