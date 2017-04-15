package co.touchlab.droidconandroid.shared.network;
import java.util.List;

public class SponsorsResult
{
    public int totalSpanCount;

    public List<Sponsor> sponsors;

    public class Sponsor
    {
       public int spanCount;
       public String sponsorName;
       public String sponsorImage;
       public String sponsorLink;

        public Sponsor(int spanCount, String sponsorName, String sponsorImage, String sponsorLink)
        {
            this.spanCount = spanCount;
            this.sponsorName = sponsorName;
            this.sponsorImage = sponsorImage;
            this.sponsorLink = sponsorLink;
        }
    }
}
