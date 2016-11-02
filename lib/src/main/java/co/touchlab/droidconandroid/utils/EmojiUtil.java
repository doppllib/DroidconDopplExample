package co.touchlab.droidconandroid.utils;
import android.text.TextUtils;

/**
 * Created by Ramona Harrison
 * on 7/13/16.
 */

public class EmojiUtil
{
    public static int DECIMAL_VALUE_A = 97;

    public static int[] EMOJI_ABC = {
            0x1F34E, // apple
            0x1F430, // bunny
            0x1F425, // chick
            0x1F436, // dog
            0x1F346, // eggplant
            0x1F438, // frog
            0x1F347, // grapes
            0x1F439, // hamster
            0x1F368, // ice cream
            0x1F456, // jeans
            0x1F428, // koala
            0x1F981, // lion
            0x1F42D, // mouse
            0x1F443, // nose
            0x1F419, // octopus
            0x1F43C, // panda
            0x1F31B, // quarter moon
            0x1F916, // robot
            0x1F575, // spy
            0x1F422, // turtle
            0x1F984, // unicorn
            0x1F596, // vulcan hand
            0x1F349, // watermelon
            0x1F47E, // extraterrestrial
            0x270C,  // y hand
            0x1F634  // zzz smiley
    };

    public static String getEmojiForUser(String displayName)
    {
        int unicode = 0x1F60A; // default smiley

        if(! TextUtils.isEmpty(displayName))
        {
            int c = displayName.toLowerCase().charAt(0) - DECIMAL_VALUE_A;
            if(c >= 0 && c < EMOJI_ABC.length)
            {
                unicode = EMOJI_ABC[c];
            }
        }

        return new String(Character.toChars(unicode));
    }
}
