package com.trisys.rn.baseapp.practiceTest.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.text.Spanned
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jstarczewski.pc.mathview.src.TextAlign
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.AnswerClickListener
import com.trisys.rn.baseapp.model.AnswerChooseItem
import kotlinx.android.synthetic.main.row_answer_choose_list.view.*
import org.sufficientlysecure.htmltextview.HtmlFormatter
import org.sufficientlysecure.htmltextview.HtmlFormatterBuilder
import org.sufficientlysecure.htmltextview.HtmlResImageGetter


class AnswerChooseAdapter(
    val context: Context,
    private val answerChooseItem: ArrayList<AnswerChooseItem>,
    private val answerClickListener: AnswerClickListener,
    private val questionPosition: Int,
    private val isReview: Boolean
) : RecyclerView.Adapter<AnswerChooseAdapter.ViewHolder>() {

    private var previousPosition = -1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_answer_choose_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val answerItem = answerChooseItem[position]
        if (answerItem.isSelected) previousPosition = position


// loads html from string and displays cat_pic.png from the app's drawable folder

// loads html from string and displays cat_pic.png from the app's drawable folder
//        val formattedHtml: Spanned = HtmlFormatter.formatHtml(
//            HtmlFormatterBuilder().setHtml(answerItem.answer.toString())
//                .setImageGetter(HtmlResImageGetter(holder.itemView.answer.context))
//        )

//        Log.d("Spanned",formattedHtml.toString())
//        holder.itemView.answer.text = formattedHtml
//        holder.itemView.html_text.text = formattedHtml
//
//        holder.itemView.webview.getSettings().setLoadWithOverviewMode(true)
//        holder.itemView.webview.getSettings().setUseWideViewPort(true)
//        holder.itemView.webview.getSettings().setDefaultFontSize(58)
//        val ans = answerItem.answer!!.replace("\n","").replace("<p class=\\\"p4\\\">","")
//        holder.itemView.formula_one.text = ans
//
        val ans = answerItem.answer!!.replace("\n","").replace("<p class=\\\"p4\\\">","")
        holder.itemView.mvTest.apply {
            textZoom = 60
            textColor = Color.GREEN.toString()
            textAlign = TextAlign.LEFT
            backgroundColor = Color.TRANSPARENT.toString()
            text = ans
        }


        //webView
//        val data =  "<!DOCTYPE html>\n" +
//                "<html>\n" +
//                "  <head>\n" +
//                "    <title>Title of the document</title>\n" +
//                "  </head>\n" +
//                "  <body style=\"margin: 0; padding: 0\">\n $ans" +
//                "   \n" +
//                "  </body>\n" +
//                "</html>"
//
//        Log.d("data", data)
//        holder.itemView.webview.loadData(data, "text/html", "UTF-8")

//HTML TEXT View
//        if(answerItem.answer.toString().contains("img src=\\\"data:image")){
//            val re = Regex("\\<.*?\\>")
//            val data = re.replace(answerItem.answer.toString().trim(), "")
//            Log.e("Img Data", data)
//        }

//        val re = Regex("\\<.*?\\>")
//        holder.itemView.answer.text = re.replace(answerItem.answer.toString().trim(), "")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            holder.itemView.answer.text =
//                Html.fromHtml(answerItem.answer.toString(), HtmlCompat.FROM_HTML_MODE_COMPACT)
//        }

        if (!isReview) {
            holder.itemView.setOnClickListener {

                if (!answerItem.isSelected) {
                    if (previousPosition > -1) {
                        answerChooseItem[previousPosition].isSelected = false
                        notifyItemChanged(previousPosition)
                    }
                    answerItem.isSelected = true
                    notifyItemChanged(position)
                    val answer: Char = (position + 97).toChar()
                    answerClickListener.onAnswerClicked(true, answer, questionPosition)
                } else {
                    answerItem.isSelected = false
                    notifyItemChanged(position)
                    answerClickListener.onAnswerClicked(false, '-', questionPosition)
                }
            }
        }

        if (answerItem.isSelected) {
            holder.itemView.answer.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_check_circle,
                0
            )
            holder.itemView.answer.background.setTint(
                ContextCompat.getColor(
                    context,
                    R.color.tea_green
                )
            )
        } else if (isReview && position == 1) {
            holder.itemView.answer.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_close_outline,
                0
            )
            holder.itemView.answer.background.setTint(
                ContextCompat.getColor(
                    context,
                    R.color.pale_pink
                )
            )
        } else {
            holder.itemView.answer.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_circle_outline,
                0
            )
            holder.itemView.answer.background.setTint(
                ContextCompat.getColor(
                    context,
                    R.color.alice_blue
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return answerChooseItem.size
    }
}