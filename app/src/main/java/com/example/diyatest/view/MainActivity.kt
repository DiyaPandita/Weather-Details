package com.example.diyatest.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.diyatest.R
import com.example.diyatest.Utils.Constants
import com.example.diyatest.Utils.Constants.Companion.ORIENTATION_SAVE_DATA
import com.example.diyatest.Utils.ModelPreferencesManager
import com.example.diyatest.data.ErrorTypes
import com.example.diyatest.data.WeatherResponseData
import com.example.diyatest.di.DaggerWeatherComponent
import com.example.diyatest.di.WeatherModule
import com.example.diyatest.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), MainView {
    val presenter = MainPresenter(this)
    private var pressedTime: Long = 0
    lateinit var cityName: String

    lateinit var weatherResponse: WeatherResponseData
    var saveDestinationCity: MutableList<WeatherResponseData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDI()
        setContentView(R.layout.activity_main)
        showError()
        if (getIntent().getExtras() != null) {
            weatherResponse =
                getIntent().getSerializableExtra(Constants.SELECTED_WEATHER) as WeatherResponseData
            updateForecast(weatherResponse)
        }
        checkSharedPreference()
        addToFav()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_page_menu, menu)

        val menuItem = menu?.findItem(R.id.search_button)
        val searchMenuItem = menuItem?.actionView

        if (searchMenuItem is SearchView) {
            searchMenuItem.queryHint = getString(R.string.menu_search_hint)
            searchMenuItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    cityName = query
                    getForecast(query)
                    menuItem.collapseActionView()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }
        return true
    }

    override fun hideSpinner() {
        mainContainer.visibility = View.VISIBLE
        loadingSpinner.visibility = View.GONE
    }

    override fun updateForecast(forecasts: WeatherResponseData) {

        weatherResponse = forecasts;
        ModelPreferencesManager.put(weatherResponse, Constants.SavedCity)
        if (ModelPreferencesManager.getFavCitiesList()!!.contains(weatherResponse)) {
            var tempArrayList = ModelPreferencesManager.getFavCitiesList()

            tempArrayList!!.removeAt(ModelPreferencesManager.getFavCitiesList()!!
                .indexOf(weatherResponse))
            tempArrayList.add(weatherResponse)
            ModelPreferencesManager.saveCityToFavrouites(this, tempArrayList)

        }
        showContainer()
        city_temp.text = forecasts.main?.temp.toString() + "°C"
        city_name.text = forecasts.name + ", " + forecasts.sys.country
        city_dateTime.text = forecasts.date?.toLong()?.let { getDateTime(it) };
        city_name.text = forecasts.name
        city_description.text = forecasts.forecast.get(0).description
        city_humidity.text =
            getString(R.string.humitdity) + " " + forecasts.main?.humidity.toString()
        city_pressure.text =
            getString(R.string.pressure) + " " + forecasts.main?.pressure.toString()
        city_temp_max.text =
            getString(R.string.max_temp) + forecasts.main?.tempMax.toString() + "°C"
        city_temp_min.text =
            getString(R.string.min_temp) + forecasts.main?.tempMin.toString() + "°C"

    }

    private fun getForecast(query: String) = presenter.getDailyForecast(query)

    fun Activity.toast(toastMessage: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, toastMessage, duration).show()
    }

    private fun cityAlreadySearched(): Boolean {
        for (i in ModelPreferencesManager.getFavCitiesList()!!) {
            if (i.name.equals(cityName, true)) {
                updateForecast(i)
                return true
            }
        }
        return false
    }

    private fun getDateTime(timeStamp: Long): String? {
        try {
            val timeFormatter = SimpleDateFormat("dd-MMM-yyyy, HH:mm")
            return timeFormatter.format(Date(timeStamp * 1000L))
        } catch (e: Exception) {
            return e.toString()
        }
    }

    private fun checkSharedPreference() {
        ModelPreferencesManager.get<WeatherResponseData>(Constants.SavedCity)?.let {
            updateForecast(it)
        }
        saveDestinationCity = ModelPreferencesManager.getFavCitiesList()!!
    }

    private fun addToFav() {
        lnr_addFav.setOnClickListener(View.OnClickListener {
            if (ModelPreferencesManager.getFavCitiesList()!!.contains(weatherResponse)) {
                toast(getString(R.string.city_already_added))
            } else {
                saveDestinationCity?.add(weatherResponse)
                ModelPreferencesManager.saveCityToFavrouites(this, saveDestinationCity)

                toast(getString(R.string.city_added))

            }
        })

        lnr_fav.setOnClickListener(View.OnClickListener {
            if (ModelPreferencesManager.getFavCitiesList()!!.size > 0) {
                val intent = Intent(this@MainActivity, SearchFavrouiteActivity::class.java);
                startActivity(intent)
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.empty_fav_list),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun injectDI() {
        DaggerWeatherComponent
            .builder()
            .weatherModule(WeatherModule())
            .build()
            .inject(presenter)
    }

    override fun showContainer() {
        mainContainer.visibility = View.VISIBLE
        loadingSpinner.visibility = View.GONE
        errorText.visibility = View.GONE
    }

    override fun showSpinner() {
        mainContainer.visibility = View.GONE
        errorText.visibility = View.GONE
        loadingSpinner.visibility = View.VISIBLE
    }

    override fun showError() {
        mainContainer.visibility = View.GONE
        loadingSpinner.visibility = View.GONE
        errorText.visibility = View.VISIBLE
    }


    override fun showErrorToast(errorType: ErrorTypes) {
        showError()
        when (errorType) {
            ErrorTypes.CALL_ERROR -> {

                if (!cityAlreadySearched())
                    ModelPreferencesManager.get<WeatherResponseData>(Constants.SavedCity)?.let {
                        updateForecast(it)
                    }
                toast(getString(R.string.connection_error_message))
            }
            ErrorTypes.MISSING_API_KEY -> toast(getString(R.string.missing_api_key_message))
            ErrorTypes.NO_RESULT_FOUND -> toast(getString(R.string.city_not_found_toast_message))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (::weatherResponse.isInitialized)
            outState.putSerializable(ORIENTATION_SAVE_DATA, weatherResponse)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState.getSerializable(ORIENTATION_SAVE_DATA) != null)
            updateForecast(savedInstanceState.getSerializable(ORIENTATION_SAVE_DATA) as WeatherResponseData)
        super.onRestoreInstanceState(savedInstanceState)
    }

    @Override
    override fun onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
        } else {
            toast(getString(R.string.again_back_press))
        }
        pressedTime = System.currentTimeMillis();
    }

}
