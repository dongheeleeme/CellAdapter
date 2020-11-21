package me.dongheelee.celladapter

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

private const val HEAD_CELL_POSITION = 0

open class CellAdapter<T : CellType>(
    private val delegate: Delegate<T>
) : RecyclerView.Adapter<Cell<T>>() {

    protected val cells = mutableListOf<T>()

    /**
     * Set cells into the RecyclerView
     * @param cells
     */
    fun setCells(cells: List<T>) {
        val diffCallback = CellDiffCallback(this.cells, cells)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.cells.clear()
        this.cells.addAll(cells)

        diffResult.dispatchUpdatesTo(this)
    }

    /**
     * Add cell into the RecyclerView
     * @param cell
     * @param where position of RecyclerView
     */
    fun addCell(cell: T, where: CellPosition = CellPosition.TAIL) {
        when (where) {
            CellPosition.HEAD -> {
                cells.add(HEAD_CELL_POSITION, cell)
                notifyItemInserted(HEAD_CELL_POSITION)
            }
            CellPosition.TAIL -> {
                cells.add(cell)
                notifyItemInserted(cells.size)
            }
        }
    }

    /**
     * Add cells into the RecyclerView
     * @param cells
     * @param where position of RecyclerView
     */
    fun addCells(cells: List<T>, where: CellPosition = CellPosition.TAIL) {
        when (where) {
            CellPosition.HEAD -> {
                this.cells.addAll(HEAD_CELL_POSITION, cells)
                notifyItemRangeInserted(HEAD_CELL_POSITION, cells.size)
            }
            CellPosition.TAIL -> {
                this.cells.addAll(cells)
                notifyItemRangeInserted(tailCellPosition, cells.size)
            }
        }
    }

    /**
     * Add cells into the RecyclerView
     * @param cells
     * @param position position of RecyclerView
     */
    fun addCells(cells: List<T>, position: Int) {
        this.cells.addAll(position, cells)
        notifyItemRangeInserted(position, cells.size)
    }

    /**
     * Update cell of RecyclerView
     * @param cell
     */
    fun updateCell(cell: T) {
        val position = getCellPositionOrNull(cell) ?: throw CouldNotFindCellException()

        cells[position] = cell
        Handler().postDelayed({ notifyItemChanged(position, Unit) }, 10)
    }

    /**
     * Clear cells of RecyclerView
     */
    fun clearCells() {
        cells.clear()
        notifyDataSetChanged()
    }

    /**
     * Remove cell of RecyclerView by position
     * @param position
     */
    fun removeCell(position: Int) {
        cells.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * Remove cell of RecyclerView by cell
     * @param cell
     */
    fun removeCell(cell: T) {
        val position = getCellPositionOrNull(cell) ?: return

        cells.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * Remove cell of RecyclerView by cell position
     * @param where
     */
    fun removeCell(where: CellPosition) {
        when (where) {
            CellPosition.HEAD -> removeCell(HEAD_CELL_POSITION)
            CellPosition.TAIL -> removeCell(tailCellPosition)
        }
    }

    /**
     * Remove cell of RecyclerView skip tail
     */
    fun removeCellSkipTail() {
        cells.removeAt(tailCellPosition - 1)
        notifyItemRemoved(tailCellPosition)
    }

    /**
     * Remove cells of RecyclerView
     * @param cells
     */
    fun removeCells(cells: List<T>) {
        val filteredCells = this.cells.filter { !cells.contains(it) }

        val diffCallback = CellDiffCallback(this.cells, filteredCells)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.cells.clear()
        this.cells.addAll(filteredCells)

        diffResult.dispatchUpdatesTo(this)
    }

    /**
     * Get cell position or null
     * @param cell
     */
    fun getCellPositionOrNull(cell: T): Int? =
        try {
            val foundCell = cells.first { it.uniqueId() == cell.uniqueId() }
            cells.indexOf(foundCell)
        } catch (e: NoSuchElementException) {
            null
        }

    /**
     * Get cell as <T>
     * @param position
     */
    fun <T : CellType> getCellAs(position: Int) = (cells[position] as T)

    /**
     * Get count of cells
     */
    fun getCellCount(): Int = itemCount

    /**
     * Get span size of cell
     */
    fun getCellSpanSize(position: Int): Int = cells[position].spanSize()

    /**
     * Get tail position of cells
     */
    private val tailCellPosition
        get() = cells.size - 1

    override fun getItemViewType(position: Int): Int = cells[position].layoutId()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Cell<T> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return delegate.cellTypeMapper(viewType, view)
    }

    override fun onBindViewHolder(holder: Cell<T>, position: Int) {
        holder.bind(cells[position])
    }

    override fun getItemCount(): Int = cells.size

    /**
     * Delegate to map cell type to cell
     */
    interface Delegate<T : CellType> {

        fun cellTypeMapper(viewType: Int, view: View): Cell<T>
    }
}

class CouldNotFindCellException : IllegalStateException() {

    override val message: String? = "CouldNotFindCellException"
}
