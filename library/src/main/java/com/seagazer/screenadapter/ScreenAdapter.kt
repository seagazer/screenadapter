package com.seagazer.screenadapter

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log

/**
 * Create by Seagazer
 * Date: 2020/4/25
 */
class ScreenAdapter {
    companion object {
        private const val DEFAULT_WIDTH_DP = 375
        private const val DEFAULT_HEIGHT_DP = 750
        private var sDensity: Float = 1.0f
        private var sScaleDensity: Float = 1.0f
        private var sApplication: Application? = null
        private val sDisplayMetricsCache = HashMap<String, DisplayMetricsHolder>()
        private var sCurrentDisplayMetrics: DisplayMetricsHolder? = null
        private val callback = object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            }

            override fun onActivityStarted(activity: Activity?) {
                sCurrentDisplayMetrics?.let {
                    val dm = sDisplayMetricsCache[activity!!.javaClass.name]
                    if (dm != null && dm.density != it.density) {
                        Log.d("ScreenAdapter", "Resume displayMetrics for current activity :$activity")
                        val activityDisplayMetrics = activity.resources.displayMetrics
                        activityDisplayMetrics.density = dm.density
                        activityDisplayMetrics.scaledDensity = dm.scaledDensity
                        activityDisplayMetrics.densityDpi = dm.densityDpi
                    }
                }
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
                sDisplayMetricsCache.remove(activity!!.javaClass.name)
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }
        }

        private fun initApplication(application: Application) {
            if (sApplication == null) {
                sApplication = application
                sApplication!!.registerActivityLifecycleCallbacks(callback)
            }
        }

        private fun refreshDisplayMetrics(activity: Activity, targetDensity: Float, targetScaleDensity: Float, targetDensityDpi: Int) {
            val holder = DisplayMetricsHolder(targetDensity, targetScaleDensity, targetDensityDpi)
            sDisplayMetricsCache[activity.javaClass.name] = holder
            sCurrentDisplayMetrics = holder
        }

        fun adjustWidthDensity(activity: Activity, application: Application) {
            adjustWidthDensity(activity, application, DEFAULT_WIDTH_DP)
        }

        fun adjustWidthDensity(activity: Activity, application: Application, widthDp: Int) {
            initApplication(application)
            val appDisplayMetrics = application.resources.displayMetrics
            sDensity = appDisplayMetrics.density
            sScaleDensity = appDisplayMetrics.scaledDensity
            if (sScaleDensity == 0f) {
                application.registerComponentCallbacks(object : ComponentCallbacks {
                    override fun onConfigurationChanged(newConfig: Configuration?) {
                        newConfig?.run {
                            if (fontScale > 0) {
                                sScaleDensity = application.resources.displayMetrics.scaledDensity
                            }
                        }
                    }

                    override fun onLowMemory() {
                    }
                })
            }

            val targetDensity = appDisplayMetrics.widthPixels * 1.0f / widthDp
            val targetScaleDensity = targetDensity * (sScaleDensity / sDensity)
            val targetDensityDpi = 160 * targetDensity.toInt()
            activity.resources.displayMetrics.run {
                density = targetDensity
                scaledDensity = targetScaleDensity
                densityDpi = targetDensityDpi
            }
            refreshDisplayMetrics(activity, targetDensity, targetScaleDensity, targetDensityDpi)
        }

        fun adjustHeightDensity(activity: Activity, application: Application) {
            adjustHeightDensity(activity, application, DEFAULT_HEIGHT_DP)
        }

        fun adjustHeightDensity(activity: Activity, application: Application, heightDp: Int) {
            initApplication(application)
            val appDisplayMetrics = application.resources.displayMetrics
            sDensity = appDisplayMetrics.density
            sScaleDensity = appDisplayMetrics.scaledDensity
            if (sScaleDensity == 0f) {
                application.registerComponentCallbacks(object : ComponentCallbacks {
                    override fun onConfigurationChanged(newConfig: Configuration?) {
                        newConfig?.run {
                            if (fontScale > 0) {
                                sScaleDensity = application.resources.displayMetrics.scaledDensity
                            }
                        }
                    }

                    override fun onLowMemory() {
                    }
                })
            }

            val targetDensity = appDisplayMetrics.heightPixels * 1.0f / heightDp
            val targetScaleDensity = targetDensity * (sScaleDensity / sDensity)
            val targetDensityDpi = 160 * targetDensity.toInt()
            activity.resources.displayMetrics.run {
                density = targetDensity
                scaledDensity = targetScaleDensity
                densityDpi = targetDensityDpi
            }
            refreshDisplayMetrics(activity, targetDensity, targetScaleDensity, targetDensityDpi)
        }
    }

    data class DisplayMetricsHolder constructor(
            val density: Float,
            val scaledDensity: Float,
            val densityDpi: Int
    )


}