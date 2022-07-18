package com.gkkendor.app.adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gkkendor.app.R
import com.gkkendor.app.model.Report
import com.google.android.material.textview.MaterialTextView

class ReportAdapter : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {
    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTittle: MaterialTextView = itemView.findViewById(R.id.tvReportTittle)
        val tvDesc: MaterialTextView = itemView.findViewById(R.id.tvReportDesc)
        val tvDate: MaterialTextView = itemView.findViewById(R.id.tvReportDate)
        val tvStatus: MaterialTextView = itemView.findViewById(R.id.tvReportStatus)
    }

    private val differCallback = object : DiffUtil.ItemCallback<Report>() {
        override fun areItemsTheSame(oldItem: Report, newItem: Report): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Report, newItem: Report): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        return ReportViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_report,
                parent,
                false
            )
        )
    }

    private var onItemClickListener: ((Report) -> Unit)? = null

    fun setOnItemClickListener(listener: (Report) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = differ.currentList[position]
        holder.itemView.apply {

            holder.tvTittle.text = report.title
            holder.tvDesc.text = report.content
            holder.tvDate.text = report.publishedAt
            holder.tvStatus.apply {
                text = report.status
                when(report.status){
                    "Success" -> setTextColor(Color.GREEN)
                    "Failed" -> setTextColor(Color.RED)
                    "Pending" -> setTextColor(Color.DKGRAY)
                    else -> setTextColor(Color.DKGRAY)
                }
            }
            setOnClickListener {
                onItemClickListener?.let {
                    it(report)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}