package co.touchlab.droidconandroid.network.dao;

import co.touchlab.squeaky.field.DatabaseField;
import co.touchlab.squeaky.table.DatabaseTable;

/**
 * Created by izzyoji :) on 8/12/15.
 */
@DatabaseTable
public class Block extends co.touchlab.droidconandroid.data.Block
{
    @DatabaseField
    public String startDate;

    @DatabaseField
    public String endDate;

}
