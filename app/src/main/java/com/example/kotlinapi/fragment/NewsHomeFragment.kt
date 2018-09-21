package com.example.kotlinapi.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kotlinapi.model.Article
import com.example.kotlinapi.model.NewsApiRes
import com.example.kotlinapi.R
import com.example.kotlinapi.apiservices.NewsApiService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_news_home.*
import kotlinx.android.synthetic.main.item_news_row.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewsHomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewsHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsHomeFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_news_home, container, false)

        callNewsApiService()

        return view
    }

    fun callNewsApiService() {
        val news = NewsApiService.create().getTopHeadlines("us", "business", "ab757e67c36e4d38aaba442122f9f3ad")

        news.enqueue(object : Callback<NewsApiRes> {
            override fun onFailure(call: Call<NewsApiRes>?, t: Throwable?) {
                Log@ Log.d("res", t.toString())
            }

            override fun onResponse(call: Call<NewsApiRes>?, response: Response<NewsApiRes>?) {
                Log@ Log.d("res", "=>" + response?.body())
                var news: NewsApiRes? = response?.body()
                Log@ Log.d("res", "=>" + news?.articles?.size)
                Log@ Log.d("res", "=>" + news?.status)
                progressbar.visibility = View.GONE
                setNewsRecyclerAdapter(response?.body())
            }
        })
    }

    fun setNewsRecyclerAdapter(news: NewsApiRes?) {
        categoryRecycler.layoutManager = LinearLayoutManager(context)
        var newslist: List<Article> = ArrayList()
        if (news != null) {
            categoryRecycler.adapter = NewsAdapter(news.articles, this!!.context!!)
        }
//        categoryRecycler.adapter = news?.articles?.toList()?.let { NewsAdapterRssFeed(newslist, this) }
    }

    class NewsAdapter(val items: List<Article>, val context: Context) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
        override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {

            holder.titalTxt.text = items[position].title
            holder.detailTxt.text = items[position].description
            Picasso.get().load(items[position].urlToImage).into(holder.imageView)
            holder.bind(items[position], context, position)
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
            return NewsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news_row, parent, false))

        }

        override fun getItemCount(): Int {
            return items.size
        }

        class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val imageView = itemView.featuredImage
            val titalTxt = itemView.titleTxt
            val detailTxt = itemView.detailTxt

            fun bind(cate: Article, context: Context, pos: Int) {

                itemView?.featuredImage?.setOnClickListener({
                    Toast.makeText(context, pos.toString() + " ", Toast.LENGTH_LONG).show()
                })
            }
        }

    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): NewsHomeFragment {
            val fragment = NewsHomeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): NewsHomeFragment {
            val fragment = NewsHomeFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
