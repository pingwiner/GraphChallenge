package com.cats32.challenge.ui

import androidx.recyclerview.widget.RecyclerView
import com.cats32.challenge.databinding.TableRowBinding
import com.cats32.challenge.db.DbPoint


class RowHolder(private val itemBinding: TableRowBinding) : RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(point: DbPoint) {
        itemBinding.x.text = point.x.toString()
        itemBinding.y.text = point.y.toString()
    }
}