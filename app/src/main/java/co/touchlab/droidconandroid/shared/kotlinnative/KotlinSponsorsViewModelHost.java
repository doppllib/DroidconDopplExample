package co.touchlab.droidconandroid.shared.kotlinnative;
import android.support.annotation.NonNull;

import java.util.List;

import co.touchlab.droidconandroid.shared.viewmodel.SponsorsViewModel;

/**
 * Created by kgalligan on 10/24/17.
 */

public abstract class KotlinSponsorsViewModelHost implements SponsorsViewModel.Host
{
    @Override
    public void onShowSponsors(@NonNull List<SponsorsViewModel.SponsorSection> sections)
    {
        onShowSponsors(sections.toArray(new SponsorsViewModel.SponsorSection[sections.size()]));
    }

    public abstract void onShowSponsors(@NonNull SponsorsViewModel.SponsorSection[] sections);
}
