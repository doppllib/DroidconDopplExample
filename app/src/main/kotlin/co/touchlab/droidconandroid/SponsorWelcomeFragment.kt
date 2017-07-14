package co.touchlab.droidconandroid

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_sponsors.*

/**
 * Created by sufeizhao on 7/11/17.
 */
class SponsorWelcomeFragment : BaseSponsorWelcomeFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sponsors, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        view?.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        showSponsor(google, "google", R.dimen.sponsor_platinum_width, R.dimen.sponsor_platinum_height, "http://developer.android.com/")
        showSponsor(facebook, "facebook", R.dimen.sponsor_gold_width, R.dimen.sponsor_gold_height, "https://www.facebook.com/")
        showSponsor(couchbase, "couchbase", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "http://www.couchbase.com/")
        showSponsor(tumblr, "tumblr", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "http://tumblr.com/about")
        showSponsor(genymobile, "genymobile", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "http://www.genymobile.com/")
        showSponsor(uber, "uber", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "https://www.uber.com/")
        showSponsor(microsoft, "microsoft", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "http://azure.microsoft.com/en-us/")
        showSponsor(amex, "amex", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "https://www.americanexpress.com/us/content/mobile/")
        showSponsor(priceline, "priceline", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "http://www.priceline.com/careers")
        showSponsor(paypal, "paypal", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "https://www.braintreepayments.com/")
        showSponsor(veloxity, "veloxity", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "http://veloxity.us/")
        showSponsor(firebase, "firebase", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "https://www.firebase.com/")
        showSponsor(sympli, "sympli", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "http://sympli.io/")
        showSponsor(pluralsight, "pluralsight", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "http://www.pluralsight.com/")
        showSponsor(branch, "branch", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "https://branch.io/")
        showSponsor(generalassembly, "generalassembly", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "https://generalassemb.ly/")
        showSponsor(nimble, "nimble", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "http://www.nimbledroid.com/")
        showSponsor(appgrade, "appgrade", R.dimen.sponsor_bronze_width, R.dimen.sponsor_bronze_height, "http://www.appgrade.com/")
    }
}