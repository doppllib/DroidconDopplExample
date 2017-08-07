package co.touchlab.droidconandroid

import android.app.Activity
import android.view.View
import co.touchlab.droidconandroid.shared.utils.TimeUtils
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by kgalligan on 7/21/14.
 */
fun Activity.findView(id: Int): View {
    return this.findViewById(id)
}

fun View.findView(id: Int): View {
    return this.findViewById(id)
}

fun Int.isOdd(): Boolean = this % 2 != 0

fun Long.formatDate(format: String): String = TimeUtils.makeDateFormat(format).format(this)

fun Int.calculateAlphaPercentage(totalScrollRange: Int): Float =
        1 - (Math.abs(this).toFloat() / totalScrollRange)

fun View.setViewVisibility(value: Boolean) {
    this.visibility = if (value) View.VISIBLE else View.INVISIBLE
}

fun <T> T.just() = Observable.just(this)
fun <T> Array<T>.from() = Observable.fromArray(this)
fun <T> Iterable<T>.from() = Observable.fromIterable(this)
fun <T> (() -> T).fromCallable() = Single.fromCallable(this)