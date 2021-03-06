package com.sasuke.launcheroneplus.ui.hidden_apps.app_selector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.sasuke.launcheroneplus.R
import com.sasuke.launcheroneplus.data.model.App

class HiddenAppSelectionAdapter(private val glide: RequestManager) :
    RecyclerView.Adapter<HiddenAppSelectionViewHolder>(),
    HiddenAppSelectionViewHolder.OnClickListeners {

    private lateinit var onClickListeners: OnClickListeners
    private lateinit var appList: MutableList<App>

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HiddenAppSelectionViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cell_app_selector, parent, false)
        val holderHidden = HiddenAppSelectionViewHolder(view, glide)
        holderHidden.setOnClickListeners(this)
        return holderHidden
    }

    override fun getItemCount(): Int {
        return if (::appList.isInitialized) appList.size else 0
    }

    override fun onBindViewHolder(holderHidden: HiddenAppSelectionViewHolder, position: Int) {
        if (::appList.isInitialized) {
            holderHidden.setApp(appList[position])
        }
    }

    override fun onBindViewHolder(
        holderHidden: HiddenAppSelectionViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holderHidden, position, payloads)
        if (payloads.isNotEmpty()) {
            val flag = payloads[0]
            if (flag is Boolean) {
                holderHidden.toggle(flag)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    interface OnClickListeners {
        fun onHiddenItemClick(position: Int, appInfo: App)
    }

    fun setApps(list: MutableList<App>) {
        this.appList = list
        notifyDataSetChanged()
    }

    fun toggle(position: Int) {
        var flag = appList[position].isHidden
        flag = !flag
        appList[position].isHidden = flag
        notifyItemChanged(position, flag)
    }

    fun setOnClickListeners(onClickListeners: OnClickListeners) {
        this.onClickListeners = onClickListeners
    }

    override fun onHiddenItemClick(position: Int, appInfo: App) {
        if (::onClickListeners.isInitialized)
            onClickListeners.onHiddenItemClick(position, appInfo)
    }

//    override fun getSectionText(position: Int): CharSequence {
//        return appList[position].label[0].toUpperCase().toString()
//    }
}