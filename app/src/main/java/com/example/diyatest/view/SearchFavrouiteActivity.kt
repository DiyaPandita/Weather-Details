package com.example.diyatest.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diyatest.R
import com.example.diyatest.Utils.ModelPreferencesManager
import com.example.diyatest.data.WeatherResponseData
import kotlinx.android.synthetic.main.activity_search_favrouite.*

class SearchFavrouiteActivity : AppCompatActivity() {

    lateinit var citiesArrayList: ArrayList<WeatherResponseData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_favrouite)
        initializeForecastList()
        initializeAdapter()
    }

    private fun initializeAdapter() {
        city_list.apply {
            layoutManager = LinearLayoutManager(this@SearchFavrouiteActivity)
            city_list.layoutManager = layoutManager
            city_list.adapter = com.example.diyatest.view.CityAdapter()
            (adapter as CityAdapter).updateItems(citiesArrayList as java.util.ArrayList<WeatherResponseData>);
        }
    }

    private fun initializeForecastList() {
        citiesArrayList = ModelPreferencesManager.getFavCitiesList()!!
    }

    @Override
    override fun onBackPressed() {
        finish()
    }
}