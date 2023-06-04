package hu.bme.aut.mobweb.xfcbm7.customerfoodapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject

class Login : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonSubmit: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewError: TextView
    private lateinit var textViewRegister: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPreferences = getSharedPreferences("CustomerApp", MODE_PRIVATE)

     sharedPreferences = getSharedPreferences("CustomerApp", MODE_PRIVATE)
     if (sharedPreferences.getString("login", "false") == "true") {
           val intent = Intent(applicationContext, MainActivity::class.java)
          startActivity(intent)
            finish()
        }

        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        buttonSubmit = findViewById(R.id.submit)
        progressBar = findViewById(R.id.loading)
        textViewError = findViewById(R.id.error)
        textViewRegister = findViewById(R.id.registerNow)

        textViewRegister.setOnClickListener {
            val intent = Intent(applicationContext, Registration::class.java)
            startActivity(intent)
            finish()
        }

        buttonSubmit.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            textViewError.visibility = View.GONE
            email = editTextEmail.text.toString()
            password = editTextPassword.text.toString()

            val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
            val url = "http://10.0.2.2:8000/api/users/login"

            val stringRequest = object : StringRequest(Method.POST, url,
                Response.Listener<String> { response ->
                    progressBar.visibility = View.GONE
                    try {
                        val jsonObject = JSONObject(response)
                        val status = jsonObject.getString("status")
                        val message = jsonObject.getString("message")
                        if (status == "success") {
                            name = jsonObject.getString("name")
                            email = jsonObject.getString("email")
                            val myEdit: SharedPreferences.Editor = sharedPreferences.edit()
                            myEdit.putString("login", "true")
                            myEdit.putString("name", name)
                            myEdit.putString("email", email)
                            myEdit.apply()
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            textViewError.text = message
                            textViewError.visibility = View.VISIBLE
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error: VolleyError ->
                    progressBar.visibility = View.GONE
                    textViewError.text = error.localizedMessage
                    textViewError.visibility = View.VISIBLE
                    error.printStackTrace()
                }) {
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["email"] = email
                    params["password"] = password
                    return params
                }
            }

            queue.add(stringRequest)
        }
    }

}