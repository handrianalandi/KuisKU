package com.example.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView

class adapterAddSoalGuru(private val listSoalGuru: MutableList<soalGuru>):
RecyclerView.Adapter<adapterAddSoalGuru.ListViewHolder>()
{
    private lateinit var onItemClickCallback : OnItemClickCallback
    interface OnItemClickCallback{
        fun editSoal(data:soalGuru)
    }
    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback=onItemClickCallback
    }

    inner class ListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var _tvSoal:TextView=itemView.findViewById(R.id.tvSoal)
        var _tvJawabanA:TextView=itemView.findViewById(R.id.tvJawabanA)
        var _tvJawabanB:TextView=itemView.findViewById(R.id.tvJawabanB)
        var _tvJawabanC:TextView=itemView.findViewById(R.id.tvJawabanC)
        var _tvJawabanD:TextView=itemView.findViewById(R.id.tvJawabanD)
        var _tvJawabanBenar:TextView=itemView.findViewById(R.id.tvJawabanBenar)
        var _btnEdit:ImageButton=itemView.findViewById(R.id.ivEdit)
        var _btnDelete:ImageButton=itemView.findViewById(R.id.ivDelete)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapterAddSoalGuru.ListViewHolder {
        val view:View=LayoutInflater.from(parent.context)
            .inflate(R.layout.layoutsoalguru,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapterAddSoalGuru.ListViewHolder, position: Int) {
        var soal=listSoalGuru[position]

        holder._tvJawabanA.setText("A."+soal.jawaban1)
        holder._tvJawabanB.setText("B."+soal.jawaban2)
        holder._tvJawabanC.setText("C."+soal.jawaban3)
        holder._tvJawabanD.setText("D."+soal.jawaban4)
        holder._tvJawabanBenar.setText("Jawaban: "+soal.jawabanbenar)
        holder._tvSoal.setText(soal.soal)

        holder._btnEdit.setOnClickListener {
            onItemClickCallback.editSoal(soal)
        }

        holder._btnDelete.setOnClickListener {
            listSoalGuru.removeAt(position)
            db.collection("tbSoal").document(soal.idsoal).delete()
            notifyDataSetChanged()
        }
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun getItemCount(): Int {
        return listSoalGuru.size
    }

}