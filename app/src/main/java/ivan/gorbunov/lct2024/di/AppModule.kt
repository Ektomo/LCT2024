package ivan.gorbunov.lct2024.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ivan.gorbunov.lct2024.LctDataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideDataStoreRepository(
        @ApplicationContext app: Context
    ): LctDataStore = LctDataStore(app)

//
//    @Singleton
//    @Provides
//    fun provideGate(): LctGate = LctGate()

}