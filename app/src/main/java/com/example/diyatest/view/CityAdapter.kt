package com.example.diyatest.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.diyatest.R
import com.example.diyatest.Utils.Constants
import com.example.diyatest.Utils.ModelPreferencesManager
import com.example.diyatest.data.WeatherResponseData
import kotlinx.android.synthetic.main.content_search_favrouite.view.*
import java.text.SimpleDateFormat
import java.util.*


class CityAdapter() : RecyclerView.Adapter<CityAdapter.MyViewHolder>() {

    val mCityData = ArrayList<WeatherResponseData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.content_search_favrouite, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        mCityData[position].let {
            holder.bind(mCityData = it)
        }
        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent = Intent(holder.itemView.context, MainActivity::class.java);
            intent.putExtra(Constants.SELECTED_WEATHER, (mCityData[position]))
            holder.itemView.context.startActivity(intent)
        })
        holder.itemView.img_dlt.setOnClickListener(View.OnClickListener {
            mCityData.removeAt(position)
            ModelPreferencesManager.saveCityToFavrouites(holder.itemView.context,mCityData)
            notifyDataSetChanged()
        })
    }

    override fun getItemCount(): Int {
        return mCityData.size
    }

    fun updateItems(list: MutableList<WeatherResponseData>){
        mCityData.clear()
        mCityData.addAll(list)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(mCityData: WeatherResponseData) {
            itemView.city_Country_txt.text = mCityData.name
            itemView.date_txt.text = (mCityData.date)?.toLong()?.let { getDate(it) }
            itemView.temp_txt.text = mCityData.main?.temp.toString()
        }

        private fun getDate(date: Long): String {
            val timeFormatter = SimpleDateFormat("dd.MM.yyyy")
            return timeFormatter.format(Date(date * 1000L))
        }
    }
}