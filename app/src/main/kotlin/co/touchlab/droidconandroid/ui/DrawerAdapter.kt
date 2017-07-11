package co.touchlab.droidconandroid.ui

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.touchlab.droidconandroid.R
import co.touchlab.droidconandroid.setViewVisibility
import kotlinx.android.synthetic.main.item_drawer.view.*

class DrawerAdapter(private val context: Context,
                    private val dataSet: List<Any>,
                    private val drawerClickListener: DrawerClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var selectedPos = 1

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        when (viewType) {
            VIEW_TYPE_NAVIGATION -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_drawer, parent, false)
                return NavigationViewHolder(view)
            }
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_drawer_header, parent, false)
                return HeaderViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_drawer_divider, parent, false)
                return DividerViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_HEADER -> {
                val headerHolder = holder as HeaderViewHolder
                headerHolder.itemView.setOnClickListener {
                    if (selectedPos != position)
                        drawerClickListener.onHeaderItemClick()
                }
            }
            VIEW_TYPE_NAVIGATION -> {
                val navItem = dataSet[position] as NavigationItem
                val view = (holder as NavigationViewHolder).itemView
                view.title.setText(navItem.titleRes)
                val drawable = ContextCompat.getDrawable(context, navItem.iconRes)

                view.isSelected = selectedPos == position
                if (view.isSelected) {
                    drawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context,
                            R.color.nav_icon_highlight), PorterDuff.Mode.SRC_IN)
                    view.highlight.visibility = View.VISIBLE
                } else {
                    drawable.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context,
                            R.color.drawer_icons), PorterDuff.Mode.SRC_IN)
                    view.highlight.visibility = View.GONE
                }
                view.icon.setImageDrawable(drawable)
                view.action_icon.setViewVisibility(navItem.showAction)

                view.setOnClickListener {
                    if (selectedPos != position)
                        drawerClickListener.onNavigationItemClick(position, navItem.titleRes)
                }
            }
        }
    }

    fun setSelectedPosition(position: Int) {
        selectedPos = position
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemViewType(position: Int): Int {
        when {
            position == 0 -> return VIEW_TYPE_HEADER
            dataSet[position] is NavigationItem -> return VIEW_TYPE_NAVIGATION
            else -> return VIEW_TYPE_DIVIDER
        }
    }

    class NavigationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class DividerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private val VIEW_TYPE_HEADER: Int = 0
        private val VIEW_TYPE_NAVIGATION: Int = 1
        private val VIEW_TYPE_DIVIDER: Int = 2
    }
}

interface DrawerClickListener {
    fun onNavigationItemClick(position: Int, titleRes: Int)
    fun onHeaderItemClick()
}

class NavigationItem(val titleRes: Int, val iconRes: Int, val showAction: Boolean = false)