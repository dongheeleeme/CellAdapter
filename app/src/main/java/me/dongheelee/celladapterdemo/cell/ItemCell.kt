package me.dongheelee.celladapterdemo.cell

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import me.dongheelee.celladapter.Cell
import me.dongheelee.celladapterdemo.R

class ItemCell(
    private val containerView: View,
    private val isItemClickedEvent: (CellTypes.Item) -> Unit,
    private val isItemSelectedEvent: (CellTypes.Item) -> Unit
) : Cell<CellTypes>(containerView) {

    private val numberText by lazy { containerView.findViewById<TextView>(R.id.number_text) }
    private val checkBox by lazy { containerView.findViewById<CheckBox>(R.id.checkbox) }

    override fun bind(data: CellTypes) {
        if (data !is CellTypes.Item) return

        numberText.text = data.name
        containerView.setOnClickListener { isItemClickedEvent.invoke(data) }

        checkBox.isChecked = data.isSelected
        checkBox.setOnClickListener {
            data.isSelected = checkBox.isChecked
            isItemSelectedEvent.invoke(data)
        }
    }
}
