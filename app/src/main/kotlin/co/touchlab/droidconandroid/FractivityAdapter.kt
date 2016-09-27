package co.touchlab.droidconandroid

import android.app.Activity
import android.os.Bundle

/**
 * Created by kgalligan on 7/26/14.
 */
open class FractivityAdapter(val c : Activity, savedInstanceState: Bundle?)
{
    open fun onStop()
    {

    }

    open fun onDestroy()
    {

    }

    open fun onSaveInstanceState(outState: Bundle)
    {

    }
}

abstract class FractivityAdapterActivity() : Activity()
{
    var adapter : FractivityAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        adapter = createAdapter(savedInstanceState)
    }

    abstract fun createAdapter(savedInstanceState: Bundle?): FractivityAdapter

    override fun onDestroy()
    {
        super.onDestroy()
        adapter!!.onDestroy()
    }

    override fun onStop()
    {
        super.onStop()
        adapter!!.onStop()
    }


    override fun onSaveInstanceState(outState: Bundle)
    {
        super<Activity>.onSaveInstanceState(outState)
        adapter!!.onSaveInstanceState(outState)
    }
}