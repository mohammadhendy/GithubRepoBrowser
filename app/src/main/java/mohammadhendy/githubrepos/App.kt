package mohammadhendy.githubrepos

import android.app.Application
import mohammadhendy.githubrepos.dependency_injection.DaggerInjector
import mohammadhendy.githubrepos.dependency_injection.modules.RestApiModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DaggerInjector
            .get()
            .createDependencyInjectionRoot(this)
            .inject(this)
    }
}