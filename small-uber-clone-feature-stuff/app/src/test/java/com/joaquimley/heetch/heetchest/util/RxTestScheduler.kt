package com.joaquimley.heetch.heetchest.util

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RxSchedulerRule : TestRule {

    override fun apply(base: Statement, description: Description) =
        object : Statement() {
            override fun evaluate() {
                RxJavaPlugins.setInitNewThreadSchedulerHandler { TEST_SCHEDULER }
                RxJavaPlugins.setComputationSchedulerHandler { TEST_SCHEDULER }
                RxJavaPlugins.setIoSchedulerHandler { TEST_SCHEDULER }
                RxJavaPlugins.setNewThreadSchedulerHandler { TEST_SCHEDULER }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }

    companion object {
        private val TEST_SCHEDULER = Schedulers.trampoline()
    }
}