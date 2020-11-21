package me.dongheelee.celladapter

import android.view.View

/**
 * This is used for static cell. static cell have the role of drawing ui without data.
 * @param containerView
 */
class StaticCell<T>(containerView: View) : Cell<T>(containerView) {

    override fun bind(data: T) {
        return
    }
}
