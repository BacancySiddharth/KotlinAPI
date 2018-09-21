package com.example.kotlinapi.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Spannable
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kotlinapi.R
import com.example.kotlinapi.model.RssFeed
import kotlinx.android.synthetic.main.item_news_row.view.*
import kotlinx.android.synthetic.main.item_news_row_type_2.view.*


class NewsAdapterRssFeed(val items: List<RssFeed>, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var imageType: Int = 0
    var type2: Int = 1

    override fun onBindViewHolder(viewholder: RecyclerView.ViewHolder, position: Int) {

        if (viewholder is NewsViewHolder) {
            var holder: NewsViewHolder = viewholder
            holder.titalTxt.text = items[position].title
            holder.detailTxt.text = items[position].description
            if (!items[position].image.isNullOrBlank()) {
                Log.e("image", items[position].image)
//            Picasso.get().load(items[position].image.toString()).resize(600, 200).into(holder.imageView)
            }
            holder.bind(items[position], context, position)
        } else if (viewholder is NewsViewHolderType2) {
            var holder: NewsViewHolderType2 = viewholder
            holder.titalTxt.text = items[position].title
            holder.detailTxt.text = getSpannableString(items[position].description.toString())
            holder.pubdetailTxt.text = items[position].pubDate
        }
    }

    fun getSpannableString(content: String): Spannable {
        val html: Spannable
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            html = Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY, null, null) as Spannable
        } else {
            html = Html.fromHtml(content, null, null) as Spannable
        }
        return html

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == imageType) {
            return NewsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news_row, parent, false))
        } else {
            return NewsViewHolderType2(LayoutInflater.from(context).inflate(R.layout.item_news_row_type_2, parent, false))
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {

        if (!TextUtils.isEmpty(items[position]?.image)) {
            return imageType
        } else {
            return type2
        }
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView = itemView.featuredImage
        val titalTxt = itemView.titleTxt
        val detailTxt = itemView.detailTxt

        fun bind(cate: RssFeed, context: Context, pos: Int) {

            itemView?.featuredImage?.setOnClickListener({
                Toast.makeText(context, pos.toString() + " ", Toast.LENGTH_LONG).show()
            })
        }
    }

    class NewsViewHolderType2(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titalTxt = itemView.type2_titleTxt
        val detailTxt = itemView.type2_detailTxt
        val pubdetailTxt = itemView.type2_pubdateTxt

        fun bind(cate: RssFeed, context: Context, pos: Int) {

            itemView?.featuredImage?.setOnClickListener({
                Toast.makeText(context, pos.toString() + " ", Toast.LENGTH_LONG).show()
            })
        }
    }


}
