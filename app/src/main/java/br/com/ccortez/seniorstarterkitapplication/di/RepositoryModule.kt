package br.com.ccortez.seniorstarterkitapplication.di

import br.com.ccortez.seniorstarterkitapplication.data.repository.CountriesRepositoryImpl
import br.com.ccortez.seniorstarterkitapplication.domain.repository.CountriesRepository
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
    fun provideCountriesRepository(
        impl: CountriesRepositoryImpl,
    ): CountriesRepository = impl
}
