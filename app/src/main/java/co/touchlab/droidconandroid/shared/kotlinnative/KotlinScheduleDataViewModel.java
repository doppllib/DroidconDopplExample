package co.touchlab.droidconandroid.shared.kotlinnative;
import co.touchlab.droidconandroid.shared.viewmodel.DaySchedule;
import co.touchlab.droidconandroid.shared.viewmodel.ScheduleDataViewModel;

/**
 * Created by kgalligan on 10/24/17.
 */

public abstract class KotlinScheduleDataViewModel implements ScheduleDataViewModel.Host
{
    @Override
    public void loadCallback(DaySchedule[] daySchedules)
    {
        loadCallback(new DayScheduleCollection(daySchedules));
    }

    public abstract void loadCallback(DayScheduleCollection dayScheduleCollection);

    public static class DayScheduleCollection
    {
        final DaySchedule[] daySchedules;

        public DayScheduleCollection(DaySchedule[] daySchedules)
        {
            this.daySchedules = daySchedules;
        }

        public int size()
        {
            return daySchedules.length;
        }

        public DaySchedule get(int i)
        {
            return daySchedules[i];
        }
    }
}
