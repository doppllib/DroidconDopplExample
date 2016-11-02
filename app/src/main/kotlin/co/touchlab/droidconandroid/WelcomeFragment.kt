package co.touchlab.droidconandroid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.wnafee.vector.compat.ResourcesCompat

/**
 *
 * Created by izzyoji :) on 7/22/15.
 */
class WelcomeFragment : Fragment() {


    companion object
    {
        private val BACKGROUND_COLOR_RES = "background_color_res"
        private val IMAGE_RES = "image_res"
        private val TEXT_COLOR_RES = "text_color_res"
        private val TITLE_RES = "title_res"
        private val DESC_RES = "desc_res"

        fun newInstance(backgroundColorRes: Int, imageRes: Int, textColorRes: Int, titleRes: Int, descRes: Int): WelcomeFragment
        {
            val fragment = WelcomeFragment()
            val args = Bundle()
            args.putInt(BACKGROUND_COLOR_RES, backgroundColorRes)
            args.putInt(IMAGE_RES, imageRes)
            args.putInt(TEXT_COLOR_RES, textColorRes)
            args.putInt(TITLE_RES, titleRes)
            args.putInt(DESC_RES, descRes)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return  inflater!!.inflate(R.layout.fragment_welcome, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val bundle = getArguments()
        view?.setBackgroundColor(getResources().getColor(bundle.getInt(BACKGROUND_COLOR_RES)))

        val root = getView()
        val image = root?.findViewById(R.id.image)!! as ImageView
        val imageRes = bundle.getInt(IMAGE_RES)
        image.setImageDrawable(ResourcesCompat.getDrawable(getActivity(), imageRes))


        val titleTV = root?.findViewById(R.id.title)!! as TextView
        titleTV.setText(bundle.getInt(TITLE_RES))
        titleTV.setTextColor(getResources().getColor(bundle.getInt(TEXT_COLOR_RES)))

        val descTV = root?.findViewById(R.id.description)!! as TextView
        descTV.setText(bundle.getInt(DESC_RES))
        descTV.setTextColor(getResources().getColor(bundle.getInt(TEXT_COLOR_RES)))

    }
}

open class BaseSponsorWelcomeFragment : Fragment() {

    fun showSponsor(root: View, logoId: Int, imageName: String, widthRes: Int, heigtRes: Int, url: String)
    {
        val image = root.findViewById(logoId)!! as ImageView
        Picasso
                .with(getActivity())
                .load("file:///android_asset/sponsors/"+ imageName +".png")
                .resizeDimen(widthRes, heigtRes)
                .into(image)
        image.setOnClickListener { v -> showSite(url) }
    }

    fun showSite(url: String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null)
        {
            getActivity().startActivity(intent);
        }
    }
}

class SponsorWelcomeFragment : BaseSponsorWelcomeFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return  inflater!!.inflate(R.layout.fragment_sponsors, null)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val root = view!!

        root.setBackgroundColor(getResources().getColor(android.R.color.white))

        showSponsor(root, R.id.google, "google", R.dimen.sponsor_platinum_width, R.dimen.sponsor_platinum_height, "http://developer.android.com/")

        showSponsor(root, R.id.facebook, "facebook", R.dimen.sponsor_gold_width, R.dimen.sponsor_gold_height, "https://www.facebook.com/")

        showSponsor(root, R.id.couchbase, "couchbase", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "http://www.couchbase.com/")
        showSponsor(root, R.id.tumblr, "tumblr", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "http://tumblr.com/about")
        showSponsor(root, R.id.genymobile, "genymobile", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "http://www.genymobile.com/")
        showSponsor(root, R.id.uber, "uber", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "https://www.uber.com/")
        showSponsor(root, R.id.microsoft, "microsoft", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "http://azure.microsoft.com/en-us/")
        showSponsor(root, R.id.amex, "amex", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "https://www.americanexpress.com/us/content/mobile/")

        showSponsor(root, R.id.priceline, "priceline", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "http://www.priceline.com/careers")
        showSponsor(root, R.id.paypal, "paypal", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "https://www.braintreepayments.com/")
        showSponsor(root, R.id.veloxity, "veloxity", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "http://veloxity.us/")
        showSponsor(root, R.id.firebase, "firebase", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "https://www.firebase.com/")
        showSponsor(root, R.id.sympli, "sympli", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "http://sympli.io/")
        showSponsor(root, R.id.pluralsight, "pluralsight", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "http://www.pluralsight.com/")
        showSponsor(root, R.id.branch, "branch", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "https://branch.io/")
        showSponsor(root, R.id.generalassembly, "generalassembly", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "https://generalassemb.ly/")
        showSponsor(root, R.id.nimble, "nimble", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "http://www.nimbledroid.com/")
        showSponsor(root, R.id.appgrade, "appgrade", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "http://www.appgrade.com/")
    }
}

class Sponsor2WelcomeFragment : BaseSponsorWelcomeFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return  inflater!!.inflate(R.layout.fragment_sponsors2, null)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val root = view!!

        root.setBackgroundColor(getResources().getColor(android.R.color.white))

        showSponsor(root, R.id.twilio, "twilio", R.dimen.sponsor_platinum_width, R.dimen.sponsor_platinum_height, "https://www.twilio.com/")

        showSponsor(root, R.id.couchbase, "couchbase", R.dimen.sponsor_gold_width, R.dimen.sponsor_gold_height, "http://www.couchbase.com/")

        showSponsor(root, R.id.firebase, "firebase", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "https://www.firebase.com/")
        showSponsor(root, R.id.commonsware, "commonsware", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "https://commonsware.com/")

        showSponsor(root, R.id.dramafever, "dramafever", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "http://www.dramafever.com/")
        showSponsor(root, R.id.bignerdranch, "bignerdranch", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "http://bignerdranch.com/")

        showSponsor(root, R.id.mirego, "mirego", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "http://www.mirego.com/en")
        showSponsor(root, R.id.lyft, "lyft", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "http://www.lyft.com/")

    }



}
