package co.touchlab.droidconandroid

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_sponsors2.*

/**
 * Created by sufeizhao on 7/11/17.
 */
class Sponsor2WelcomeFragment : BaseSponsorWelcomeFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_sponsors2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        view?.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        showSponsor(twilio, "twilio", R.dimen.sponsor_platinum_width, R.dimen.sponsor_platinum_height, "https://www.twilio.com/")
        showSponsor(couchbase, "couchbase", R.dimen.sponsor_gold_width, R.dimen.sponsor_gold_height, "http://www.couchbase.com/")
        showSponsor(firebase, "firebase", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "https://www.firebase.com/")
        showSponsor(commonsware, "commonsware", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "https://commonsware.com/")
        showSponsor(dramafever, "dramafever", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "http://www.dramafever.com/")
        showSponsor(bignerdranch, "bignerdranch", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "http://bignerdranch.com/")
        showSponsor(mirego, "mirego", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "http://www.mirego.com/en")
        showSponsor(lyft, "lyft", R.dimen.sponsor_silver_width, R.dimen.sponsor_silver_height, "http://www.lyft.com/")
    }
}