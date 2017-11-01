package co.touchlab.droidconandroid.shared.kotlinnative;
import java.util.List;

import co.touchlab.droidconandroid.shared.viewmodel.ConferenceDataViewModel;

/**
 * Created by kgalligan on 10/24/17.
 */

public abstract class KotlinConferenceDataViewModelHost implements ConferenceDataViewModel.Host
{
    @Override
    public void updateConferenceDates(List<Long> dates)
    {
        Long[] longDates = dates.toArray(new Long[dates.size()]);
        long[] ld = new long[longDates.length];
        int index = 0;
        for(Long longDate : longDates)
        {
            ld[index++] = longDate;
        }
        updateConferenceDates(ld);
    }

    public abstract void updateConferenceDates(long[] dates);
}
