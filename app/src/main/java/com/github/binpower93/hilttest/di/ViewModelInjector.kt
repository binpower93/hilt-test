package com.github.binpower93.hilttest.di

import com.github.binpower93.hilttest.ui.newnote.NewNoteViewModel
import com.github.binpower93.hilttest.ui.posts.PostsViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ViewModelInjector {
    /**
     * Inject required dependencies into the PostsViewModel
     * @param postsViewModel the instance of the PostsViewModel in which to inject the dependencies
     */
    fun inject(postsViewModel: PostsViewModel)

    /**
     * Inject required dependencies into the NewNoteViewModel
     * @param newNoteViewModel the instance of the NewNoteViewModel in which to inject the dependencies
     */
    fun inject(newNoteViewModel: NewNoteViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}