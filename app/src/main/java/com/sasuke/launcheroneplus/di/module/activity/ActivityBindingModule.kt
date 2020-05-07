package com.sasuke.launcheroneplus.di.module.activity

import com.sasuke.launcheroneplus.ui.launcher.LauncherActivity
import com.sasuke.launcheroneplus.di.scope.PerActivityScope
import com.sasuke.launcheroneplus.ui.hidden_apps.HiddenAppsActivity
import com.sasuke.launcheroneplus.ui.hidden_apps.app_selector.AppSelectionActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [])
abstract class ActivityBindingModule {

    @PerActivityScope
    @ContributesAndroidInjector(modules = [LauncherActivityModule::class])
    internal abstract fun mainActivity(): LauncherActivity

    @PerActivityScope
    @ContributesAndroidInjector(modules = [])
    internal abstract fun hiddenAppsActivity(): HiddenAppsActivity

    @PerActivityScope
    @ContributesAndroidInjector(modules = [])
    internal abstract fun appSelectionActivity(): AppSelectionActivity
}