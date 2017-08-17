package co.touchlab.droidconandroid.shared.interactors;


import co.touchlab.doppl.testing.DopplJunitTestHelper;

public class OneTest
{
    public static int runDopplResources(String resourceName)
    {
        return DopplJunitTestHelper.runResource(resourceName);
    }
}
