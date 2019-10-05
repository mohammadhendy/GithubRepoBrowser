package mohammadhendy.githubrepos

import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TestRules {

    /**
     * Replaces the RxJava [Schedulers] with the specified scheduler or by default [Schedulers.trampoline]
     */
    class ReplaceSchedulers @JvmOverloads constructor(
        private val main: Scheduler = Schedulers.trampoline(),
        private val io: Scheduler = Schedulers.trampoline(),
        private val computation: Scheduler = Schedulers.trampoline()
    ) : TestRule {

        override fun apply(base: Statement, description: Description): Statement {
            return object : Statement() {
                @Throws(Throwable::class)
                override fun evaluate() {
                    RxAndroidPlugins.setMainThreadSchedulerHandler { main }
                    RxJavaPlugins.setIoSchedulerHandler { io }
                    RxJavaPlugins.setComputationSchedulerHandler { computation }
                    try {
                        base.evaluate()
                    } finally {
                        RxJavaPlugins.reset()
                    }
                }
            }
        }
    }
}