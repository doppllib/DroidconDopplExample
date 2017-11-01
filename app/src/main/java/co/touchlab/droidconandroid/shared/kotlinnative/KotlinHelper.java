package co.touchlab.droidconandroid.shared.kotlinnative;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by kgalligan on 10/24/17.
 */

public class KotlinHelper
{
    public static String loadResrouceSeed(String resource)
    {
        InputStream resourceAsStream = KotlinHelper.class.getClassLoader()
                .getResourceAsStream(resource);

        BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream));

        StringBuilder sb = new StringBuilder();
        String line;
        try
        {
            while((line = br.readLine()) != null)
            {
                sb.append(line).append("\n");
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }

        return sb.toString();
    }
}
