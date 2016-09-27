package co.touchlab.droidconandroid.utils;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by izzyoji :) on 7/30/15.
 */
public class CustomTarget implements Target
{
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
    {

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable)
    {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable)
    {

    }
}
