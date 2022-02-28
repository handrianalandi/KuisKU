package com.example.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adapterNilaiViewSiswa(private val listNilaiViewSiswa:MutableList<ujiansiswaclass>):
    RecyclerView.Adapter<adapterNilaiViewSiswa.ListViewHolder>()
{
    inner class ListViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var _tvNilai:TextView=itemView.findViewById(R.id.tvNilai)
        var _tvNama:TextView=itemView.findViewById(R.id.tvNama)
        var _tvKode:TextView=itemView.findViewById(R.id.tvKode)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapterNilaiViewSiswa.ListViewHolder {
        val view:View=LayoutInflater.from(parent.context)
            .inflate(R.layout.layoutnilaiviewsiswa,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapterNilaiViewSiswa.ListViewHolder, position: Int) {
        var nilai=listNilaiViewSiswa[position]

        holder._tvKode.setText(nilai.uuidujian)
        holder._tvNama.setText(nilai.uuidsiswa)
        holder._tvNilai.setText(nilai.nilai.toString())
    }

    override fun getItemCount(): Int {
       return listNilaiViewSiswa.size
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    fun refreshdata(/*list:List<nilai>*/){
//        listNilaiViewSiswa.clear()
//        listNilaiViewSiswa.addAll(list)
        notifyDataSetChanged()
    }

}