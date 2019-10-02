package mohammadhendy.githubrepos.dependency_injection.modules

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides

@Module
class AndroidModule(private val context: Context) {

    @Provides
    fun providesContext() : Context = context

    @Provides
    fun providesResources() : Resources = context.resources
}