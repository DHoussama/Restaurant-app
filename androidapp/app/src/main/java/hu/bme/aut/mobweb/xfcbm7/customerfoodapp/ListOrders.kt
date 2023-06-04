package hu.bme.aut.mobweb.xfcbm7.customerfoodapp

import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ListOrders : AppCompatActivity() {
    private lateinit var textViewOrders: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_orders)
        textViewOrders = findViewById(R.id.textOrders)
        sharedPreferences = getSharedPreferences("CustomerApp", MODE_PRIVATE)
        textViewOrders.movementMethod = ScrollingMovementMethod()
        fetchData("http://10.0.2.2:8000/api/users/orders/list?email="
                + sharedPreferences.getString("email", ""))
    }

    private fun parseJSON(response: String) {
        try {
            val jsonArray = JSONArray(response)
            for (i in 0 until jsonArray.length()) {
                val stu = jsonArray.getJSONObject(i)
                val id = stu.getString("id")
                val created_at = stu.getString("created_at")
                val status = stu.getString("status")
                val item_details = stu.getString("item_details")
                val jsonArrayItem = JSONArray(item_details)
                textViewOrders.append("Order placed on: $created_at\nStatus: $status\n")
                for (j in 0 until jsonArrayItem.length()) {
                    val jsonObjectItem = jsonArrayItem.getJSONObject(j)
                    textViewOrders.append("Items: \n${jsonObjectItem.getString("name")}\nPrice: " +
                            "${jsonObjectItem.getString("price")}\n")
                }
                textViewOrders.append("\n\n")
            }
        } catch (e: JSONException) {
            throw RuntimeException(e)
        }
    }

    private fun fetchData(apiUrl: String) {
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
        val stringRequest = StringRequest(Request.Method.GET, apiUrl,
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