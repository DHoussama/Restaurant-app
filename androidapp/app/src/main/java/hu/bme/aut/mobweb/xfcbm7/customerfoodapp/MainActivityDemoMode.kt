package hu.bme.aut.mobweb.xfcbm7.customerfoodapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class MainActivityDemoMode : AppCompatActivity() {
    private lateinit var arrayList: ArrayList<ListData>
    private val apiURL = "http://10.0.2.2:8000/api/product/list"
    private lateinit var buttonGoLogin: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_demo_mode)

        buttonGoLogin = findViewById(R.id.goLogin)
        arrayList = ArrayList()
    fetchData()

        buttonGoLogin.setOnClickListener {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }


}

private fun parseJSON(data: String) {
    try {
        val jsonArray = JSONArray(data)
        for (i in 0 until jsonArray.length()) {
            val stu = jsonArray.getJSONObject(i)
            val id = stu.getString("id")
            val name = stu.getString("name")
            val des = stu.getString("description")
            val image = stu.getString("image")
            val price = stu.getString("price")
            arrayList.add(ListData(id, name, des, image, price))
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewDemo)
        val adapter = RecyclerViewAdapter(arrayList,mode = true)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    } catch (e: JSONException) {
        e.printStackTrace()
    }
}

private fun fetchData() {
    val queue: RequestQueue = Volley.newRequestQueue(this)
    val stringRequest = object : StringRequest(
        Request.Method.GET, apiURL,
        Response.Listener<String> { response ->
            parseJSON(response)
        },
        Response.ErrorListener { error: VolleyError ->
            error.printStackTrace()
        }) {}

    queue.add(stringRequest)
}
}