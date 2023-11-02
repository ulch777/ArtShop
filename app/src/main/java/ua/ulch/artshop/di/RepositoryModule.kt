package ua.ulch.artshop.di


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ua.ulch.artshop.data.repository.ArtShopRepository
import ua.ulch.artshop.data.repository.ArtShopRepositoryImpl


@InstallIn(ViewModelComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideArtShopRepository(repository: ArtShopRepositoryImpl): ArtShopRepository
}