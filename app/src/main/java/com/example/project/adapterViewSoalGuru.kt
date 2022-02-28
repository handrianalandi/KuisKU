package com.example.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adapterViewSoalGuru(private val ListViewSoalGuru:MutableList<ujianclass>) :
    RecyclerView.Adapter<adapterViewSoalGuru.ListViewHolder>()
{
    private lateinit var onItemClickCallback : OnItemClickCallback

    interface OnItemClickCallback {
        fun onEditClicked(data : ujianclass)
        fun editUjian(data:ujianclass)
    }

    fun setOnItemClickCallback(onItemClickCallback : OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
    inner class ListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        var _NomorSoal: TextView =itemView.findViewById(R.id.NomorSoal)
        var _namaujian: TextView =itemView.findViewById(R.id.Soal)
        var _btnimgview: ImageButton =itemView.findViewById(R.id.imgbtnview)
        var _btnimgedit: ImageButton =itemView.findViewById(R.id.imgbtnedit)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapterViewSoalGuru.ListViewHolder {
        val view:View= LayoutInflater.from(parent.context)
            .inflate(R.layout.layoutviewsoalguru,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapterViewSoalGuru.ListViewHolder, position: Int) {
        var soal=ListViewSoalGuru[position]
        holder._NomorSoal.setText(soal.kode)
        holder._namaujian.setText(soal.nama)

        holder._btnimgview.setOnClickListener{
            onItemClickCallback.onEditClicked(soal)
        }
        holder._btnimgedit.setOnClickListener {
            onItemClickCallback.editUjian(soal)
        }
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun getItemCount(): Int {
        return ListViewSoalGuru.size
    }
}