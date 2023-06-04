package hu.bme.aut.mobweb.xfcbm7.customerfoodapp

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Cart : AppCompatActivity() {
    private lateinit var textViewCartData: TextView
    private lateinit var textViewDisDur: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var buttonConfirm: Button
    private lateinit var buttonRemove: Button
    private lateinit var totalPriceField: TextView

    private val urlConfirm =
        "http://10.0.2.2:8000/api/users/cart/confirm?user_email="

    private val urlRemove =
        "http://10.0.2.2:8000/api/users/cart/clear?user_email="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        textViewCartData = findViewById(R.id.textCartData)
        totalPriceField = findViewById(R.id.totalPrice)
        textViewDisDur = findViewById(R.id.textDisDur)
        sharedPreferences = getSharedPreferences("CustomerApp", MODE_PRIVATE)
        buttonConfirm = findViewById(R.id.btnConfirmOrder)
        buttonRemove = findViewById(R.id.btnClearCart)
        fetchData()
        buttonConfirm.setOnClickListener {
            sendRequest(it, urlConfirm + sharedPreferences.getString("email", ""))
        }

        buttonRemove.setOnClickListener {
            sendRequest(it, urlRemove + sharedPreferences.getString("email", ""))
        }
    }

    private fun sendRequest(view: View, apiUrl: String) {
        Log.e("url", apiUrl)
        view.isEnabled = false
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
        val stringRequest = StringRequest(Request.Method.GET, apiUrl,
            Response.Listener<String> { response ->
                view.isEnabled = true
                if (response == "success") {
                    Toast.makeText(applicationContext, "Operation success", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Operation failed", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                view.isEnabled = true
                error.printStackTrace()
            })
        queue.add(stringRequest)
    }

    private fun parseJSON(data: String) {
        try {
            val jsonArray = JSONArray(data)
            val jsonObject = jsonArray.getJSONObject(0)
            val jsonArray1 = jsonObject.getJSONArray("cart")
            var totalPrice = 0
            for (i in 0 until jsonArray1.length()) {
                val stu = jsonArray1.getJSONObject(i)
                val id = stu.getString("id")
                val name = stu.getString("name")
                val des = stu.getString("description")
                val numItem = stu.getString("numItem")
                val price = stu.getString("price")
                textViewCartData.append("Name: $name\nPrice: $price$\nNumber of Items: $numItem\n\n")
                totalPrice += price.toInt() * numItem.toInt()
            }
            totalPriceField.text = "Your Total Price is : $totalPrice$\n "
           // val distance = (jsonObject.getInt("distance") / 1000).toString()
           // val duration = (jsonObject.getInt("duration") / 60).toString()
           // val total_price = distance.toInt() * pricePerKM

          //  textViewDisDur.text = "Distance: $distance KM\nDelivery Fee: " +
            //        "$total_price $ \nDuration: $duration Minutes"
        } catch (e: JSONException) {
            Log.e("error", e.localizedMessage)
            e.printStackTrace()
        }
    }
    private fun fetchData() {
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(
            Request.Method.GET,
            "http://10.0.2.2:8000/api/users/cart/list?user_email="
                    + sharedPreferences.getString("email", ""),
            Response.Listener<String> { response ->
                parseJSON(response)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            }
        )
        queue.add(stringRequest)
    }

}

