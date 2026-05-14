package br.com.ccortez.seniorstarterkitapplication.di

import br.com.ccortez.seniorstarterkitapplication.data.repository.HackerNewsRepositoryImpl
import br.com.ccortez.seniorstarterkitapplication.domain.repository.HackerNewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideHackerNewsRepository(
        impl: HackerNewsRepositoryImpl,
    ): HackerNewsRepository = impl
}
