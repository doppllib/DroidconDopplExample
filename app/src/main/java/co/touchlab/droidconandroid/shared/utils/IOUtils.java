package co.touchlab.droidconandroid.shared.utils;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kgalligan on 10/29/16.
 */

public class IOUtils
{
    public static String toString(InputStream input) throws IOException
    {
        StringBuilder buf = new StringBuilder();

        BufferedReader in = null;
        try
        {
            in = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            String str;

            while((str = in.readLine()) != null)
            {
                buf.append(str);
            }
        }
        finally
        {
            if(in != null)
                in.close();
        }

        return buf.toString();
    }
}
