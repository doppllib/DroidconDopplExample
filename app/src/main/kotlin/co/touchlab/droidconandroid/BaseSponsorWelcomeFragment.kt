package co.touchlab.droidconandroid

import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import android.widget.ImageView
import com.squareup.picasso.Picasso

open class BaseSponsorWelcomeFragment : Fragment() {
    fun showSponsor(imageView: ImageView, imageName: String, widthRes: Int, heigtRes: Int, url: String) {
        Picasso.with(activity)
                .load(String.format(context.getString(R.string.sponsor_image_url), imageName))
                .resizeDimen(widthRes, heigtRes)
                .into(imageView)
        imageView.setOnClickListener { showSite(url) }
    }

    fun showSite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(intent)
        }
    }
}