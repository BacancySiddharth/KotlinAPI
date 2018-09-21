package com.example.kotlinapi.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log.d
import android.util.Log.e
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import com.example.kotlinapi.ApplicationClass
import com.example.kotlinapi.R
import com.example.kotlinapi.adapter.NewsAdapter
import com.example.kotlinapi.apiservices.NewsApiService
import com.example.kotlinapi.fragment.NewsHomeFragment
import com.example.kotlinapi.fragment.NewsRssFragment
import com.example.kotlinapi.model.NewsApiRes
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //    var mApp: ApplicationClass = (applicationContext) as ApplicationClass
    val Context.mApp: ApplicationClass
        get() = applicationContext as ApplicationClass

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentholder, NewsHomeFragment.newInstance())
                    .commit()
        }


        drawer_layout.setDrawerElevation(0f)
//        drawer_layout.setScrimColor(Color.TRANSPARENT)
        navigation.setNavigationItemSelectedListener(this)
//        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_drawer)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_drawer)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(false)

        var mDrawerToggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_drawer)

        swipelayout?.setOnRefreshListener { callNewsApiService() }

        mApp?.dbHelper.addRssData("INDIA TV", "https://www.indiatvnews.com/rssnews/topstory.xml")
        mApp?.dbHelper.addRssData("INDIA TODAY", "https://www.indiatoday.in/rss/1206578")
        mApp?.dbHelper.addRssData("TOI", "https://timesofindia.indiatimes.com/rssfeeds/-2128936835.cms")
        mApp?.dbHelper.addRssData("ZEE", "http://zeenews.india.com/rss/world-news.xml")
        mApp?.dbHelper.addRssData("ABC", "https://abcnews.go.com/abcnews/topstories")
        mApp?.dbHelper.addRssData("HindustanTimes", "https://www.hindustantimes.com/rss/india/rssfeed.xml")

        loadNavigationList()

    }

    private fun loadNavigationList() {
//        var rssdata: List<RSS> = ArrayList()
        var rssdata = mApp.dbHelper.loadAllRssFeed("")
        var nameArray: ArrayList<String> = ArrayList()
        nameArray.add("ALL")
        for (name in rssdata) {
            Log@ e("name", "=" + name.link + " :: " + name.title)
            nameArray.add(name.title!!)
        }


        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, nameArray)
        navigation_listview.adapter = adapter as ListAdapter?

        navigation_listview.setOnItemClickListener { adapterView, view, pos, l ->

            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentholder, NewsRssFragment.newInstance(nameArray[pos]))
                    .commit()
            drawer_layout.closeDrawer(GravityCompat.START)
            val snackbar = Snackbar.make(findViewById(android.R.id.content), "Welcome To " + nameArray[pos], Snackbar.LENGTH_LONG)
            snackbar.show()
        }

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.home -> {
                supportFragmentManager
                        .beginTransaction()
                        .add(R.id.fragmentholder, NewsHomeFragment.newInstance())
                        .commit()
            }
            R.id.drw_item_category -> {
                // Handle the camera action
                Toast.makeText(this, "TOI", Toast.LENGTH_LONG).show()
                val snackbar = Snackbar.make(findViewById(android.R.id.content), "Welcome To TOI", Snackbar.LENGTH_LONG)
                snackbar.show()
                supportFragmentManager
                        .beginTransaction()
                        .add(R.id.fragmentholder, NewsRssFragment.newInstance("BUS"))
                        .commit()
            }

            R.id.drw_item_news -> {
                // Handle the camera action
                Toast.makeText(this, "ECONOMICS", Toast.LENGTH_LONG).show()
                val snackbar = Snackbar.make(findViewById(android.R.id.content), "Welcome Economics Times", Snackbar.LENGTH_LONG)
                snackbar.show()
                supportFragmentManager
                        .beginTransaction()
                        .add(R.id.fragmentholder, NewsRssFragment.newInstance("World"))
                        .commit()
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun callNewsApiService() {
        val news = NewsApiService.create().getTopHeadlines("us", "business", "ab757e67c36e4d38aaba442122f9f3ad")

        news.enqueue(object : Callback<NewsApiRes> {
            override fun onFailure(call: Call<NewsApiRes>?, t: Throwable?) {
                Log@ d("res", t.toString())
            }

            override fun onResponse(call: Call<NewsApiRes>?, response: Response<NewsApiRes>?) {
                Log@ d("res", "=>" + response?.body())
                var news: NewsApiRes? = response?.body()
                Log@ d("res", "=>" + news?.articles?.size)
                Log@ d("res", "=>" + news?.status)
                setNewsRecyclerAdapter(response?.body())
            }
        })
    }

    fun setNewsRecyclerAdapter(news: NewsApiRes?) {
        categoryRecycler.layoutManager = LinearLayoutManager(this)
//        var newslist: List<Article> = ArrayList()
        if (news != null) {
            categoryRecycler.adapter = NewsAdapter(news.articles, this)
        }
//        categoryRecycler.adapter = news?.articles?.toList()?.let { NewsAdapterRssFeed(newslist, this) }
    }


}

/*
 fun setRecyclerAdapter(json: String) {


        val datares: CategoryRes = Gson().fromJson(json, CategoryRes::class.java)
        val categorylist: ArrayList<String> = ArrayList()
        categorylist.add(datares.contents.categories.art)
        categorylist.add(datares.contents.categories.funny)
        categorylist.add(datares.contents.categories.inspire)
        categorylist.add(datares.contents.categories.life)
        categorylist.add(datares.contents.categories.love)
        categorylist.add(datares.contents.categories.management)
        categorylist.add(datares.contents.categories.students)
        categorylist.add(datares.contents.categories.sports)
        Log@ d("res", "=>" + categorylist.size + " --" + datares.contents.categories.art)
        categoryRecycler.layoutManager = LinearLayoutManager(this)
        categoryRecycler.adapter = RecyclerAdapter(categorylist, this)


    }


 fun callApiService() {
        val apiService1 = WikiApiService.create().hitCountCheckRectro()

        apiService1.enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                Log@ d("res", t.toString())
            }

            override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                println(response.toString())

                Log@ d("res", "=>" + response?.body().toString())
                setRecyclerAdapter(response?.body().toString())

            }
        })
        Log.d("REQUEST", apiService1.toString() + "")
    }


class RecyclerAdapter(val items: ArrayList<String>, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHolder) {
            holder.categoryTxt?.text = items[position].toLowerCase()
            holder.bind(items[position], context, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecyclerAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_row, parent, false))

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val categoryTxt = itemView?.categoryTxt
        fun bind(cate: String, context: Context, pos: Int) {
            itemView?.categoryTxt?.setOnClickListener({
                Toast.makeText(context, pos.toString() + " " + cate, Toast.LENGTH_LONG).show()
            })
        }
    }

}
*/
