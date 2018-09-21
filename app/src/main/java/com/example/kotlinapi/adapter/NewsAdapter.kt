package com.example.kotlinapi.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kotlinapi.R
import com.example.kotlinapi.model.Article
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_news_row.view.*

class NewsAdapter(val items: List<Article>, val context: Context) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
        override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {

            holder.titalTxt.text = items[position].title
            holder.detailTxt.text = items[position].description
            Picasso.get().load(items[position].urlToImage).resize(600, 200).into(holder.imageView)
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
