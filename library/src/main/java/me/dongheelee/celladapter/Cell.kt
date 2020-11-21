package me.dongheelee.celladapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

abstract class Cell<T>(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    /**
     * Bind data into ViewHolder
     * @param data
     */
    abstract fun bind(data: T)
}
