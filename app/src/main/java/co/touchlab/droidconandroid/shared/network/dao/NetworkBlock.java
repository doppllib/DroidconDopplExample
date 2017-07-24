package co.touchlab.droidconandroid.shared.network.dao;

import co.touchlab.squeaky.field.DatabaseField;
import co.touchlab.squeaky.table.DatabaseTable;

/**
 * Created by izzyoji :) on 8/12/15.
 */
@DatabaseTable
public class NetworkBlock extends co.touchlab.droidconandroid.shared.data.Block
{
    @DatabaseField
    public String startDate;

    @DatabaseField
    public String endDate;

}
