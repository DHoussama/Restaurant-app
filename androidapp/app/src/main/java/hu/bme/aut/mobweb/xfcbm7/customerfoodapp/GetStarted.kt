package hu.bme.aut.mobweb.xfcbm7.customerfoodapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView




class GetStarted : AppCompatActivity() {

    private lateinit var getStarted: TextView
    private lateinit var tryDemo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)



    getStarted = findViewById(R.id.getStarted)

    getStarted.setOnClickListener {
        val intent = Intent(applicationContext, Login::class.java)
        startActivity(intent)
        finish()
    }

        tryDemo = findViewById(R.id.tryDemo)

        tryDemo.setOnClickListener {
            val intent = Intent(applicationContext, MainActivityDemoMode::class.java)
            startActivity(intent)
            finish()
        }
    }
}