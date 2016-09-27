package co.touchlab.droidconandroid

import android.app.Activity
import android.view.View

/**
 * Created by kgalligan on 7/21/14.
 */
fun Activity.findView(id : Int) : View
{
    return this.findViewById(id)!!
}

fun View.findView(id: Int): View
{
    return this.findViewById(id)!!
}