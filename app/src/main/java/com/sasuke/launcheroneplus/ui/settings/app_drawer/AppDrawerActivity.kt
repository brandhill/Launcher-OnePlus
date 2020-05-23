package com.sasuke.launcheroneplus.ui.settings.app_drawer

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.sasuke.launcheroneplus.LauncherApp
import com.sasuke.launcheroneplus.R
import com.sasuke.launcheroneplus.data.model.SettingPreference
import com.sasuke.launcheroneplus.ui.base.BaseActivity
import com.sasuke.launcheroneplus.ui.color_picker.ColorPickerFragment
import com.sasuke.launcheroneplus.util.SharedPreferencesSettingsLiveData
import kotlinx.android.synthetic.main.activity_app_drawer_setting.*
import kotlinx.android.synthetic.main.activity_wallpaper_settings.toolbar
import javax.inject.Inject

class AppDrawerActivity : BaseActivity() {

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var sharedPreferencesSettingsLiveData: SharedPreferencesSettingsLiveData

    private lateinit var appDrawerActivityViewModel: AppDrawerActivityViewModel

    private lateinit var colorPickerFragment: ColorPickerFragment

    private var isFastScrollEnabled = false

    companion object {
        fun newIntent(context: Context) = Intent(context, AppDrawerActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_drawer_setting)
        inject()
        setupToolbar()
        observeLiveData()
        setupListeners()
        updateUI(LauncherApp.color)
        initColorPicker()
    }

    private fun inject() {
        appDrawerActivityViewModel =
            ViewModelProvider(this, viewModelFactory).get(AppDrawerActivityViewModel::class.java)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun observeLiveData() {
        sharedPreferencesSettingsLiveData.observe(this, Observer {
            it?.let {
                isFastScrollEnabled = it.isFastScrollEnabled
                updateUI(it)
            }
        })
    }

    private fun setupListeners() {
        clScrollAccentColor.setOnClickListener {
            openColorPicker()
        }
        clFastScroller.setOnClickListener {
            isFastScrollEnabled = !isFastScrollEnabled
            cbFastScroller.isChecked = isFastScrollEnabled
            appDrawerActivityViewModel.setFastScrollState(isFastScrollEnabled)
        }
    }

    private fun updateUI(color: Int) {
        if (color != 0) {
            AppCompatResources.getDrawable(this, R.drawable.scroll_accent_drawable)?.let {
                val wrappedDrawable = DrawableCompat.wrap(it)
                DrawableCompat.setTint(
                    wrappedDrawable,
                    color
                )
                glide.load(wrappedDrawable)
                    .into(ivIconScrollAccent)
            }
            cbFastScroller.buttonTintList = ColorStateList.valueOf(color)
            tvHeaderScroll.setTextColor(color)
        }
    }

    private fun initColorPicker() {
        colorPickerFragment = ColorPickerFragment.newInstance()
    }

    private fun openColorPicker() {
        colorPickerFragment.show(supportFragmentManager, colorPickerFragment.tag)
    }

    private fun updateUI(settingPreference: SettingPreference) {
        cbFastScroller.isChecked = isFastScrollEnabled
        updateUI(settingPreference.primaryColor)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        if (::colorPickerFragment.isInitialized) {
            colorPickerFragment.setOnClickListeners(null)
            if (colorPickerFragment.isVisible)
                colorPickerFragment.dismiss()
        }
        super.onDestroy()
    }
}