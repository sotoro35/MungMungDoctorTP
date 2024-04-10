package com.hsr2024.mungmungdoctortp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hsr2024.mungmungdoctortp.bnv2map.MapDetailActivity
import com.hsr2024.mungmungdoctortp.data.Place
import com.hsr2024.mungmungdoctortp.databinding.RecyclerItemMapBinding

class MapRecycelrAdapter(val context:Context, val itemList:List<Place>) : Adapter<MapRecycelrAdapter.VH>(){

    inner class VH(val binding: RecyclerItemMapBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val layoutInflater = LayoutInflater.from(context)
        val binding = RecyclerItemMapBinding.inflate(layoutInflater)
        return VH(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = itemList[position]
        holder.binding.tvPlaceName.text = item.place_name
        holder.binding.tvAddressName.text = item.address_name
        holder.binding.tvDistance.text = item.distance
        holder.binding.tvPhone.text = item.phone
        holder.binding.ivPhone.setOnClickListener {
            //전화해주는인텐트
            //전화인텐트
            val noHyphen = item.phone.replace("-", "")
            val intent = Intent(Intent.ACTION_DIAL)
            val uri = Uri.parse("tel:$noHyphen")
            intent.setData(uri)

            context.startActivity(intent)
        }
        holder.binding.root.setOnClickListener{
            //MapDetailActivity이동
            val intent = Intent(context, MapDetailActivity::class.java)
            intent.putExtra("url", item.place_url)
            context.startActivity(intent)
        }
    }

}