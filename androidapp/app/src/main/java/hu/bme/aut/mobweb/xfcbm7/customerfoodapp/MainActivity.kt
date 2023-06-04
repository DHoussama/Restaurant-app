package hu.bme.aut.mobweb.xfcbm7.customerfoodapp
import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.view.View
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import org.json.JSONObject
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var buttonCart: Button
    private lateinit var buttonAddress: Button
    private lateinit var buttonAllOrders: Button
    private lateinit var arrayList: ArrayList<ListData>
    private val apiURL = "http://10.0.2.2:8000/api/product/list"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("CustomerApp", MODE_PRIVATE)
        buttonCart = findViewById(R.id.btnCart)
        buttonAddress = findViewById(R.id.btnSelectAddress)
        buttonAllOrders = findViewById(R.id.btnAllOrders)
        checkPermissions()

        if (sharedPreferences.getString("login", "false") == "false") {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }

        arrayList = ArrayList()
        fetchData()

        buttonAllOrders.setOnClickListener {
            val intent = Intent(applicationContext, ListOrders::class.java)
            startActivity(intent)
        }

        buttonAddress.setOnClickListener {
            val intent = Intent(applicationContext, Profile::class.java)
            startActivity(intent)
        }

        buttonCart.setOnClickListener {
            val intent = Intent(applicationContext, Cart::class.java)
            startActivity(intent)
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

            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            val adapter = RecyclerViewAdapter(arrayList)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun fetchData() {
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(Request.Method.GET, apiURL,
            Response.Listener<String> { response ->
                parseJSON(response)
            },
            Response.ErrorListener { error: VolleyError ->
                error.printStackTrace()
            }) {}

        queue.add(stringRequest)
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            }
        }
    }
}