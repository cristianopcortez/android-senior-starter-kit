package br.com.ccortez.seniorstarterkitapplication.domain.repository

import br.com.ccortez.seniorstarterkitapplication.domain.model.Resource

/**
 * Contrato base para repositórios da aplicação.
 * Cada desafio define suas próprias interfaces que estendem ou seguem este padrão,
 * expondo dados como [Resource] (Loading / Success / Error).
 *
 * Exemplo de evolução no desafio:
 * ```
 * interface UserRepository : Repository {
 *     suspend fun getUser(id: String): Resource<User>
 *     fun getUsers(): Flow<Resource<List<User>>>
 * }
 * ```
 */
interface Repository
