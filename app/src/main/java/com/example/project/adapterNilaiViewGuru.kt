package com.example.project

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class adapterNilaiViewGuru (private val listNilaiViewGuru:MutableList<ujiansiswaclass>):
RecyclerView.Adapter<adapterNilaiViewGuru.ListViewHolder>()
{
    inner class ListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var _tvNilai: TextView =itemView.findViewById(R.id.tvNilai)
        var _tvNama: TextView =itemView.findViewById(R.id.tvNama)
        var _tvKode: TextView =itemView.findViewById(R.id.tvKode)
        var _btnDelete:ImageButton=itemView.findViewById(R.id.btnDelete)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapterNilaiViewGuru.ListViewHolder {
        val view:View= LayoutInflater.from(parent.context)
            .inflate(R.layout.layoutnilaiviewsiswa,parent,false)
        return ListViewHolder(view)
    }
    override fun onBindViewHolder(holder: adapterNilaiViewGuru.ListViewHolder, position: Int) {
        var nilai=listNilaiViewGuru[position]

        holder._tvKode.setText(nilai.uuidujian)
        holder._tvNama.setText(nilai.uuidsiswa)
        holder._tvNilai.setText(nilai.nilai)
        holder._btnDelete.visibility=View.VISIBLE

        holder._btnDelete.setOnClickListener {
            AlertDialog.Builder(holder._btnDelete.context)
                .setTitle("Masuk Ujian")
                .setMessage(
                    "!!PERHATIAN!!\n" +
                            "Data akan terhapus dan tidak bisa dikembalikan\n\n" +
                            "Apakah anda yakin mau menghapus data ${nilai.uuidsiswa}?"
                )
                .setPositiveButton(
                    "Hapus",
                    DialogInterface.OnClickListener { dialog, which ->
                        db.collection("tbUjianSiswa").document(nilai.uuid).delete()
                        listNilaiViewGuru.removeAt(position)
                        Toast.makeText(holder._btnDelete.context,"Record ujian siswa berhasil dihapus!",Toast.LENGTH_SHORT).show()
                        notifyDataSetChanged()
                    }
                )
                .setNegativeButton(
                    "Batal",
                    DialogInterface.OnClickListener { dialog, which ->

                    }
                ).show()
        }
    }
    override fun getItemCount(): Int {
        return listNilaiViewGuru.size
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
}