package ivan.gorbunov.lct2024.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ivan.gorbunov.lct2024.LctDataStore
import ivan.gorbunov.lct2024.gate.connection.ApiService
import ivan.gorbunov.lct2024.gate.connection.ChatWebSocket
import ivan.gorbunov.lct2024.gate.connection.Gate
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideDataStoreRepository(
        @ApplicationContext app: Context
    ): LctDataStore = LctDataStore(app)

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }


    @Singleton
    @Provides
    fun provideGate(client: OkHttpClient, @ApplicationContext app: Context): Gate {
        return Gate(client, "http://176.123.166.61:5000/", app)
    }

    @Singleton
    @Provides
    fun provideApiService(gate: Gate): ApiService {
        return ApiService(gate)
    }

    @Provides
    @Singleton
    fun provideChatWebSocket(client: OkHttpClient): ChatWebSocket {
        return ChatWebSocket(client, "ws://176.123.166.61:5000")
    }

//
//    @Singleton
//    @Provides
//    fun provideGate(): LctGate = LctGate()

}