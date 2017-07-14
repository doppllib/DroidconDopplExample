package co.touchlab.droidconandroid.ui

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by Ramona Harrison
 * on 7/1/16.
 */
class SabotaToolbarTitle : TextView {
    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    fun init(context: Context?) {
        val sabota: Typeface = Typeface.createFromAsset(context!!.assets, "font/Sabota.otf")
        typeface = sabota
    }
}
