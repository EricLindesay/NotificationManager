package com.example.notificationmanager

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val data: ArrayList<NotificationInfo?>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTV: TextView
        val descTV: TextView
        val background: ConstraintLayout
        val icon: ImageView

        init {
            // Define click listener for the ViewHolder's View
            titleTV = view.findViewById(R.id.titleTV)
            descTV = view.findViewById(R.id.descTV)
            background = view.findViewById(R.id.frameLayout)
            icon = view.findViewById(R.id.imageView)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    fun getTextColorForBackground(backgroundColor: Int): Int {
        // Extract RGB components
        val red = (Color.red(backgroundColor) / 255.0).toFloat()
        val green = (Color.green(backgroundColor) / 255.0).toFloat()
        val blue = (Color.blue(backgroundColor) / 255.0).toFloat()

        // Calculate luminance
        val luminance = 0.2126 * red + 0.7152 * green + 0.0722 * blue

        // Return black for bright background, white for dark background
        return if (luminance > 0.5) Color.BLACK else Color.WHITE
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.titleTV.text = data[position]?.title
        viewHolder.descTV.text = data[position]?.text
        viewHolder.icon.setImageDrawable(data[position]?.icon)
//        if (data[position] != null && data[position]?.encodedIcon != null) {
//            viewHolder.icon.setImageBitmap(base64ToBitmap(data[position]!!.encodedIcon!!))
//        }
        if (data[position] != null && data[position]?.color != null) {
            val background = viewHolder.background.background
            if (background is GradientDrawable) {
                background.setColor(data[position]!!.color!!)
//                data[position]!!.color { background.setColor(it) }
            }
            // Decide the text colour
            val colour: Int = getTextColorForBackground(data[position]!!.color!!)
            setTextColours(viewHolder, colour)

            // Calculate contrast of text and bg
            val contrast: Double = calculateContrastRatio(data[position]!!.color!!, colour)
            if (contrast < 3) {
                Log.e(MainActivity.PACKAGE, "Contrast is not high enough for guidelines")
            }
        }
//        println("Got title and desc " + dataSet[position]?.title +" " +dataSet[position]?.text)
    }

    fun base64ToBitmap(base64String: String): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }


    fun setTextColours(viewHolder: ViewHolder, colour: Int) {
        viewHolder.titleTV.setTextColor(colour)
        viewHolder.descTV.setTextColor(colour)
    }

    fun calculateContrastRatio(color1: Int, color2: Int): Double {
        fun getRelativeLuminance(color: Int): Double {
            val r = Color.red(color) / 255.0
            val g = Color.green(color) / 255.0
            val b = Color.blue(color) / 255.0

            fun channel(c: Double): Double {
                return if (c <= 0.03928) c / 12.92 else Math.pow((c + 0.055) / 1.055, 2.4)
            }

            return 0.2126 * channel(r) + 0.7152 * channel(g) + 0.0722 * channel(b)
        }

        val luminance1 = getRelativeLuminance(color1)
        val luminance2 = getRelativeLuminance(color2)

        return if (luminance1 > luminance2) {
            (luminance1 + 0.05) / (luminance2 + 0.05)
        } else {
            (luminance2 + 0.05) / (luminance1 + 0.05)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = data.size
}