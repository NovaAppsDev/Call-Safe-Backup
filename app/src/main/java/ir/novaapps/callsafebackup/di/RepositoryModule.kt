package ir.novaapps.callsafebackup.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.novaapps.callsafebackup.data.domain.repository.CallLogRepository
import ir.novaapps.callsafebackup.data.domain.repository.ContactRepository
import ir.novaapps.callsafebackup.data.repository.CallLogRepositoryImpl
import ir.novaapps.callsafebackup.data.repository.ContactRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun providerContactRepository() : ContactRepository{
        return ContactRepositoryImpl()
    }

    @Provides
    fun providerCallLogRepository():CallLogRepository{
        return CallLogRepositoryImpl()
    }
}