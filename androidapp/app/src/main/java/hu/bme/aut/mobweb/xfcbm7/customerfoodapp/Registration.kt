package hu.bme.aut.mobweb.xfcbm7.customerfoodapp

import android.content.Intent
import android.location.GnssAntennaInfo
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import javax.xml.transform.ErrorListener

class Registration : AppCompatActivity() {


    // Declare your views
    private lateinit var editTextName: TextInputEditText
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var textViewError: TextView
    private lateinit var textViewLogin: TextView
    private lateinit var buttonSubmit: Button
    private lateinit var progressBar: ProgressBar

    // Declare your variables
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        editTextName = findViewById(R.id.name)
        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        textViewError = findViewById(R.id.error)
        textViewLogin = findViewById(R.id.loginNow)
        buttonSubmit = findViewById(R.id.submit)
        progressBar = findViewById(R.id.loading)
        textViewLogin.setOnClickListener {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }
        buttonSubmit.setOnClickListener {
            name = editTextName.text.toString()
            email = editTextEmail.text.toString()
            password = editTextPassword.text.toString()

            val url =
                "http://10.0.2.2:8000/api/users/register"
            val queue = Volley.newRequestQueue(applicationContext)

            val stringRequest = object : StringRequest(Method.POST, url,
                Response.Listener<String> { response ->
                    if (response.contains("success")) {
                        Toast.makeText(applicationContext, "Account created", Toast.LENGTH_LONG).show()
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, response, Toast.LENGTH_LONG).show()
                    }
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["name"] = name
                    params["email"] = email
                    params["password"] = password
                    return params
                }
            }
            queue.add(stringRequest)
        }
    }
}