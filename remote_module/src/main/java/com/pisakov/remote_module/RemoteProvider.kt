package com.pisakov.remote_module

interface RemoteProvider {
    fun provideRemote(): TmdbApi
}