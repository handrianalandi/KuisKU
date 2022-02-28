package com.example.project

import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import kotlin.coroutines.coroutineContext

class adapterSoalViewSiswa(private val listSoalViewSiswa:MutableList<soalGuru>) :
RecyclerView.Adapter<adapterSoalViewSiswa.ListViewHolder>()

{
    private lateinit var onItemClickCallback : OnItemClickCallback

    interface OnItemClickCallback{
        fun setJawaban(jawabansoal:String,position:Int)
    }

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback=onItemClickCallback
    }

    inner class ListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
//        var _tvNomer:TextView=itemView.findViewById(R.id.tvNomer)
        var _tvSoal:TextView=itemView.findViewById(R.id.tvSoal)
        var _rbJawaban1:RadioButton=itemView.findViewById(R.id.rbJawaban1)
        var _rbJawaban2:RadioButton=itemView.findViewById(R.id.rbJawaban2)
        var _rbJawaban3:RadioButton=itemView.findViewById(R.id.rbJawaban3)
        var _rbJawaban4:RadioButton=itemView.findViewById(R.id.rbJawaban4)
        var _rgJawaban:RadioGroup=itemView.findViewById(R.id.rgJawaban)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapterSoalViewSiswa.ListViewHolder {
        val view:View=LayoutInflater.from(parent.context)
            .inflate(R.layout.layoutsoalviewsiswa,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapterSoalViewSiswa.ListViewHolder, position: Int) {
        var soal=listSoalViewSiswa[position]

        holder._tvSoal.setText(soal.soal)

        holder._rbJawaban1.isSelected=false
        holder._rbJawaban2.isSelected=false
        holder._rbJawaban3.isSelected=false
        holder._rbJawaban4.isSelected=false

        holder._rbJawaban1.setText(soal.jawaban1)
        holder._rbJawaban2.setText(soal.jawaban2)
        holder._rbJawaban3.setText(soal.jawaban3)
        holder._rbJawaban4.setText(soal.jawaban4)


        holder._rbJawaban1.setOnClickListener {
            onItemClickCallback.setJawaban(/*holder._rbJawaban1.text.toString()*/"A",position)
        }
        holder._rbJawaban2.setOnClickListener {
            onItemClickCallback.setJawaban(/*holder._rbJawaban2.text.toString()*/"B",position)
        }
        holder._rbJawaban3.setOnClickListener {
            onItemClickCallback.setJawaban(/*holder._rbJawaban3.text.toString()*/"C",position)
        }
        holder._rbJawaban4.setOnClickListener {
            onItemClickCallback.setJawaban(/*holder._rbJawaban4.text.toString()*/"D",position)
        }



//        fun getJawaban(): Int {
//            return holder._rgJawaban.checkedRadioButtonId
//        }
    }
    //supaya tidak ke recycle
    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun getItemCount(): Int {
       return listSoalViewSiswa.size
    }
    fun refreshdata(){
        notifyDataSetChanged()
    }
}