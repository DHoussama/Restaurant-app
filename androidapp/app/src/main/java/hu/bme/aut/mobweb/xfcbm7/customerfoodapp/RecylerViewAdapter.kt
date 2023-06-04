package hu.bme.aut.mobweb.xfcbm7.customerfoodapp
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso

class RecyclerViewAdapter(private val listdata: ArrayList<ListData>, private val mode: Boolean =false) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private val removeUrl =
        "http://10.0.2.2:8000/api/users/cart/remove"
    private val addUrl =
        "http://10.0.2.2:8000/api/users/cart/add"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        sharedPreferences = context.getSharedPreferences("CustomerApp", Context.MODE_PRIVATE)
        val layoutInflater = LayoutInflater.from(parent.context)

        val listItem = layoutInflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(listItem)


    }



    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = listdata[position]
        holder.textViewTitle.text = "${list.name} - $${list.price}"
        holder.textViewDes.text = list.des
        Picasso.get().load(list.img).into(holder.imageView)
        if (mode) {
        holder.buttonAdd.isVisible= false
        holder.buttonRemove.isVisible= false }
        holder.buttonAdd.setOnClickListener {
            it.isEnabled = false
            sendRequest(
                it,
                "$addUrl?item_id=${list.id}&user_email=${sharedPreferences.getString("email", "")}"
            )
        }
        holder.buttonRemove.setOnClickListener {
            it.isEnabled = false
            sendRequest(
                it,
                "$removeUrl?item_id=${list.id}&user_email=${sharedPreferences.getString("email", "")}"
            )
        }


    }

    override fun getItemCount(): Int {
        return listdata.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val textViewDes: TextView = itemView.findViewById(R.id.textViewDes)
        val buttonAdd: ImageButton = itemView.findViewById(R.id.add_item)
        val buttonRemove: ImageButton = itemView.findViewById(R.id.remove_item)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        var numItem = 0
    }

    private fun sendRequest(v: View, apiUrl: String) {
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(Request.Method.GET, apiUrl,
            Response.Listener { response ->
                v.isEnabled = true
                if (response == "success") {
                    Toast.makeText(context, "Operation success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                v.isEnabled = true
                error.printStackTrace()
            })
        queue.add(stringRequest)
    }

}