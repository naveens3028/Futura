package com.trisys.rn.baseapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.learn.LearnActivity
import com.trisys.rn.baseapp.model.chapter.ChapterResponseData

class SubjectListAdapter(
    val context: Context,
    private val chaptersList: List<ChapterResponseData>,
    val batchId: String
) :
    RecyclerView.Adapter<SubjectListAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chapternametxt = itemView.findViewById(R.id.chapternametxt) as TextView
        val rlt_subj = itemView.findViewById(R.id.rlt_subj) as RelativeLayout
        val txtIndex = itemView.findViewById(R.id.txtIndex) as TextView
        val chapterDetails = itemView.findViewById(R.id.chapterDetails) as AppCompatTextView
        val chapterDetailsMaterials = itemView.findViewById(R.id.chapterDetailsMaterials) as AppCompatTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_subjectlist, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = chaptersList[position]
        holder.chapternametxt.text = (chaptersList[position].courseName)

        holder.txtIndex.text = "" + (position + 1)

        holder.chapterDetails.text = data.topicMaterialResponses?.size.toString() + " Topics"
        holder.chapterDetailsMaterials.text =  data.videoCount.toString() + " Materials"

        when{
            position.toString().last().toString().contains("0")->{
                holder.rlt_subj.setBackgroundResource(R.color.card1)
            }
            position.toString().last().toString().contains("1")->{
                holder.rlt_subj.setBackgroundResource(R.color.card2)
            }
            position.toString().last().toString().contains("2")->{
                holder.rlt_subj.setBackgroundResource(R.color.card3)
            }
            position.toString().last().toString().contains("3")->{
                holder.rlt_subj.setBackgroundResource(R.color.card4)
            }
            position.toString().last().toString().contains("4")->{
                holder.rlt_subj.setBackgroundResource(R.color.card5)
            }
            position.toString().last().toString().contains("5")->{
                holder.rlt_subj.setBackgroundResource(R.color.card6)
            }
            position.toString().last().toString().contains("6")->{
                holder.rlt_subj.setBackgroundResource(R.color.card7)
            }
            position.toString().last().toString().contains("7")->{
                holder.rlt_subj.setBackgroundResource(R.color.card8)
            }
            position.toString().last().toString().contains("8")->{
                holder.rlt_subj.setBackgroundResource(R.color.card9)
            }
            position.toString().last().toString().contains("8")->{
                holder.rlt_subj.setBackgroundResource(R.color.card10)
            }
            else->{
                holder.rlt_subj.setBackgroundResource(R.color.card11)
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, LearnActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("title", chaptersList[position].courseName)
            intent.putExtra("id", chaptersList[position].id)
            intent.putExtra("materials", Gson().toJson(data.topicMaterialResponses))
            intent.putExtra("batchID", batchId)
            Log.e("popPos", position.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return chaptersList.size
    }
}