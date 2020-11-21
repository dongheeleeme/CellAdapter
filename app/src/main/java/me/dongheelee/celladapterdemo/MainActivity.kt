package me.dongheelee.celladapterdemo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.dongheelee.celladapter.*
import me.dongheelee.celladapterdemo.cell.CellTypes
import me.dongheelee.celladapterdemo.cell.CellTypes.*
import me.dongheelee.celladapterdemo.cell.ItemCell

class MainActivity : AppCompatActivity(), CellAdapter.Delegate<CellTypes> {

    private var cellAdapter = CellAdapter(this)

    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recycler_view) }
    private val gridButton by lazy { findViewById<Button>(R.id.grid_button) }
    private val linearButton by lazy { findViewById<Button>(R.id.linear_button) }
    private val shuffleButton by lazy { findViewById<Button>(R.id.shuffle_button) }
    private val clearButton by lazy { findViewById<Button>(R.id.clear_button) }

    private var isItemRemoved = false

    private val items: List<Item> =
        (0..100).map { it.toString() }.map {
            Item(it.toLong(), it)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        initClickListeners()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = cellAdapter

        cellAdapter.setCells(Header + items + Footer)
    }

    private fun initClickListeners() {
        gridButton.setOnClickListener {
            val gridLayoutManager = GridLayoutManager(this, 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int =
                        cellAdapter.getCellSpanSize(position)
                }
            }
            recyclerView.layoutManager = gridLayoutManager
        }

        linearButton.setOnClickListener {
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

        shuffleButton.setOnClickListener {
            if (isItemRemoved) {
                cellAdapter.addCells(items, 1)

                isItemRemoved = false

                updateButtons()
            } else {
                cellAdapter.setCells(Header + items.shuffled() + Footer)
            }
        }

        clearButton.setOnClickListener {
            cellAdapter.removeCells(
                cells = items.filter { it.uniqueId().startsWith("Item") }
            )

            isItemRemoved = true

            updateButtons()
        }
    }

    override fun cellTypeMapper(viewType: Int, view: View): Cell<CellTypes> =
        when (viewType) {
            R.layout.item_header,
            R.layout.item_footer -> StaticCell(view)
//            R.layout.item -> object : Cell<CellTypes>(view) {
//
//                override fun bind(data: CellTypes) {
//                    if (data !is Item) return
//                    containerView.number_text.text = data.name
//                }
//            }
            R.layout.item -> ItemCell(
                view,
                isItemClickedEvent = {
//                    addCell(CellPosition.HEAD)
//                    addCell(CellPosition.TAIL)
//
//                    addCells(CellPosition.HEAD)
//                    addCells(CellPosition.TAIL)
//
//                    updateCell(it)
//
//                    getCellPositionOrNull(it)
//
//                    clearCells()
//
//                    removeCellByPosition()
//                    removeCellByCellPosition(CellPosition.HEAD)
//                    removeCellByCellPosition(CellPosition.TAIL)
//                    removeCellByCell(it)
//                    removeCellWithSkipTail()
                },
                isItemSelectedEvent = cellAdapter::updateCell
            )
            else -> error("Couldn't match the view type")
        }

    private fun updateCell(cell: CellTypes) {
        val position = cellAdapter.getCellPositionOrNull(cell) ?: return
        val headCell = cellAdapter.getCellAs<Item>(position)

        headCell.name = getRandomNumber().toString()
        cellAdapter.updateCell(headCell)
    }

    private fun addCells(cellPosition: CellPosition) {
        val cellCount = cellAdapter.getCellCount().toLong()

        cellAdapter.addCells(
            cells = Item(
                id = cellCount,
                name = getRandomNumber().toString()
            ) + Item(
                id = cellCount + 1,
                name = getRandomNumber().toString()
            ),
            where = cellPosition
        )
    }

    private fun addCell(cellPosition: CellPosition) {
        val cellCount = cellAdapter.getCellCount().toLong()

        cellAdapter.addCell(
            cell = Item(
                cellCount,
                getRandomNumber().toString()
            ),
            where = cellPosition
        )
    }

    private fun getCellPositionOrNull(it: Item) {
        val position = cellAdapter.getCellPositionOrNull(it) ?: return

        Toast.makeText(this, position.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun clearCells() {
        cellAdapter.clearCells()
    }

    private fun removeCellWithSkipTail() {
        cellAdapter.removeCellSkipTail()
    }

    private fun removeCellByCell(cell: CellTypes) {
        cellAdapter.removeCell(cell)
    }

    private fun removeCellByCellPosition(cellPosition: CellPosition) {
        cellAdapter.removeCell(cellPosition)
    }

    private fun removeCellByPosition() {
        cellAdapter.removeCell(0)
    }

    private fun updateButtons() {
        if (isItemRemoved) {
            shuffleButton.text = getString(R.string.add_items)
        } else {
            shuffleButton.text = getString(R.string.shuffle)
        }
    }

    private fun getRandomNumber(): Int = (0..100).random()
}
