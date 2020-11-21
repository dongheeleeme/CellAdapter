package me.dongheelee.celladapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class Cell<T>(containerView: View) : RecyclerView.ViewHolder(containerView) {

    /**
     * Bind data into ViewHolder
     * @param data
     */
    abstract fun bind(data: T)
}
