package me.dongheelee.celladapter

private const val DEFAULT_SPAN_SIZE = 1

interface CellType {
    fun layoutId(): Int
    fun uniqueId(): String
    fun spanSize(): Int = DEFAULT_SPAN_SIZE
}
