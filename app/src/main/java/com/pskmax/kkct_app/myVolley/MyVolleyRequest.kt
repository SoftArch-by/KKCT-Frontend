package com.pskmax.kkct_app.myVolley
import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject



class MyVolleyRequest {
    private var mRequestQueue:RequestQueue?=null
    private var context: Context?=null
    private var iVolley:IVolley?=null
    var imageLoader:ImageLoader?=null

    private val requestQueue:RequestQueue
        get(){
            if (mRequestQueue == null){
                mRequestQueue = Volley.newRequestQueue(context!!.applicationContext)}
                return mRequestQueue!!
        }
    private constructor(context: Context,iVolley: IVolley){
        this.context = context
        this.iVolley = iVolley
        mRequestQueue = requestQueue
        this.imageLoader = ImageLoader(mRequestQueue, object : ImageLoader.ImageCache {
            private  val mCache = LruCache<String,Bitmap>(10)
            override fun getBitmap(url: String?): Bitmap? {
                return mCache.get(url)
            }

            override fun putBitmap(url: String?, bitmap: Bitmap?) {
                mCache.put(url,bitmap)
            }

        })
    }
    private constructor(context: Context){
        this.context = context
        mRequestQueue = requestQueue
        this.imageLoader = ImageLoader(mRequestQueue, object : ImageLoader.ImageCache {
            private  val mCache = LruCache<String,Bitmap>(10)
            override fun getBitmap(url: String?): Bitmap? {
                return mCache.get(url)
            }

            override fun putBitmap(url: String?, bitmap: Bitmap?) {
                mCache.put(url,bitmap)
            }

        })
    }

    fun <T> addToRequestQueue(req: Request<T>){
        requestQueue.add(req);
    }

    //simple Get method
    fun getRequest(url:String){
        val getRequest = JsonObjectRequest(Request.Method.GET,url,null,Response.Listener { response->
            iVolley!!.onResponse(response.toString())
        },Response.ErrorListener { error ->
            iVolley!!.onResponse(error.message!!)
        })
        addToRequestQueue(getRequest)
    }

    fun getRequestWithHeader(url: String,accessToken: String){
        val getRequest = object : StringRequest(Request.Method.GET,url,
            Response.Listener { response ->
                iVolley!!.onResponse(response.toString())
            },Response.ErrorListener { error -> iVolley!!.onResponse(error.message!!) })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers["Authorization"] = "Bearer $accessToken"
                return headers
            }
        }
        addToRequestQueue(getRequest)
    }

    //POST method with body
    fun postRequestWithBody(url: String,requestBody:JSONObject){

        val postRequest = object : StringRequest(Request.Method.POST,url,
            Response.Listener { response ->
                iVolley!!.onResponse(response.toString())
            },Response.ErrorListener { error -> iVolley!!.onResponse(error.message!!) })
        {
            override fun getBodyContentType(): String {
                return "application/json"
            }

            override fun getBody(): ByteArray {
                return requestBody.toString().toByteArray(Charsets.UTF_8)
            }
        }
        addToRequestQueue(postRequest)
    }

    //POST method with params
    fun  postRequest(url: String){
        val postRequest = object : StringRequest(Request.Method.POST,url,
            Response.Listener { response ->
                iVolley!!.onResponse(response.toString())
            },Response.ErrorListener { error -> iVolley!!.onResponse(error.message!!) })
        {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String,String>()
                params["email"] = "tsttest@gmail.com"
                params["password"] = "Testt14345678"
                params["citizenID"]     =  "12345678910123"
                return params
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
            addToRequestQueue(postRequest)
    }

    companion object{
        private  var mInstance : MyVolleyRequest? = null
        @Synchronized
        fun getInstance(context: Context) : MyVolleyRequest{
            if(mInstance == null){
                mInstance = MyVolleyRequest(context)
            }
            return  mInstance!!
        }
        @Synchronized
        fun getInstance(context: Context,iVolley: IVolley) : MyVolleyRequest{
            if(mInstance == null){
                mInstance = MyVolleyRequest(context,iVolley)
            }
            return  mInstance!!
        }
    }
}