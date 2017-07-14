package co.touchlab.droidconandroid

import android.app.SearchManager
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.touchlab.android.threading.eventbus.EventBusExt
import co.touchlab.android.threading.tasks.TaskQueue
import co.touchlab.droidconandroid.UserDetailActivity.Companion.USER_CODE
import co.touchlab.droidconandroid.shared.data.UserAccount
import co.touchlab.droidconandroid.shared.tasks.AbstractFindUserTask
import co.touchlab.droidconandroid.shared.tasks.FindUserTask
import co.touchlab.droidconandroid.shared.utils.EmojiUtil
import co.touchlab.droidconandroid.utils.Toaster
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user_detail.*
import org.apache.commons.lang3.StringUtils

/**
 * Created by kgalligan on 7/27/14.
 */

class UserDetailFragment : Fragment() {

    companion object {
        private val TWITTER_PREFIX: String = "http://www.twitter.com/"
        private val GPLUS_PREFIX: String = "http://www.google.com/+"
        private val LINKEDIN_PREFIX: String = "http://www.linkedin.com/in/"
        private val FACEBOOK_PREFIX: String = "http://www.facebook.com/"
        private val PHONE_PREFIX: String = "tel:"
        private val TAG = UserDetailFragment::class.java.simpleName

        interface FinishListener {
            fun onFragmentFinished()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBusExt.getDefault().register(this)
        TaskQueue.loadQueueNetwork(activity).execute(FindUserTask(findUserCodeArg()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_detail, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.title = ""
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusExt.getDefault().unregister(this)
    }

    private fun findUserCodeArg(): String {
        var userCode = arguments?.getString(USER_CODE)
        if (userCode.isNullOrBlank()) {
            userCode = activity.intent.getStringExtra(USER_CODE)
        }

        if (userCode.isNullOrBlank()) {
            Log.e(TAG, "Must set user code")
            Toaster.showMessage(activity, getString(R.string.no_user_code_error))

            if (activity is UserDetailActivity)
                (activity as UserDetailActivity).onFragmentFinished()
        }

        return userCode!!
    }

    fun onEventMainThread(findUserTask: AbstractFindUserTask) {
        if (findUserTask.isError) {
            Log.e(TAG, "User not found")
            Toaster.showMessage(activity, getString(R.string.network_error))

            if (activity is UserDetailActivity)
                (activity as UserDetailActivity).onFragmentFinished()
        } else {
            val userAccount = findUserTask.user
            showUserData(userAccount)
        }
    }

    private fun showUserData(userAccount: UserAccount) {
        val avatarKey = userAccount.avatarImageUrl()
        if (!avatarKey.isNullOrBlank()) {
            val callback = object : Callback {
                override fun onSuccess() {
                    placeholder_emoji.text = ""
                }

                override fun onError() {
                    placeholder_emoji?.let {
                        it.text = EmojiUtil.getEmojiForUser(userAccount.name)
                    }
                }
            }

            Picasso.with(activity)
                    .load(avatarKey)
                    .placeholder(R.drawable.circle_profile_placeholder)
                    .into(profile_image, callback)
        } else {
            placeholder_emoji.text = EmojiUtil.getEmojiForUser(userAccount.name)
        }

        val iconsDefaultColor = ContextCompat.getColor(activity, R.color.social_icons)
        makeIconsPretty(iconsDefaultColor)

        if (!userAccount.name.isNullOrBlank()) {
            name.text = userAccount.name
        }

        if (!userAccount.company.isNullOrBlank()) {
            company.text = userAccount.company
            company.visibility = View.VISIBLE

            company2.text = userAccount.company
            company2.visibility = View.VISIBLE
            company2.setOnClickListener {
                val intent = Intent(Intent.ACTION_WEB_SEARCH)
                val keyword = userAccount.company
                intent.putExtra(SearchManager.QUERY, keyword)
                startActivity(intent)
            }
        }

        val facebookAccount = userAccount.facebook
        if (!facebookAccount.isNullOrBlank()) {
            facebook.text = facebookAccount
            facebook.setOnClickListener {
                openLink(Uri.parse(FACEBOOK_PREFIX + facebookAccount))
            }
            facebook.visibility = View.VISIBLE
        }

        var twitterAccount = userAccount.twitter
        if (!twitterAccount.isNullOrBlank()) {
            twitterAccount = twitterAccount.replace("@", "")
            twitter.text = "@$twitterAccount"
            twitter.setOnClickListener {
                openLink(Uri.parse(TWITTER_PREFIX + twitterAccount))
            }
            twitter.visibility = View.VISIBLE
        }

        val linkedInAccount = userAccount.linkedIn
        if (!linkedInAccount.isNullOrBlank()) {
            linkedIn.text = linkedInAccount
            linkedIn.setOnClickListener {
                openLink(Uri.parse(LINKEDIN_PREFIX + linkedInAccount))
            }
            linkedIn.visibility = View.VISIBLE
        }

        var gPlusAccount = userAccount.gPlus
        if (!gPlusAccount.isNullOrBlank()) {
            gPlusAccount = gPlusAccount.replace("+", "")
            gPlus.text = "+$gPlusAccount"
            gPlus.setOnClickListener {
                openLink(Uri.parse(GPLUS_PREFIX + gPlusAccount))
            }
            gPlus.visibility = View.VISIBLE
        }

        if (!userAccount.website.isNullOrBlank()) {
            website.text = userAccount.website
            website.setOnClickListener {
                var url = userAccount.website

                if (!url.startsWith("http://")) {
                    url = "http://" + url
                }
                openLink(Uri.parse(url))
            }
            website.visibility = View.VISIBLE
        }

        if (!userAccount.profile.isNullOrBlank()) {
            bio.text = Html.fromHtml(StringUtils.trimToEmpty(userAccount.profile)!!)
            bio.visibility = View.VISIBLE
        }
    }

    private fun openLink(webPage: Uri?) {
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        if (intent.resolveActivity(activity.packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun makeIconsPretty(darkVibrantColor: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val contactDrawable = ContextCompat.getDrawable(activity,
                    R.drawable.vic_person_add_black_24dp)
            contactDrawable.colorFilter = PorterDuffColorFilter(darkVibrantColor,
                    PorterDuff.Mode.SRC_IN)

            val companyDrawable = ContextCompat.getDrawable(activity,
                    R.drawable.vic_company_black_24dp)
            companyDrawable.colorFilter = PorterDuffColorFilter(darkVibrantColor,
                    PorterDuff.Mode.SRC_IN)
            company2.setCompoundDrawablesWithIntrinsicBounds(companyDrawable, null, null, null)

            val websiteDrawable = ContextCompat.getDrawable(activity,
                    R.drawable.vic_website_black_24dp)
            websiteDrawable.colorFilter = PorterDuffColorFilter(darkVibrantColor,
                    PorterDuff.Mode.SRC_IN)
            website.setCompoundDrawablesWithIntrinsicBounds(websiteDrawable, null, null, null)

            val bioDrawable = ContextCompat.getDrawable(activity, R.drawable.vic_bio_black_24dp)
            bioDrawable.colorFilter = PorterDuffColorFilter(darkVibrantColor,
                    PorterDuff.Mode.SRC_IN)
            bio.setCompoundDrawablesWithIntrinsicBounds(bioDrawable, null, null, null)
        }
    }
}