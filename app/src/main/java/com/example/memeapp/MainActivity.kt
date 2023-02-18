package com.example.memeapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var currentImageUrl : String?= null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GenerateMeme()
    }

    fun ShareMethod(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT ,"Hey Checkout this cool meme i have got it from $currentImageUrl")
        val chooser = Intent.createChooser(intent,"Share this meme using...")
        startActivity(chooser)
    }
    fun GenerateMeme() {
        //instantiate the Queue
        progressBar.visibility = View.VISIBLE
        //we should use Singleton pattern in vollley library.....it is class which helps us create only one instance of volley library in our project in for multiple api requests/volley requests for that first we have to create the class for singelton pattern and copy code from docs
        //val queue = Volley.newRequestQueue(this) //It is in normal pattern for volley we dont want to create the request queue
        //request queue is queue which holds all the requests for api in our app and gives api requests one by one in Queue manner
        //val url = "https://meme-api.herokuapp.com/gimme" as it stops working
        val url = "https://meme-api.com/gimme"
        //request a json response from the provided url
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                currentImageUrl = response.getString("url")
                Glide.with(applicationContext).load(currentImageUrl).listener(object :  RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                }).into(memeimage)
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Something went Wrong", Toast.LENGTH_SHORT).show()
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun NextMethod(view: View) {
        GenerateMeme()
    }
}