package me.dongheelee.celladapterdemo.cell

import me.dongheelee.celladapter.CellType
import me.dongheelee.celladapterdemo.R

sealed class CellTypes : CellType {

    object Header : CellTypes() {

        override fun uniqueId(): String = "Header"
        override fun layoutId(): Int = R.layout.item_header
        override fun spanSize(): Int = 2
    }

    data class Item(
        val id: Long,
        var name: String,
        var isSelected: Boolean = false
    ) : CellTypes() {

        override fun uniqueId(): String = "Item_${id}"
        override fun layoutId(): Int = R.layout.item
    }

    object Footer : CellTypes() {

        override fun uniqueId(): String = "Footer"
        override fun layoutId(): Int = R.layout.item_footer
        override fun spanSize(): Int = 2
    }
}
