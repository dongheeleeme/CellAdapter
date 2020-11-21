package me.dongheelee.celladapter

operator fun <T : CellType> T.plus(cellTypes: List<T>) = listOf(this) + cellTypes
operator fun <T : CellType> T.plus(cellType: T) = listOf(this) + cellType
