package com.cats32.challenge.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cats32.challenge.databinding.TableRowBinding
import com.cats32.challenge.db.DbPoint


class PointsAdapter : RecyclerView.Adapter<RowHolder>() {
    private var data = listOf<DbPoint>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val itemBinding = TableRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RowHolder(itemBinding)
    }

    fun submit(newData: List<DbPoint>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(viewHolder: RowHolder, position: Int) {
        viewHolder.bind(data[position])
    }

    override fun getItemCount() = data.size
}