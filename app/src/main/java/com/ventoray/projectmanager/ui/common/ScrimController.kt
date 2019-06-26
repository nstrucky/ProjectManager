package com.ventoray.projectmanager.ui.common

import android.support.design.widget.BottomSheetBehavior
import android.view.View

class ScrimController(scrimView: View): BottomSheetBehavior.BottomSheetCallback() {
    private val scrim: View = scrimView
    override fun onSlide(view: View, position: Float) {
    }

    override fun onStateChanged(view: View, state: Int) {
        when (state) {
            BottomSheetBehavior.STATE_COLLAPSED, BottomSheetBehavior.STATE_HIDDEN -> hideScrim(true)
            BottomSheetBehavior.STATE_EXPANDED -> hideScrim(false)
        }
    }

    private fun hideScrim(hide: Boolean) {
        //TODO add animation
        if (hide) scrim.visibility = View.GONE else scrim.visibility = View.VISIBLE
    }
}