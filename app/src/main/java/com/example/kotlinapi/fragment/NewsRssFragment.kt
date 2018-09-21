package com.example.kotlinapi.fragment

import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.util.Log.d
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kotlinapi.ApplicationClass
import com.example.kotlinapi.R
import com.example.kotlinapi.adapter.NewsAdapterRssFeed
import com.example.kotlinapi.apiservices.NewsApiService
import com.example.kotlinapi.model.RSS
import com.example.kotlinapi.model.RSSresponse
import com.example.kotlinapi.model.RssFeed
import com.example.kotlinapi.utils.UtilityHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_rss_home.*
import org.json.XML
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayInputStream
import java.nio.charset.Charset
import javax.xml.parsers.DocumentBuilderFactory


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewsRssFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewsRssFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsRssFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    var categorySort: String? = null
    var rssdata: List<RSS> = ArrayList()
    var feedcounter: Int = 0

    //    var mApp: ApplicationClass = (activity?.applicationContext) as ApplicationClass
    var mApp = ApplicationClass.applicationContext()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categorySort = arguments?.getString(ARG_CATE)
//        mApp = activity?.applicationContext as ApplicationClass

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_rss_home, container, false)


        // Open the realm for the UI thread.
        if (categorySort?.equals("BUS")!!) {
            callNewsApiService()
        } else if (categorySort?.toLowerCase().equals("World".toLowerCase())!!) {
            if (UtilityHelper.isConnected(this!!.context!!)) {
                callWorldApiService()
            } else {
                val mApp = (activity?.applicationContext) as ApplicationClass
                var rssfeedlist: ArrayList<RssFeed> = mApp.dbHelper.findProductlist("")
                Log@ d("size", "=>" + rssfeedlist.size)
                if (rssfeedlist != null) {
                    Log@ d("size", "=>recycler")
                    try {
                        newsRecycler?.layoutManager = LinearLayoutManager(this.context)
                        newsRecycler?.adapter = NewsAdapterRssFeed(rssfeedlist, this!!.context!!)
                    } catch (ignor: Exception) {
                        ignor.printStackTrace()
                    }
                }
            }
        } else {
//            loadRssFeedOneByOne()
        }


        return view
    }


    override fun onResume() {
        Log@ d("size", "=>onresume")
        var rssfeedlist: ArrayList<RssFeed> = mApp.dbHelper.findProductlist(this!!.categorySort!!)
        newsRecycler.layoutManager = LinearLayoutManager(this.context)
        setRecyclerAdapter(rssfeedlist)
        loadRssFeedOneByOne()

        val snackbar = Snackbar.make(activity?.findViewById(android.R.id.content)!!, "Welcome To " + rssfeedlist.size, Snackbar.LENGTH_LONG)
        snackbar.show()

        frg_rss_swipelayout.setOnRefreshListener {
            var rssfeedlist: ArrayList<RssFeed> = mApp.dbHelper.findProductlist(this!!.categorySort!!)
            newsRecycler.layoutManager = LinearLayoutManager(this.context)
            setRecyclerAdapter(rssfeedlist)
            loadRssFeedOneByOne()

            val snackbar = Snackbar.make(activity?.findViewById(android.R.id.content)!!, "Welcome To " + rssfeedlist.size, Snackbar.LENGTH_LONG)
            snackbar.show()
        }

        super.onResume()
    }

    fun loadRssFeedOneByOne() {
        rssdata = mApp.dbHelper.loadAllRssFeed(this!!.categorySort!!)
        newsRecycler.layoutManager = LinearLayoutManager(this.context)
        if (rssdata?.size > 0) {
            loadRssfeed(rssdata[feedcounter].link!!)
        }
    }

    fun loadRssfeed(url: String) {
        progressbar?.visibility = View.VISIBLE
        Toast.makeText(activity, "LOAD", Toast.LENGTH_SHORT).show()
        var setsuburl: String = url.substringBeforeLast("/")
        Log@ e("url", "=>BEFORE" + setsuburl)
        Log@ e("url", "=>AFTER" + url.substringAfterLast("/"))

        val news = NewsApiService.loadFeedFromUrl(setsuburl + "/").getSimpleAllRSSNews(url.substringAfterLast("/"))
        news?.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                progressbar?.visibility = View.GONE
                ++feedcounter
                if (rssdata?.size > feedcounter) {
                    loadRssfeed(rssdata[feedcounter].link!!)
                }
                if (frg_rss_swipelayout.isRefreshing) {
                    frg_rss_swipelayout.isRefreshing = false
                }

                Toast.makeText(activity, "FAIL " + url, Toast.LENGTH_SHORT).show()
                Log@ Log.d("res", t.toString())
            }

            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                progressbar?.visibility = View.GONE
                try {

                    ++feedcounter
                    if (rssdata?.size > feedcounter) {
                        loadRssfeed(rssdata[feedcounter].link!!)
                    }
                    if (frg_rss_swipelayout.isRefreshing) {
                        frg_rss_swipelayout.isRefreshing = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }


                try {
                    setRssDOMparser(response?.body().toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {

//                    setRSStoJSONparser(response?.body().toString(), rssdata[feedcounter].title!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun setRecyclerAdapter(datalist: ArrayList<RssFeed>) {
        if (datalist != null) {
            newsRecycler.layoutManager = LinearLayoutManager(this.context)
            newsRecycler.adapter = NewsAdapterRssFeed(datalist, this!!.context!!)
            progressbar.visibility = View.GONE
            if (frg_rss_swipelayout.isRefreshing) {
                frg_rss_swipelayout.isRefreshing = false
            }
        }
    }

    fun callNewsApiService() {
        val news = NewsApiService.newsRss().getBuissness()

        news.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                progressbar?.visibility = View.GONE
                if (frg_rss_swipelayout.isRefreshing) {
                    frg_rss_swipelayout.isRefreshing = false
                }
                Log@ d("res", t.toString())
            }

            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                progressbar?.visibility = View.GONE

                try {
                    setRssDOMparser(response?.body().toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun callWorldApiService() {

        val news = NewsApiService.economicRssNews().getEconomics()
        news.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                progressbar?.visibility = View.GONE
                setRssDOMparser("")
                Log@ Log.d("res", t.toString())
            }

            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                progressbar?.visibility = View.GONE

                try {
                    setRssDOMparser(response?.body().toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun setRSStoJSONparser(respo: String, title: String) {
        val rssfeed: ArrayList<RssFeed> = ArrayList()
        try {
            var xmlJSONObj = XML.toJSONObject(respo)
            Log.d("response", "=>" + xmlJSONObj)
            var resjson: RSSresponse = Gson().fromJson(xmlJSONObj.toString(), RSSresponse::class.java)
            resjson.rss.channel.item.forEach { item ->
                if (!item.imagecontent.url.isNullOrBlank()) {
                    item.image = item.imagecontent.url
                }
                rssfeed.add(RssFeed(item.title, item.description, item.link, item.image, item.pubDate, title))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setNewsRecyclerAdapter(rssfeed)
    }

    fun setRssDOMparser(res: String) {
        Log.e("response", "=>" + res)

        if (frg_rss_swipelayout.isRefreshing) {
            frg_rss_swipelayout.isRefreshing = false
        }

        val rssfeed: ArrayList<RssFeed> = ArrayList()
        val bytes = Charset.forName("UTF-16LE").encode(res).array()
        val inputStream = ByteArrayInputStream(bytes)
        val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream)


        xmlDoc.documentElement.normalize()
        println("Root:" + xmlDoc.documentElement.nodeName)

        val bookList: NodeList = xmlDoc.getElementsByTagName("item")
        println("Root:" + bookList.length)

        for (i in 0..bookList.length - 1) {
            var bookNode: Node = bookList.item(i)

            if (bookNode.getNodeType() === Node.ELEMENT_NODE) {

                val elem = bookNode as Element
                val mMap = mutableMapOf<String, String>()

                for (j in 0..elem.attributes.length - 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mMap.putIfAbsent(elem.attributes.item(j).nodeName, elem.attributes.item(j).nodeValue)
                    }
                }

                try {
//                    println("Image: ${elem.getElementsByTagName("image").item(0).textContent}")
//                    println("Title: ${elem.getElementsByTagName("title").item(0).textContent}")
//                    println("description: ${elem.getElementsByTagName("description").item(0).textContent}")
                    var title = elem.getElementsByTagName("title").item(0).textContent
                    var description = elem.getElementsByTagName("description").item(0).textContent
                    var imagenode: Node? = elem.getElementsByTagName("image").item(0)

                    var link = elem.getElementsByTagName("link").item(0).textContent
                    var pubDate = elem.getElementsByTagName("pubDate").item(0).textContent

                    if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(link) && !TextUtils.isEmpty(pubDate) && !TextUtils.isEmpty(imagenode?.textContent!!)) {
                        rssfeed.add(RssFeed(title, description, link, imagenode?.textContent!!, pubDate))
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
//                println("Genre: ${elem.getElementsByTagName("genre").item(0).textContent}")
//                println("Price: ${elem.getElementsByTagName("price").item(0).textContent}")
//                println("publish_date: ${elem.getElementsByTagName("publish_date").item(0).textContent}")
            }
        }
        setNewsRecyclerAdapter(rssfeed)

    }

    fun setNewsRecyclerAdapter(news: List<RssFeed>) {
        newsRecycler.layoutManager = LinearLayoutManager(context)
        val mApp = (activity?.applicationContext) as ApplicationClass
        /*for (item in news) {
            mApp?.dbHelper.addProduct(item)
        }*/
        news.forEach {
            mApp?.dbHelper.addProduct(it)
        }

        var rssfeedlist: ArrayList<RssFeed> = mApp.dbHelper.findProductlist("")

        Log@ d("size", "=>" + rssfeedlist.size)
        setRecyclerAdapter(rssfeedlist)

    }


    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_CATE = "CATEGORY"

        fun newInstance(param1: String, param2: String): NewsRssFragment {
            val fragment = NewsRssFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_CATE, param2)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(category: String): NewsRssFragment {
            val fragment = NewsRssFragment()
            val args = Bundle()
            args.putString(ARG_CATE, category)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor




