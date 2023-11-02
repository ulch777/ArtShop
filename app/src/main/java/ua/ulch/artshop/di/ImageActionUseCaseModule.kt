package ua.ulch.artshop.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import ua.ulch.artshop.domain.ImageActionUseCase
import ua.ulch.artshop.domain.ImageActionUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
object ImageActionUseCaseModule {

    @Provides
    fun provideImageActionUseCase(@ApplicationContext context: Context?): ImageActionUseCase {
        return ImageActionUseCaseImpl(context)
    }
}