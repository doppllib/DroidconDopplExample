package co.touchlab.droidconandroid.shared.network.dao;
import java.util.List;

public class SponsorsResult
{
    public int totalSpanCount;

    public List<Sponsor> sponsors;

    public class Sponsor
    {
       public final int spanCount;
       public final String sponsorName;
       public final String sponsorImage;
       public final String sponsorLink;

        public Sponsor(int spanCount, String sponsorName, String sponsorImage, String sponsorLink)
        {
            this.spanCount = spanCount;
            this.sponsorName = sponsorName;
            this.sponsorImage = sponsorImage;
            this.sponsorLink = sponsorLink;
        }

        public int getSpanCount()
        {
            return spanCount;
        }

        public String getSponsorName()
        {
            return sponsorName;
        }

        public String getSponsorImage()
        {
            return sponsorImage;
        }

        public String getSponsorLink()
        {
            return sponsorLink;
        }
    }


}
